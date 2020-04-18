**服务器配置，三台centos虚拟机，ip列表如下：**
 `192.168.52.131`
 `192.168.52.132`
 `192.168.52.133`
 安装es之前先安装jdk，jdk的安装略去。
 es的版本：elasticsearch-6.5.4

**三台服务器es安装路径信息**



```json
[root@master app]# pwd
/usr/local/app
[root@master app]# ls
elasticsearch-6.5.4  elasticsearch-6.5.4.zip  jdk1.8.0_191
```

三台服务器配置如下：

192.168.52.131配置信息：



```json
[root@master elasticsearch-6.5.4]# vim config/elasticsearch.yml
```



```yml
#配置es的集群名称，默认是elasticsearch，
#es会自动发现在同一网段下的es，
# 如果在同一网段下有多个集群，就可以用这个属性来区分不同的集群。
cluster.name: cell
#
# ------------------------------------ Node ------------------------------------
node.name: node_01
node.master: true
node.data: true
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
#
path.data: /var/data/elasticsearch
#
# Path to log files:
#
path.logs: /var/log/elasticsearch
network.host: 0.0.0.0
http.port: 9200
transport.tcp.port: 9300

discovery.zen.ping.unicast.hosts: ["192.168.52.131:9300","192.168.52.132:9300", "192.168.52.133:9300"]

discovery.zen.minimum_master_nodes: 2 
```

192.168.52.132配置信息：

只需要修改node.name即可：



```yml
node.name: node_02
```

192.168.52.133配置信息：



```css
node.name: node_03
```

分别启动三台服务器上的es，（这里不能使用root用户启动，请创建个普通用户，如何创建，略去）

查看集群信息：

浏览器访问：http://192.168.52.131:9200/_cat/nodes?v

显示结果如下：



```js
ip             heap.percent ram.percent cpu load_1m load_5m load_15m node.role master name
192.168.52.132           13          94   6    0.11    0.26     0.23 mdi       -      node_02
192.168.52.133           14          93   4    0.10    0.17     0.14 mdi       *      node_03
192.168.52.131           13          92  15    0.22    0.50     0.28 mdi       -      node_01
```

到此搭建成功了。。