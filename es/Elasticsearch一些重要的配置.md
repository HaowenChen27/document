## Elasticsearch一些重要的配置

虽然Elasticsearch只需要很少的配置，但在投入生产之前必须考虑以下设置。

- 路径设置
- 群集名称
- 节点名称
- 网络主机
- 发现设置
- 堆大小
- 堆转储路径（JVM heap dump path）
- GC记录
- 临时目录

### 一、路径设置（`path.data` and `path.logs`）

如果您使用.zip或.tar.gz存档，则数据和日志目录是$ES_HOME的子文件夹。 如果这些重要文件夹保留在其默认位置，则在将Elasticsearch升级到新版本时，存在删除它们的高风险。在生产使用中，您肯定需要更改数据和日志文件夹的位置：



```yml
path:
  logs: /var/log/elasticsearch
  data: /var/data/elasticsearch
```

path.data设置可以设置为多个路径，在这种情况下，所有路径都将用于存储数据（尽管属于单个分片的文件将全部存储在同一数据路径中）：



```yml
path:
  data:
    - /mnt/elasticsearch_1
    - /mnt/elasticsearch_2
    - /mnt/elasticsearch_3
```

## 二、群集名称（`cluster.name`）

节点只能在与群集中的所有其他节点共享其cluster.name时加入群集。 默认名称为elasticsearch，但您应将其更改为适当的名称，该名称描述了群集的用途。



```yml
cluster.name: logging-prod
```

确保不要在不同的环境中重用相同的群集名称，否则最终会导致节点加入错误的群集。

### 三、节点名称(`node.name`)

默认情况下，Elasticsearch将使用随机生成的UUID的前七个字符作为节点ID。 请注意，节点ID是持久的，并且在节点重新启动时不会更改，因此默认节点名称也不会更改。

值得配置一个更有意义的名称，它还具有在重新启动节点后保持不变的优点：



```yml
node.name: prod-data-2
```

node.name也可以设置为服务器的HOSTNAME，如下所示：网络主机



```yml
node.name: ${HOSTNAME}
```

## 四、节点名称(`network.host`)



```yml
network.host: 192.168.1.10
```

## 五、发现设置(`Discovery settings`)

Elasticsearch使用名为“Zen Discovery”的自定义发现实现进行节点到节点的群集和主选举。 在投入生产之前，应该配置两个重要的发现设置。

**第一个配置：**



```yaml
discovery.zen.ping.unicast.hosts:
```

开箱即用，没有任何网络配置，Elasticsearch将绑定到可用的环回地址，并将扫描端口9300到9305以尝试连接到在同一服务器上运行的其他节点。 这提供了自动群集体验，无需进行任何配置。

当需要在其他服务器上形成具有节点的群集时，您必须提供群集中可能是实时且可联系的其他节点的种子列表。 这可以指定如下：



```yaml
discovery.zen.ping.unicast.hosts:
   - 192.168.1.10:9300  ## 本机绑定的ip和端口，端口：节点和节点之间通信的端口
   - 192.168.1.11       ## 如果未指定端口，端口将默认为transport.profiles.default.port并回退到transport.tcp.port。
   - seeds.mydomain.com ## A hostname that resolves to multiple IP addresses will try all resolved addresses. 
```

**第二个配置：**



```yaml
discovery.zen.minimum_master_nodes
```

如果没有此设置，遭受网络故障的群集可能会将群集拆分为两个独立的群集 - 脑裂 - 这将导致数据丢失。为避免脑裂，应将此设置设置为符合条件的主节点的法定数量：



```yaml
(master_eligible_nodes / 2) + 1
```

换句话说，如果有三个符合主节点的节点，则应设置最小主节点：



```yaml
discovery.zen.minimum_master_nodes: 2
```

## 六、堆大小(`heap size`)



```sh
# Set the minimum and maximum heap size to 2 GB. 
ES_JAVA_OPTS="-Xms2g -Xmx2g" ./bin/elasticsearch 
# Set the minimum and maximum heap size to 4000 MB. 
ES_JAVA_OPTS="-Xms4000m -Xmx4000m" ./bin/elasticsearch 
```

## 七、堆转储路径(`JVM heap dump path`)

默认情况下，Elasticsearch将JVM配置为将内存异常转储到默认数据目录（这是RPM和Debian软件包发行版的 /var / lib / elasticsearch，以及Elasticsearch安装根目录下的数据目录） tar和zip归档文件分发）。 如果此路径不适合接收堆转储，则应修改jvm.options中的条目-XX：HeapDumpPath = .... 如果指定目录，JVM将根据正在运行的实例的PID为堆转储生成文件名。 如果指定固定文件名而不是目录，则当JVM需要在内存不足异常上执行堆转储时，该文件必须不存在，否则堆转储将失败。

## 八、GC记录(`GC logging`)

默认情况下，Elasticsearch启用GC日志。 这些在jvm.options中配置，默认为与Elasticsearch日志相同的默认位置。 默认配置每64 MB轮换一次日志，最多可占用2 GB的磁盘空间。

## 九、临时目录(`Temp directory`)

默认情况下，Elasticsearch使用启动脚本在系统临时目录下创建的专用临时目录。 在某些Linux发行版上，系统实用程序将清除/ tmp中的文件和目录（如果它们最近未被访问过）。如果长时间不使用需要临时目录的功能，则可能导致在Elasticsearch运行时删除专用临时目录。如果随后使用需要临时目录的功能，则会导致问题。

如果使用.deb或.rpm软件包安装Elasticsearch并在systemd下运行，则Elasticsearch使用的专用临时目录将从定期清理中排除。

但是，如果您打算在Linux上运行.tar.gz发行版很长一段时间，那么您应该考虑为Elasticsearch创建一个专用的临时目录，该目录不在将从中清除旧文件和目录的路径下。此目录应具有权限集，以便只有运行Elasticsearch的用户才能访问它。然后在启动Elasticsearch之前将$ES_TMPDIR环境变量设置为指向它。