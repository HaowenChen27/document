参考文档：

https://nacos.io/zh-cn/docs/quick-start.html

采用下载源码方式进行

区别：因为服务器网络速度受限，使用本地git clone源码，使用ftp上传到服务器。

遇到问题：

启动失败：

```java
java.lang.IllegalArgumentException: db.num is null
```

执行conf下的nacos-mysql.sql脚本

修改 conf下的配置文件 关于数据库配置部分

然后又出现了下一个错误：

Unable to start embedded Tomcat



单机运行  sh startup.sh -m standalone



http://localhost:8848/nacos/#/login ，默认账号密码都是nacos