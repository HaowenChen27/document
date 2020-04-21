## 关于elasticsearch的EsRejectedExecutionException

### 起因

------

呃，中午准备吃饭的时候，突然发现产品的搜索功能挂了，没办法，只能放下手里的碗，抱起前面的砖准备开始排查问题。

### 排查过程

------

连接线上服务器，查询ERROR日志，果然一堆ES相关报错，初步怀疑是公司内部DBIndex服务（内部基于dubbo封装的提供者，用于app端查询的服务）挂了。

连接DBIndex服务所在机器，查询服务进程状态，发现没什么问题，继续查看日志，发现一堆类似的错误。如图：

![img](https://upload-images.jianshu.io/upload_images/10315528-5c68769b77c2ca0b.png)



看来可以确定是elasticsearch自身服务的问题了，但是具体是什么问题呢，一顿度娘，谷哥的操作之后，发现并没有一个比较贴切的回答。

暂时没什么头绪，就继续查看错误日志，一直翻到报错的起始点，发现了一些不一样的异常信息。如图：



![img](https://upload-images.jianshu.io/upload_images/10315528-82f2308eafb89762.png)

这个错就比明显了，大概可以发现跟ES自身的线程池有关系。

1. 查阅相关资料后了解到，使用Elasticsearch的时候，在并发查询量大的情况下，访问流量超过了集群中单个Elasticsearch实例的处理能力，Elasticsearch服务端会触发保护性的机制，拒绝执行新的访问，并且抛出EsRejectedExecutionException异常。
    这个保护机制与异常触发是由Elasticsearch API实现中的thread pool与配套的queue决定的。
    在示例中，Elasticsearch为index操作分配的线程池，pool size=7，thread = 7, queue capacity=1000，当7个线程处理不过来，并且队列中缓冲的tasks超过1000个，那么新的task就会被简单的丢弃掉，并且抛出EsRejectedExecutionException异常。

### 总结

------

最后靠着重启大法解决了这次的问题。。。我们可以打开 es/configs/elasticsearch.yml 配置一些线程池的参数，但估计治标不治本，最好的方法还是加机器跑集群吧，单实例始终是单实例。。。