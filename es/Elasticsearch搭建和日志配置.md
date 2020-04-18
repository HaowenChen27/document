## Elasticsearch

### 一、搭建Elasticsearch

- 下载并安装.tar.gz包

  

  ```shell
  wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.5.4.tar.gz
  wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-6.5.4.tar.gz.sha512  
  shasum -a 512 -c elasticsearch-6.5.4.tar.gz.sha512 ①
  tar -xzf elasticsearch-6.5.4.tar.gz
  cd elasticsearch-6.5.4/ ②
  ```

  ①：比较下载的.tar.gz存档的SHA和发布的校验和，该校验和应输出elasticsearch- {version} .tar.gz：OK。

  ②：该目录称为$ ES_HOME。

- 启动

  

  ```shell
  ./bin/elasticsearch
  ```

- 作为进程启动 ( -p 是可选择的)

  

  ```shell
  ./bin/elasticsearch -d -p pid
  ```

### 二、配置Elasticsearch

- 配置文件位置

  Elasticsearch有三个配置文件：

  ​     elasticsearch.yml用于配置Elasticsearch
   ​     用于配置Elasticsearch JVM设置的jvm.options
   ​     log4j2.properties用于配置Elasticsearch日志记录

  这些文件位于config目录中，config目录位置默认为$ ES_HOME / config。 可以通过ES_PATH_CONF环境变量更改config目录的位置，如下所示：

  

  ```shell
  ES_PATH_CONF=/path/to/my/config ./bin/elasticsearch
  ```

- 配置elasticsearch.yml

  

  ```yml
  # ======================== Elasticsearch 配置文件 =========================
  #
  # NOTE: Elasticsearch 有默认的配置。
  #
  # 以下是一些常用的重要的配置
  #
  # 可以访问以下网站获取更多配置信息
  # https://www.elastic.co/guide/en/elasticsearch/reference/index.html
  #
  # ---------------------------------- Cluster 集群 -------------------------------
  #
  # Use a descriptive name for your cluster:给集群起个名字
  #
  cluster.name: cluster01
  #
  # ------------------------------------ Node 节点信息 -----------------------------
  #
  # Use a descriptive name for the node:给节点起个名字
  #
  node.name: node01
  #
  # Add custom attributes to the node: 给节点添加自定义属性
  #
  #node.attr.rack: r1
  #
  # ----------------------------------- Paths 路径 -----------------------------------
  #
  # 存储数据的路径 (separate multiple locations by comma，如果有多个路径，以逗号分隔):
  # 默认是在  $ES_HOme/data
  #
  #path.data: /path/to/data
  #
  # 存储日志的路径：$ES_HOme/logs
  #
  #path.logs: /path/to/logs
  #
  # ----------------------------------- Memory -----------------------------------
  #
  # Lock the memory on startup:当启动的时候锁定内存
  #
  #bootstrap.memory_lock: true
  #
  # Make sure that the heap size is set to about half the memory available
  # on the system and that the owner of the process is allowed to use this
  # limit.
  # 确保将堆大小设置为系统上可用内存的大约一半，并允许进程的所有者使用此限制。
  #
  # Elasticsearch performs poorly when the system is swapping the memory.
  # 当系统交换内存时，Elasticsearch的性能很差。
  #
  # ---------------------------------- Network -----------------------------------
  # 绑定的ip地址
  #network.host: 192.168.0.1
  #
  # 绑定的端口
  #
  #http.port: 9200
  #
  # For more information, consult the network module documentation.
  #
  # --------------------------------- Discovery ----------------------------------
  #
  # Pass an initial list of hosts to perform discovery when new node is started:
  # The default list of hosts is ["127.0.0.1", "[::1]"]
  #
  #discovery.zen.ping.unicast.hosts: ["host1", "host2"]
  #
  # Prevent the "split brain" by configuring the majority of nodes (total number of master-eligible nodes / 2 + 1):
  #
  #discovery.zen.minimum_master_nodes: 
  #
  # For more information, consult the zen discovery module documentation.
  #
  # ---------------------------------- Gateway -----------------------------------
  #
  # Block initial recovery after a full cluster restart until N nodes are started:
  #
  #gateway.recover_after_nodes: 3
  #
  # For more information, consult the gateway module documentation.
  #
  # ---------------------------------- Various -----------------------------------
  #
  # Require explicit names when deleting indices:
  #
  #action.destructive_requires_name: true
  ```

- 配置Logging

  - 使用log4j2.properties文件配置Log4j2。
  - Elasticsearch公开了三个属性：
    - $ {sys：es.logs.base_path}，：将解析为日志目录
    - $ {sys：es.logs.cluster_name}：将解析为群集名称（在默认配置中用作日志文件名的前缀）
    - $ {sys：es.logs.node_name}（：将解析为节点名称（如果明确设置了节点名称）

  例如： 如果您的日志目录（path.logs）是/var/log/elasticsearch，并且您的集群名为production,
   那么 $ {sys:es.logs.base_path}将解析为 /var/log/elasticsearch, $ {sys:es.logs.base_path} $ {sys:file.separator} $ {sys:es.logs.cluster_name} .log将解析为/var/log/elasticsearch/production.log。



```properties
##1. 配置RollingFile appender 
appender.rolling.type = RollingFile 
appender.rolling.name = rolling
##2.Log to /日志路径/集群名字.log 
appender.rolling.fileName = ${sys:es.logs.base_path}${sys:file.separator}${sys:es.logs.cluster_name}.log 
appender.rolling.layout.type = PatternLayout
appender.rolling.layout.pattern = [%d{ISO8601}][%-5p][%-25c{1.}] [%node_name]%marker %.-10000m%n
##3. 将日志滚动到/日志路径/集群名字-yyyy-MM-dd-i.log;日志将在每个卷上压缩， i递增
appender.rolling.filePattern = ${sys:es.logs.base_path}${sys:file.separator}${sys:es.logs.cluster_name}-%d{yyyy-MM-dd}-%i.log.gz 
appender.rolling.policies.type = Policies
#4. 使用基于时间的滚动策略
appender.rolling.policies.time.type = TimeBasedTriggeringPolicy 
#5. 每天滚动日志
appender.rolling.policies.time.interval = 1 
#6.按照自然天计算（而不是每隔二十四小时滚动）
appender.rolling.policies.time.modulate = true 
#7. 使用基于大小的滚动策略
appender.rolling.policies.size.type = SizeBasedTriggeringPolicy 
#8. 256 MB后滚动日志
appender.rolling.policies.size.size = 256MB 
appender.rolling.strategy.type = DefaultRolloverStrategy
appender.rolling.strategy.fileIndex = nomax
#9. 滚动日志时使用删除操作
appender.rolling.strategy.action.type = Delete 
appender.rolling.strategy.action.basepath = ${sys:es.logs.base_path}
#10. 仅删除与文件模式匹配的日志
appender.rolling.strategy.action.condition.type = IfFileName 
#11. 该模式仅删除主日志
appender.rolling.strategy.action.condition.glob = ${sys:es.logs.cluster_name}-* 
#12. 仅在我们累积了太多压缩日志时才删除
appender.rolling.strategy.action.condition.nested_condition.type = IfAccumulatedFileSize 
#13. 压缩日志的大小条件为2 GB
appender.rolling.strategy.action.condition.nested_condition.exceeds = 2GB
```

注意： Log4j的配置解析被任何无关的空格弄糊涂了; 如果您在此页面上复制并粘贴任何Log4j设置，或者一般输入任何Log4j配置，请务必修剪任何前导和尾随空格。

注意，您可以在appender.rolling.filePattern中使用.zip替换.gz，以使用zip格式压缩滚动日志。 如果删除.gz扩展名，则日志将不会在滚动时进行压缩。

如果要在指定的时间段内保留日志文件，可以使用具有删除操作的翻转策略。

配置如下：



```properties
#1. Configure the DefaultRolloverStrategy 
appender.rolling.strategy.type = DefaultRolloverStrategy 
#2. Configure the Delete action for handling rollovers 
appender.rolling.strategy.action.type = Delete 
#3. The base path to the Elasticsearch logs 
appender.rolling.strategy.action.basepath = ${sys:es.logs.base_path} 
#4. The condition to apply when handling rollovers 
appender.rolling.strategy.action.condition.type = IfFileName 
#5. Delete files from the base path matching the glob ${sys:es.logs.cluster_name}-*; this is the glob that log files are rolled to; this is needed to only delete the rolled Elasticsearch logs but not also delete the deprecation and slow logs 
appender.rolling.strategy.action.condition.glob = ${sys:es.logs.cluster_name}-* 
#6. A nested condition to apply to files matching the glob 
appender.rolling.strategy.action.condition.nested_condition.type = IfLastModified 
#7. Retain logs for seven days 
appender.rolling.strategy.action.condition.nested_condition.age = 7D 
```

更多的配置请参考：http://logging.apache.org/log4j/2.x/manual/configuration.html

通过 `log4j2.properties` 配置日志级别：



```properties
logger.<unique_identifier>.name = <name of logging hierarchy>
logger.<unique_identifier>.level = <level>
```

例如：



```properties
logger.transport.name = org.elasticsearch.transport
logger.transport.level = trace
```