# ThreadLocal原理分析与使用场景

**什么是ThreadLocal变量**

ThreadLoal 变量，线程局部变量，同一个 ThreadLocal 所包含的对象，在不同的 Thread 中有不同的副本。这里有几点需要注意：

- 因为每个 Thread 内有自己的实例副本，且该副本只能由当前 Thread 使用。这是也是 ThreadLocal 命名的由来。
- 既然每个 Thread 有自己的实例副本，且其它 Thread 不可访问，那就不存在多线程间共享的问题。

ThreadLocal 提供了线程本地的实例。它与普通变量的区别在于，每个使用该变量的线程都会初始化一个完全独立的实例副本。ThreadLocal 变量通常被private static修饰。当一个线程结束时，它所使用的所有 ThreadLocal 相对的实例副本都可被回收。

总的来说，ThreadLocal 适用于每个线程需要自己独立的实例且该实例需要在多个方法中被使用，也即变量在线程间隔离而在方法或类间共享的场景。



**ThreadLocal实现原理**

首先 ThreadLocal 是一个泛型类，保证可以接受任何类型的对象。

因为一个线程内可以存在多个 ThreadLocal 对象，所以其实是 ThreadLocal 内部维护了一个 Map ，这个 Map 不是直接使用的 HashMap ，而是 ThreadLocal 实现的一个叫做 ThreadLocalMap 的静态内部类。而我们使用的 get()、set() 方法其实都是调用了这个ThreadLocalMap类对应的 get()、set() 方法。例如下面的 set 方法：

```java
public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }


public T get() {   
        Thread t = Thread.currentThread();   
        ThreadLocalMap map = getMap(t);   
        if (map != null)   
            return (T)map.get(this);   
  
        // Maps are constructed lazily.  if the map for this thread   
        // doesn't exist, create it, with this ThreadLocal and its   
        // initial value as its only entry.   
        T value = initialValue();   
        createMap(t, value);   
        return value;   
    }
```

createMap方法：

```java
    void createMap(Thread t, T firstValue) {   
        t.threadLocals = new ThreadLocalMap(this, firstValue);   
    } 
```

ThreadLocalMap是个静态的内部类：

```java
    static class ThreadLocalMap {   
    ........   
    }  
```

最终的变量是放在了当前线程的 `ThreadLocalMap` 中，并不是存在 ThreadLocal 上，ThreadLocal 可以理解为只是ThreadLocalMap的封装，传递了变量值。

**内存泄漏问题**

实际上 ThreadLocalMap 中使用的 key 为 ThreadLocal 的弱引用，弱引用的特点是，如果这个对象只存在弱引用，那么在下一次垃圾回收的时候必然会被清理掉。

所以如果 ThreadLocal 没有被外部强引用的情况下，在垃圾回收的时候会被清理掉的，这样一来 ThreadLocalMap中使用这个 ThreadLocal 的 key 也会被清理掉。但是，value 是强引用，不会被清理，这样一来就会出现 key 为 null 的 value。

ThreadLocalMap实现中已经考虑了这种情况，在调用 set()、get()、remove() 方法的时候，会清理掉 key 为 null 的记录。如果说会出现内存泄漏，那只有在出现了 key 为 null 的记录后，没有手动调用 remove() 方法，并且之后也不再调用 get()、set()、remove() 方法的情况下。



**使用场景**

如上文所述，ThreadLocal 适用于如下两种场景

- 每个线程需要有自己单独的实例
- 实例需要在多个方法中共享，但不希望被多线程共享



#### ThreadLocal的用法

（1）方法摘要

| 作用域    | 类型 | 方法           | 描述                                             |
| :-------- | :--- | :------------- | :----------------------------------------------- |
| public    | T    | get()          | 返回此线程局部变量的当前线程副本中的值           |
| protected | T    | initialValue() | 返回此线程局部变量的当前线程的“初始值”           |
| public    | void | remove()       | 移除此线程局部变量当前线程的值                   |
| public    | void | set(T value)   | 将此线程局部变量的当前线程副本中的值设置为指定值 |

**注意事项**：
<mark>initialValue()</mark> 这个方法是为了让子类覆盖设计的，默认缺省null。如果get()后又remove()则可能会在调用一下此方法。
<mark>remove()</mark> 移除此线程局部变量当前线程的值。如果此线程局部变量随后被当前线程 读取，且这期间当前线程没有 设置其值，则将调用其 initialValue() 方法重新初始化其值。这将导致在当前线程多次调用 initialValue 方法。



（2）常规用法

在开始之前贴出一个公共的线程测试类

```java
public class TaskThread<T> extends Thread{

    private T t;

    public TaskThread(String threadName,T t) {
        this.setName(threadName);
        this.t = t;
    }

    @Override
    public void run() {
        for (int i = 0; i < 2; i++) {

            try {
                Class[] argsClass = new Class[0];
                Method method = t.getClass().getMethod("getUniqueId",argsClass);
                int value = (int) method.invoke(t);
                System.out.println("thread[" + Thread.currentThread().getName() + "] --> uniqueId["+value+ "]");

            } catch (NoSuchMethodException e) {
                // TODO 暂不处理
                continue;

            } catch (IllegalAccessException e) {
                // TODO 暂不处理
                continue;

            } catch (InvocationTargetException e) {
                // TODO 暂不处理
                continue;

            }


        }
    }

}
```

例1：为每个线程生成一个唯一的局部标识

```java
public class UniqueThreadIdGenerator {

    // 原子整型
    private static final AtomicInteger uniqueId = new AtomicInteger(0);

    // 线程局部整型变量
    private static final ThreadLocal <Integer> uniqueNum =
            new ThreadLocal <Integer> () {
                @Override 
      					protected Integer initialValue() {
                    return uniqueId.getAndIncrement();
                }
            };

    //变量值
    public static int getUniqueId() {
        return uniqueId.get();
    }

    public static void main(String[] args) {
        UniqueThreadIdGenerator uniqueThreadId = new UniqueThreadIdGenerator();
        // 为每个线程生成一个唯一的局部标识
        TaskThread t1 = new TaskThread<UniqueThreadIdGenerator>("custom-thread-1", uniqueThreadId);
        TaskThread t2 = new TaskThread<UniqueThreadIdGenerator>("custom-thread-2", uniqueThreadId);
        TaskThread t3 = new TaskThread<UniqueThreadIdGenerator>("custom-thread-3", uniqueThreadId);
        t1.start();
        t2.start();
        t3.start();
    }

}
```

运行结果：

```
//每个线程的局部变量都是唯一的
thread[custom-thread-2] --> uniqueId[0]
thread[custom-thread-2] --> uniqueId[0]
thread[custom-thread-1] --> uniqueId[0]
thread[custom-thread-1] --> uniqueId[0]
thread[custom-thread-3] --> uniqueId[0]
thread[custom-thread-3] --> uniqueId[0]
```

例2：为每个线程创建一个局部唯一的序列

```java
public class UniqueSequenceGenerator {

    // 线程局部整型变量
    private static final ThreadLocal <Integer> uniqueNum =
            new ThreadLocal <Integer> () {
                @Override 
      					protected Integer initialValue() {
                    return 0;
                }
            };

    //变量值
    public static int getUniqueId() {
        uniqueNum.set(uniqueNum.get() + 1);
        return uniqueNum.get();
    }

    public static void main(String[] args) {
        UniqueSequenceGenerator uniqueThreadId = new UniqueSequenceGenerator();
        // 为每个线程生成内部唯一的序列号
        TaskThread t1 = new TaskThread<UniqueSequenceGenerator>("custom-thread-1", uniqueThreadId);
        TaskThread t2 = new TaskThread<UniqueSequenceGenerator>("custom-thread-2", uniqueThreadId);
        TaskThread t3 = new TaskThread<UniqueSequenceGenerator>("custom-thread-3", uniqueThreadId);
        t1.start();
        t2.start();
        t3.start();
    }

}
```

运行结果：

```
thread[custom-thread-2] --> uniqueId[1]
thread[custom-thread-2] --> uniqueId[2]
thread[custom-thread-1] --> uniqueId[1]
thread[custom-thread-1] --> uniqueId[2]
thread[custom-thread-3] --> uniqueId[1]
thread[custom-thread-3] --> uniqueId[2]
```



#### ThreadLocal的原理

（1）源码解析

源码实现片段：set

```java
/** 
    * Sets the current thread's copy of this thread-local variable 
    * to the specified value.  Most subclasses will have no need to 
    * override this method, relying solely on the {@link #initialValue} 
    * method to set the values of thread-locals. 
    * 
    * @param value the value to be stored in the current thread's copy of 
    *        this thread-local. 
    */  
   public void set(T value) {  
       Thread t = Thread.currentThread();  
       ThreadLocalMap map = getMap(t);  
       if (map != null)  
           map.set(this, value);  
       else  
           createMap(t, value);  
   } 
```

> 在这个方法内部我们看到，首先通过getMap(Thread t)方法获取一个和当前线程相关的ThreadLocalMap，然后将变量的值设置到这个ThreadLocalMap对象中，当然如果获取到的ThreadLocalMap对象为空，就通过createMap方法创建。
>
> 线程隔离的秘密，就在于ThreadLocalMap这个类。ThreadLocalMap是ThreadLocal类的一个静态内部类，它实现了键值对的设置和获取（对比Map对象来理解），每个线程中都有一个独立的ThreadLocalMap副本，它所存储的值，只能被当前线程读取和修改。ThreadLocal类通过操作每一个线程特有的ThreadLocalMap副本，从而实现了变量访问在不同线程中的隔离。因为每个线程的变量都是自己特有的，完全不会有并发错误。还有一点就是，ThreadLocalMap存储的键值对中的键是this对象指向的ThreadLocal对象，而值就是你所设置的对象了。 这个就是实现原理

源码实现片段：getMap、createMap

```java
/** 
 * Get the map associated with a ThreadLocal. Overridden in 
 * InheritableThreadLocal. 
 * 
 * @param  t the current thread 
 * @return the map 
 */  
ThreadLocalMap getMap(Thread t) {  
    return t.threadLocals;  
}  
  
/** 
 * Create the map associated with a ThreadLocal. Overridden in 
 * InheritableThreadLocal. 
 * 
 * @param t the current thread 
 * @param firstValue value for the initial entry of the map 
 * @param map the map to store. 
 */  
void createMap(Thread t, T firstValue) {  
    t.threadLocals = new ThreadLocalMap(this, firstValue);  
}  
```

源码实现片段：get

```java
/** 
 * Returns the value in the current thread's copy of this 
 * thread-local variable.  If the variable has no value for the 
 * current thread, it is first initialized to the value returned 
 * by an invocation of the {@link #initialValue} method. 
 * 
 * @return the current thread's value of this thread-local 
 */  
public T get() {  
    Thread t = Thread.currentThread();  
    ThreadLocalMap map = getMap(t);  
    if (map != null) {  
        ThreadLocalMap.Entry e = map.getEntry(this);  
        if (e != null)  
            return (T)e.value;  
    }  
    return setInitialValue();  
}  
```

源码实现片段：setInitialValue

```java
/** 
    * Variant of set() to establish initialValue. Used instead 
    * of set() in case user has overridden the set() method. 
    * 
    * @return the initial value 
    */  
   private T setInitialValue() {  
       T value = initialValue();  
       Thread t = Thread.currentThread();  
       ThreadLocalMap map = getMap(t);  
       if (map != null)  
           map.set(this, value);  
       else  
           createMap(t, value);  
       return value;  
   }  
   
   //获取和当前线程绑定的值时，ThreadLocalMap对象是以this指向的ThreadLocal对象为键
   //进行查找的，这当然和前面set()方法的代码是相呼应的。进一步地，我们可以创建不同的
   //ThreadLocal实例来实现多个变量在不同线程间的访问隔离，为什么可以这么做？因为不
   //同的ThreadLocal对象作为不同键，当然也可以在线程的ThreadLocalMap对象中设置不同
   //的值了。通过ThreadLocal对象，在多线程中共享一个值和多个值的区别，就像你在一个
   //HashMap对象中存储一个键值对和多个键值对一样，仅此而已。
```

#### ThreadLocal实际用途

例1：在数据库管理中的连接管理类是下面这样的：（摘自网上）

```java
public class ConnectionManager {
    private static Connection connect = null;

    public static Connection getConnection() {
        if(connect == null){
            connect = DriverManager.getConnection();
        }
        return connect;
    }

    ...

} 
```

在单线程的情况下这样写并没有问题，但如果在多线程情况下回出现线程安全的问题。你可能会说用同步关键字或锁来保障线程安全，这样做当然是可行的，但考虑到性能的问题所以这样子做并是很优雅。
下面是改造后的代码：

```java
public class ConnectionManager {

    private static ThreadLocal<Connection> connThreadLocal = new ThreadLocal<Connection>();

    public static Connection getConnection() {
        if(connThreadLocal.get() != null)
            return connThreadLocal.get();
        
        //获取一个连接并设置到当前线程变量中
        Connection conn = getConnection();
        connThreadLocal.set(conn);
        return conn;
    }
    
    ...

}
```

例2：日期格式（摘自网上）

使用这个日期格式类主要作用就是将枚举对象转成Map而map的值则是使用ThreadLocal存储,那么在实际的开发中可以在同一线程中的不同方法中使用日期格式而无需在创建日期格式的实例。

```java
public class DateFormatFactory {

    public enum DatePattern {

        TimePattern("yyyy-MM-dd HH:mm:ss"),
        DatePattern("yyyy-MM-dd");

        public String pattern;

        private DatePattern(String pattern) {
            this.pattern = pattern;
        }
    }

    private static final Map<DatePattern, ThreadLocal<DateFormat>> pattern2ThreadLocal;

    static {
        DatePattern[] patterns = DatePattern.values();
        int len = patterns.length;
        pattern2ThreadLocal = new HashMap<DatePattern, ThreadLocal<DateFormat>>(len);

        for (int i = 0; i < len; i++) {
            DatePattern datePattern = patterns[i];
            final String pattern = datePattern.pattern;

            pattern2ThreadLocal.put(datePattern, new ThreadLocal<DateFormat>() {
                @Override
                protected DateFormat initialValue() {
                    return new SimpleDateFormat(pattern);
                }
            });
        }
    }

    //获取DateFormat
    public static DateFormat getDateFormat(DatePattern pattern) {
        ThreadLocal<DateFormat> threadDateFormat = pattern2ThreadLocal.get(pattern);
        //不需要判断threadDateFormat是否为空
        return threadDateFormat.get();
    }

    public static void main(String[] args) {
         String dateStr = DateFormatFactory.getDateFormat(DatePattern.TimePattern).format(new Date());
         System.out.println(dateStr);
    }


}
```

**注意事项：**
必须回收自定义的ThreadLocal变量，尤其在线程池场景下，线程经常会被复用，如果不清理自定义的 ThreadLocal变量，可能会影响后续业务逻辑和造成内存泄露等问题。尽量在代理中使用try-finally块进行回收。

#### 总结

ThreadLocal是用冗余的方式换时间，而锁机制则是时间换空间，好的设计往往都是在时间、空间以及复杂度之间做权衡，道理是这样但是真正能平衡三者之间的人我姑且称之为“大成者”，愿你我在成长的道路上越走越远。