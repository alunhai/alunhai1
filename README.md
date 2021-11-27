## 安装步骤

### 准备
某些基础设施无法或者没有写在安装脚本中，需要自行安装
- mysql or mariadb
- node
- npm

下载项目
```shell
git clone https://github.com/alunhai/alunhai1.git
cd alunhai1
```

将数据集ml-latest解压到项目根目录alunhai1下

### 脚本安装
使用脚本可以配置绝大部分环境
```shell
sh install.sh
```
验证：
1. 打开网页http://localhost:8080，如果spark ui可以正常显示，且存在worker，表示spark启动成功
2. 打开网页http://localhost:50070，如果hdfs ui可以正常显示，表示hadoop安装成功

### 启动
1. 配置环境变量
```shell
source env.sh
```
2. 启动server
```shell
java -jar $BaseDir/build/libs/alunhai1-1.0-SNAPSHOT.jar
```
3. 第一次启动时，需初始化数据库的数据，另起一个terminal，执行
```shell
curl http://localhost:5000/movie-analyser/init?type=movie
curl http://localhost:5000/movie-analyser/init?type=user
```
4. 启动webui
```shell
cd webui && npm install && npm run serve -- --port 3000
```
初始登录用户(admin/admin123)