## RestTemplate实践
　　在微服务都是以HTTP接口的形式暴露自身服务的，因此在调用远程服务时就必须使用HTTP客户端。我们可以使用JDK原生的URLConnection、Apache的Http Client、Netty的异步HTTP Client, Spring的RestTemplate。但是，用起来最方便、最优雅的还是要属Feign了。这里介绍的是RestTemplate。
　　
#### 什么是RestTemplate？

RestTemplate是Spring提供的用于访问Rest服务的客户端