## SpringCloud组件关系 ##

### Eureka
所有的组件服务一般都需要向注册中心（Eureka）进行服务注册；微服务的配置一般都统一由配置中心(config)进行管理。

### Zuul
外部或者内部的非Spring Cloud项目都统一通过API网关（Zuul）来访问内部服务

### Ribbon
可用服务列表经由Ribbon进行均衡负载后，分发到后端的具体服务器

### config server
微服务的统一配置由config server管理

### Feign
微服务之间通过Feign进行通信处理业务

### Hystrix
负责处理服务调用超时熔断

### Turbine
Turbine监控服务间的调用和熔断相关指标

所有的组件服务一般都需要向注册中心（Eureka）进行服务注册；微服务的配置一般都统一由配置中心(config)进行管理。

常见的服务调用流程：

    1、外部或者内部的非Spring Cloud项目都统一通过API网关（Zuul）来访问内部服务
    2、网关(Zuul)接收到请求后，从注册中心（Eureka）获取可用服务；
    3、可用服务列表经由Ribbon进行均衡负载后，分发到后端的具体服务器；
    4、微服务之间通过Feign进行通信处理业务、微服务的统一配置由config server管理；
    5、Hystrix负责处理服务调用超时熔断；
    6、Turbine监控服务间的调用和熔断相关指标。
