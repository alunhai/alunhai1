#!/usr/bin/env bash

BaseDir=$(cd `dirname $0`;pwd)
InstallDir=$BaseDir/install
SourceDir=$InstallDir/sources
DistDir=$InstallDir/dist

mkdir -p $SourceDir
mkdir -p $DistDir

### 下载需要的安装包
cd $SourceDir
wget https://www.apache.org/dyn/closer.lua/incubator/livy/0.7.1-incubating/apache-livy-0.7.1-incubating-bin.zip -O livy.zip
wget https://mirrors.tuna.tsinghua.edu.cn/apache/spark/spark-2.4.8/spark-2.4.8-bin-hadoop2.7.tgz -O spark.tgz

### 配置安装环境
cd $DistDir
unzip $SourceDir/livy.zip
tar -xzvf $SourceDir/spark.tgz


### 初始化mysql数据
mysql_user="root"
mysql_password="3306"
mysql_db="movie_analyse"
mysql -u$mysql_user -p$mysql_password -d$mysql_db -e "source $BaseDir/src/main/resources/init.sql"
mysql -u$mysql_user -p$mysql_password -d$mysql_db -e "load data infile '$BaseDir/ml-latest/movies.csv' into table student character set utf8 fields terminated by ',' optionally enclosed by '"' escaped by '"'
                                                           lines terminated by '\r' "




