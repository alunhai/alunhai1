#!/usr/bin/env bash

BaseDir=$(cd `dirname $0`;pwd)
InstallDir=$BaseDir/install
SourceDir=$InstallDir/sources
DistDir=$InstallDir/dist

mkdir -p $SourceDir
mkdir -p $DistDir

### 下载需要的安装包
spark_name="spark-2.4.8-bin-hadoop2.7"
livy_name="apache-livy-0.7.1-incubating-bin"
#cd $SourceDir
#wget https://downloads.apache.org/incubator/livy/0.7.1-incubating/apache-livy-0.7.1-incubating-bin.zip
#wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-2.4.8/spark-2.4.8-bin-hadoop2.7.tgz
#
#### 配置安装环境
#cd $DistDir
#unzip $SourceDir/livy.zip
#tar -xzvf $SourceDir/spark.tgz
#cd $spark_name
#cp conf/slaves.template conf/slaves
#./sbin/start-all.sh
#
#spark_master="spark://$(hostname).localdomain:7077"
#export SPARK_HOME=$DistDir/spark

### 初始化mysql
mysql_user="mhy"
mysql_password="localhost"
mysql_db="movie_analyse"
mysql -u$mysql_user -p$mysql_password -e "create database if not exists $mysql_db"
mysql -u$mysql_user -p$mysql_password -D$mysql_db -e "source $BaseDir/src/main/resources/init.sql"

curl http://localhost:5000/movie-analyser/init?type=movie
curl http://localhost:5000/movie-analyser/init?type=rating
curl http://localhost:5000/movie-analyser/init?type=tag
curl http://localhost:5000/movie-analyser/init?type=user

