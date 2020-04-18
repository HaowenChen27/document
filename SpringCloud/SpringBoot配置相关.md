### postman post请求时401
springboot 1.5.X 以上默认开通了安全认证，所以需要在配置文件application.properties添加以下配置：management.security.enabled=false

异常:This application has no explicit mapping for /error, so you are seeing this as a fallback.
出现这个异常说明了跳转页面的url无对应的值.

    原因1:
    Application启动类的位置不对.要将Application类放在最外侧,即包含所有子包 
    原因:spring-boot会自动加载启动类所在包下及其子包下的所有组件.
    
    原因2:
    在springboot的配置文件:application.yml或application.properties中关于视图解析器的配置问题: 
    当pom文件下的spring-boot-starter-paren版本高时使用: 
    spring.mvc.view.prefix/spring.mvc.view.suffix 
    当pom文件下的spring-boot-starter-paren版本低时使用: 
    spring.view.prefix/spring.view.suffix
    
    原因3:
    控制器的URL路径书写问题 
    @RequestMapping(“xxxxxxxxxxxxxx”) 
    实际访问的路径与”xxx”不符合.