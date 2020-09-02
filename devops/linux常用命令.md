lsof -i:3306 

查看3306端口占用情况



启动jenkins

sudo service jenkins start





ssh-keygen -t rsa -C "xxxxx@xxxxx.com" 





sftp相关命令



chmod -R o+r+w



```
kill -9 `ps -ef | grep jyb-activity-provider| grep -v grep | awk '{print $2}'`
```

这点命令是来杀进程的

kill -9 pid

pid = ps -ef | grep jyb-activity-provider| grep -v grep | awk '{print $2}'

首先 用 ps 搜出进程信息

ps -ef  查出进程信息  类似 ps -aux

|  管道  前一个命令的输出为后一个命令的输入

ps -ef | grep jenkins-project

搜出有关 jenkins-project的进程信息 发现里面有个 grep相关的

过滤 再次进行管道 | grep -v grep

即过滤 grep相关的进程信息

ps -ef | grep jenkins-project | grep -v grep 

现在发现只有一条了

问题来了，如何取出pid   pid是第二个列

awk  详情在awk学习 [awk学习.md](awk学习.md) 

