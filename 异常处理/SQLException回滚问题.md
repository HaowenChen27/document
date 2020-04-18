#### 一，为什么框架中根本没有对Exception的一般子类进行回滚配置，异常发生时，事务都进行了回滚 ，说好的只会对RuntimeException（Unchecked 非受检异常）回滚呢？ 

此时，我们就有必要了解一下，RuntimeException所包含的子类具体有哪些：


​             ![img](https://img-blog.csdnimg.cn/20181108144244164.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMyMzMxMDcz,size_16,color_FFFFFF,t_70)

这时，或许你就明白了 ： **平常代码运行阶段经常遇到的那些异常，其实都是RuntimeException的子类。受检异常（Checked）一般在编译期就被检出，这就给你造成了一个Spring对于所有异常都会发生回滚的误解。**

![img](https://img-blog.csdnimg.cn/20181108144308457.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMyMzMxMDcz,size_16,color_FFFFFF,t_70)

下面给出一些受检CHECKED异常：


​            

#### 二，为什么我在执行方法的时候出现了SQL执行的Exception，默认配置的情况下，事务还是发生了回滚 ？                                               

下结论之前，我们应该仔细查看异常信息：

[Request processing failed; nested exception is org.springframework.dao.DuplicateKeyException:   

```wiki
[Request processing failed; nested exception is org.springframework.dao.DuplicateKeyException:   
### Error updating database.  Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry '1' for key 1  
```

下面我会给出 一个例子：类似于直播软件中，“礼物的购买事务”，其中有三个动作：          

①Mygift数量的增加            ②Customer余额的减少         ③consumption消费明细的增加



```java
int a=consumpDao.insert(s);//插入消费明细  
    int b=customerDao.insert(customer);//此处实际应该update(customer),不然会出现重复主键的异常  
    int d=0;  
    if(mygift==null){//判断礼物类型是否存在，第一次插入，而后更新  
        m.setMySum(s.getGiftSum());  
        d=mygiftDao.insert(m);  
    }else{  
        mygift.setMySum(mygift.getMySum()+s.getGiftSum());  
        d=mygiftDao.update(mygift);  
    }  
    if(a*b*d==1){  
        json.put("result",0);  
        json.put("msg", "购买成功");  
        json.put("data", "");  
    }else{  
        json.put("result",-1);  
        json.put("msg", "购买失败");  
        json.put("data", "");  
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();  
    }  
}  
```



 在程序28行，明确指出：

```java
int a=consumpDao.insert(s);//插入消费明细  
int b=customerDao.insert(customer);//此处实际应该update(customer),不然会出现重复主键的异常  
```

程序运行之前，Consumption消费记录中只有一条数据。                           

程序运行，出现异常，具体如下：

```java
[Request processing failed; nested exception is org.springframework.dao.DuplicateKeyException:   
### Error updating database.  Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry '1' for key 1  
```



对应事务中的三个动作，理论发生：

①Consumption消费明细的增加 执行成功，②Customer余额的减少SQL语句在执行的时候发生异常，③Mygift数量增加 执行成功

**程序运行后，Consumption消费记录并没有出现第二条:**

所以此时，该事务发生了回滚。org.springframework.dao.DuplicateKeyException 应该是RuntimeException的子类

**三，作出结论，是SQLException属于RuntimeException的子类？还是默认配置一般异常也会回滚呢?**                           ① 查看接口文档java.lang.SqlException, 
                        java.lang.Object
                             |____java.lang.Throwable
                                  |____ java.lang.Exception
                                       |____ java.lang.SQLException

可以看出: **java.lang.SqlException,确实是Exception的直接子类，属于CHECKED受检异常，事务是不会因为它发生回滚的！**

② **实际上**，当我们在项目开发中加入了Spring框架以后，SQL异常都被org.springframework重写，正如上面的重复主键的SQL异 常。

**产生原因：**很显然该异常原因属于一般异常，而被Spring捕捉后抛出其他自定义的RuntimeException

**具体可见：**org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator.doTranslate()

我们知道 org.springframework.dao.DuplicateKeyException来自spring-tx-4.0.0.RELEASE.jar
 反编译可见：
       java.lang.Object
           |java.lang.Throwable
                | java.lang.Exception
                      | java.lang.RuntimeException
                           | org.springframework.core.NestedRuntimeException
                                  |org.springframework.dao.DataAccessException
                                          |  org.springframework.dao.NonTransientDataAccessException
                                               |org.springframework.dao.DataIntegrityViolationException
                                                    |org.springframework.dao.DuplicateKeyException
 同样方法可以查得：org.springframework.dao中的异常都是RuntimeException的子类

 得出结论：Spring框架下，所有SQL异常都被org.springframework重写为RuntimeException，事务因此也会发生回滚！


文章来源：https://blog.csdn.net/qq_32331073/article/details/76525372