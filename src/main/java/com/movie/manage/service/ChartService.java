package com.movie.manage.service;

import com.google.common.io.CharStreams;
import com.movie.manage.entity.Triplet;
import org.apache.hadoop.fs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ChartService {
  private static final Logger logger = LoggerFactory.getLogger(ChartService.class);

  private final String sparkHome;
  private final String sparkMaster;
  private final String taskJarPath;
  private final String resultBase;
  private final FileSystem fs;
  private Process process;
  private long qid;
  private volatile AtomicInteger calStat;

  @Autowired
  public ChartService(FileSystem fs) {
    this.sparkHome = System.getenv("SPARK_HOME");
    this.sparkMaster = System.getenv("SPARK_MASTER");
    this.taskJarPath = System.getenv("ANALYSER_DIR");
    this.resultBase = "/analyse";
    this.fs = fs;
    process = null;
    calStat = new AtomicInteger(-1);
  }

  public void resetStat() {
    calStat = new AtomicInteger(-1);
  }

  public void submitTask() throws Exception {
    qid = System.currentTimeMillis();

    String command = String.format("%s/bin/spark-submit --class com.movie.analyse.tasks.Main --master %s %s %d",
        sparkHome, sparkMaster, taskJarPath, qid);
    logger.info("exec command: " + command);

    process = Runtime.getRuntime().exec(command);
    TimeUnit.SECONDS.sleep(1);
    if (!process.isAlive()) {
      int status = process.exitValue();
      if (status != 0) {
        throw new Exception("failed to submit query");
      }
    }
  }

  public void getQueryStatus() throws Exception {
    // 先检查子进程
    if (process.isAlive()) {
      logger.info("process for {} is running", qid);
      return;
    }
    int status = process.exitValue();
    if (status != 0) {
      String error = CharStreams.toString(new InputStreamReader(process.getErrorStream()));
      throw new Exception(error);
    }

    // 再检查hdfs文件是否写成功
    String[] names = new String[]{"gender", "age", "occupation", "zipcode"};
    for (String name : names) {
      Path path = new Path(String.format("%s/%s-%d.csv", resultBase, name, qid));
      Path successPath = new Path(path, "_SUCCESS");
      logger.info("check path {}", successPath);
      if (!(fs.exists(path) && fs.exists(successPath))) {
        logger.info("data is not ready for {}", qid);
        return;
      }
    }
    calStat.compareAndSet(0, 1);
  }

  public List<Triplet> readQueryResult(String name) throws Exception {
    if(calStat.compareAndSet(-1, 0)) {
      submitTask();
      return null;
    }
    if(calStat.get() == 0) {
      getQueryStatus();
      return null;
    }
    Path path = new Path(String.format("%s/%s-%d.csv", resultBase, name, qid));
    List<Path> files = new ArrayList<>();
    RemoteIterator<LocatedFileStatus> fileStatues = fs.listFiles(path, false);
    while (fileStatues.hasNext()) {
      files.add(fileStatues.next().getPath());
    }

    List<Triplet> triplets = new ArrayList<>();
    for (Path file : files) {
      try (FSDataInputStream fos = fs.open(file);
           BufferedReader br = new BufferedReader(new InputStreamReader(fos))) {
        String line;
        while ((line = br.readLine()) != null) {
          String[] sps = line.split(",");
          triplets.add(new Triplet(sps[0], sps[1], Double.parseDouble(sps[2])));
        }
      }
    }
    return triplets;
  }

}
