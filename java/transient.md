# java中的关键字transient是什么意思

百度解释：

> 使用transient修饰符来标识一个成员变量在序列化子系统中应被忽略。

> 变量修饰符，如果用transient声明一个[实例变量](https://www.baidu.com/s?wd=实例变量&tn=SE_PcZhidaonwhc_ngpagmjz&rsv_dl=gh_pc_zhidao)，当对象存储时，它的值不需要维持，即不持久化。也就是说不会为这个变量[分配内存](https://www.baidu.com/s?wd=分配内存&tn=SE_PcZhidaonwhc_ngpagmjz&rsv_dl=gh_pc_zhidao)来保存



### 一、概要介绍

Java中的transient关键字，transient是短暂的意思。对于transient 修饰的成员变量，在类实例的序列化处理过程中会被忽略。 因此，transient变量不会贯穿对象的序列化和反序列化，生命周期仅存于调用者的内存中而不会写到磁盘里持久化。

- 序列化
   		Java中对象的序列化指的是将对象转换成以字节序列的形式来表示，这些字节序列包含了对象的数据和信息，一个序列化后的对象可以被写到数据库或文件中，也可用于网络传输，一般当我们使用缓存cache（内存空间不够有可能会本地存储到硬盘）或远程调用rpc（网络传输）的时候，经常需要让我们的实体类实现Serializable接口，目的就是为了让其可序列化。当然，序列化后的最终目的是为了反序列化，恢复成原先的Java对象，所以序列化后的字节序列都是可以恢复成Java对象的，这个过程就是反序列化。

-  为什么要用transient关键字？

  ​		当持久化对象时，可能有一个特殊的对象数据成员（如用户的密码，银行卡号等），我们不想用serialization机制来保存它。为了在一个特定对象的一个域上关闭serialization，可以在这个域前加上关键字transient。

- transient的作用

    	  transient是Java语言的关键字，用来表示一个域不是该对象串行化的一部分。当一个对象被串行化的时候，transient型变量的值不包括在串行化的表示中，然而非transient型的变量是被包括进去的



![img](https://img-blog.csdn.net/20180614221139335?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTI3MjM2NzM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)



![img](https://img-blog.csdn.net/20180614221158207?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTI3MjM2NzM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/20180614221217414?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTI3MjM2NzM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

### 二、transient使用总结
（1）一旦变量被transient修饰，变量将不再是对象持久化的一部分，该变量内容在序列化后无法获得 
       访问。
（2）transient关键字只能修饰变量，而不能修饰方法和类。注意，本地变量是不能被transient关键字
       修饰的。变量如果是用户自定义类变量，则该类需要实现Serializable接口。
（3）被transient关键字修饰的变量不再能被序列化，一个静态变量不管是否被transient修饰，均不能
       被序列化(如果反序列化后类中static型变量还有值，则值为当前JVM中对应static变量的值)

![img](https://img-blog.csdn.net/20180614221348936?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTI3MjM2NzM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

![img](https://img-blog.csdn.net/20180614221407782?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTI3MjM2NzM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

### 三、使用场景

​    1、类中的字段值可以根据其它字段推导出来，如一个长方形类有三个属性长度、宽度、面积，面积不需要序列化

​    2、一些安全性的信息，一般情况下是不能离开JVM的。

​    3、如果类中使用了Logger实例，那么Logger实例也是不需要序列化的



原文链接：https://blog.csdn.net/u012723673/article/details/80699029



### 四、什么是序列化和反序列化 什么是串行化和并行化？

当两个进程在进行远程通信时，彼此可以发送各种类型的数据。无论是何种类型的数据，都会以二进制序列的形式在网络上传送。发送方需要把这个对象转换为字节序列，才能在网络上传送；接收方则需要把字节序列再恢复为对象。

  1、把对象转换为字节序列的过程称为对象的序列化。

  2、把字节序列恢复为对象的过程称为对象的反序列化。

   **序列化**就是一种用来处理对象流的机制，所谓对象流也就是将对象的内容进行流化。可以对流化后的对象进行读写操作，也可将流化后的对象传输于网络之间。序列化是为了解决在对对象流进行读写操作时所引发的问题。

​    **序列化的实现**：将需要被序列化的类实现Serializable接口，该接口没有需要实现的方法，implements Serializable只是为了标注该对象是可被序列化的，然后使用一个输出流(如：FileOutputStream)来构造一个ObjectOutputStream(对象流)对象，接着，使用ObjectOutputStream对象的writeObject(Object obj)方法就可以将参数为obj的对象写出(即保存其状态)，要恢复的话则用输入流。

<font color=red>**串行化和并行化**</font>

   **串行化**也叫做序列化,就是把存在于内存的对象数据转化成可以保存成硬盘文件的形式去存储;
   **并行化**也叫反序列化,就是把序列化后的硬盘文件加载到内存,重新变成对象数据.
   也就是把内存中对象数据变成硬盘文件.

<mark> 被static修饰的变量应该也是不会被序列化的，因为只有堆内存会被序列化.所以静态变量会天生不会被序列化。</mark>

```java
public class Test {
    
    public static void main(String args[]) throws FileNotFoundException, IOException, ClassNotFoundException {
        User user = new User();
        user.setAge("22");
        user.setName("小明");
        user.setPassword("admin");
        System.out.println(user.getAge()+"\t"+user.getName()+"\t"+user.getPassword());
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("e:/user.txt"));
        user.setAge("33"); //在序列化后在对static修饰的变量进行一次赋值操作
        oos.writeObject(user);
        oos.flush();
        oos.close();
        
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("e:/user.txt"));
        User users = (User) ois.readObject();
        
        System.out.println(users.getAge()+"\t"+users.getName()+"\t"+users.getPassword());
        
    }
}
```



可以看到在序列化前 static 修饰的变量赋值为22，而反序列化后读取的这个变量值为33，由此可以看出 static 修饰的变量本身是不会被序列化的

我们读取的值是当前jvm中的方法区对应此变量的值，所以最后输出的值为我们对static 变量后赋的值

