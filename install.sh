#!/usr/bin/env bash

set -x
set -e

BaseDir=$(cd `dirname $0`;pwd)
InstallDir=$BaseDir/install
SourceDir=$InstallDir/sources
DistDir=$InstallDir/dist
ConfDir=$BaseDir/conf

ENV_FILE="${BaseDir}/env.sh"
echo "#!/usr/bin/env bash" > $ENV_FILE

mkdir -p $SourceDir
mkdir -p $DistDir

### 下载需要的安装包
cd $SourceDir

spark_name="spark-2.4.8-bin-hadoop2.7"

if [ ! -f $spark_name.tgz ];then
  wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-2.4.8/spark-2.4.8-bin-hadoop2.7.tgz
fi

hadoop_name="hadoop-2.10.1"
if [ ! -f $hadoop_name.tar.gz ];then
  wget https://mirrors.tuna.tsinghua.edu.cn/apache/hadoop/core/hadoop-2.10.1/hadoop-2.10.1.tar.gz
fi

node_name="node-v16.13.0-linux-x64"
if [ ! -f $node_name.tar.xz ];then
  wget https://nodejs.org/dist/v16.13.0/node-v16.13.0-linux-x64.tar.xz
fi

#### 配置安装环境
cd $DistDir
if [ ! -d $spark_name ];then
  tar -xzvf $SourceDir/$spark_name.tgz
  cd $spark_name
  cp conf/slaves.template conf/slaves
  ./sbin/start-all.sh
fi

export SPARK_MASTER="spark://$(hostname):7077"
echo export SPARK_MASTER="spark://$(hostname).localdomain:7077" >> $ENV_FILE
export SPARK_HOME=$DistDir/$spark_name
echo export SPARK_HOME=$DistDir/$spark_name >> $ENV_FILE

cd $DistDir
export HADOOP_HOME=$DistDir/$hadoop_name
echo export HADOOP_HOME=$DistDir/$hadoop_name >> $ENV_FILE
if [ ! -d $hadoop_name ];then
  tar -xzvf $SourceDir/$hadoop_name.tar.gz
  cd $HADOOP_HOME
  envsubst < $ConfDir/core-site.xml > $HADOOP_HOME/etc/hadoop/core-site.xml
  envsubst < $ConfDir/hdfs-site.xml > $HADOOP_HOME/etc/hadoop/hdfs-site.xml
  cp $ConfDir/hadoop-env.sh $HADOOP_HOME/etc/hadoop/
  echo "export JAVA_HOME=${JAVA_HOME}" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh
  mkdir -p $HADOOP_HOME/tmp
  mkdir -p $HADOOP_HOME/data/name
  mkdir -p $HADOOP_HOME/data/data
  $HADOOP_HOME/bin/hdfs namenode -format
  $HADOOP_HOME/sbin/start-dfs.sh
fi

### 初始化mysql
mysql_user="root"
mysql_password="3306"
mysql_db="movie_analyse"
mysql -u$mysql_user -p$mysql_password -e "create database if not exists $mysql_db"
mysql -u$mysql_user -p$mysql_password -D$mysql_db -e "source $BaseDir/src/main/resources/init.sql"

echo export MYSQL_USER="${mysql_user}" >> $ENV_FILE
echo export MYSQL_PASSWORD="${mysql_password}" >> $ENV_FILE

$HADOOP_HOME/bin/hdfs dfs -put $BaseDir/ml-latest /

sh $BaseDir/gradlew product
#java -jar $BaseDir/build/libs/alunhai1-1.0-SNAPSHOT.jar &
#
#curl http://localhost:5000/movie-analyser/init?type=movie
#curl http://localhost:5000/movie-analyser/init?type=user

