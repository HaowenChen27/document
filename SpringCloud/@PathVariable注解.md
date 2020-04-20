# spring中@PathVariable注解的作用

@PathVariable绑定URI模板变量值是用来获得请求url中的动态参数的

![img](https://img-blog.csdnimg.cn/20190115221247360.png)

@PathVariable中有三个变量

name：要绑定到的路径变量的名称。

value：经过简单的测试个人觉得与name相同。

required：是否必须存在，默认值是True，如果不存在的话会抛出异常，为false时会忽略。

如果方法的参数和URL的模板一样时可以简单的写为

![img](https://img-blog.csdnimg.cn/20190115221159246.png)

多个变量时在后面接着写就好了。例如：

![img](https://img-blog.csdnimg.cn/20190115221521727.png)

当方法的参数和URL的模板一样时也是可以简写的。

值得注意的是，当我们有个时候想要规定URL变量的格式时，我们可以用正则来进行控制，格式是：

｛变量名:正则表达式｝

例如：

/rolePermission/{id}后面这个id只能包含数字那么我们可以这样写：

![img](https://img-blog.csdnimg.cn/20190115222714752.png)