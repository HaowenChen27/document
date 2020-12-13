一、MySQL架构介绍

关系型数据库

被Oracle公司收购

遵循GPL协议（开源协议），可以修改源码开发自己的MySQL系统

支持大型数据库，支持5000万条记录的数据库仓库

单表不要超过500万

32位系统表文件最大可支持4GB，64位系统最大支持8TB

数据库建模优化：设计数据库表

二、字符集问题

mysql中文乱码 字符集问题

vim /etc/my.cnf

character_set_server=utf8

重启

已经创建的库和表需要重新修改字符集

```sql
### 修改库的字符集
alter database mydb character set 'utf8';
### 修改表字符集
alter table mytbl convert to character set 'utf8';
```

5.7 没有设置字符集直接不能插入

之前的版本已经插入的乱码数据需要重新update



三、用户权限

select * from user\G;解决折行，按列展示 

```sql
create user zhang3 identified by '123456'; 
```

%代表所有ip

修改密码：

```sql
update mysql.user set password=password('123456') where user='li4';
```

flush privileges； #所有通过user表的修改，必须使用该命令才能生效

配置杂项：

sql_mode 

ONLY_FULL_GROUP_BY  (groupby 全覆盖)



四、逻辑架构

  0.  MySQL之外的类似Java程序访问

1. 和连接池接通
2. 访问缓存和缓冲查询（查询不到往下走，否则直接返回给请求方）
3. SQL接口分析sql
4. 解析器复杂sql解析
5. 优化器不影响结果进行优化，生成执行计划
6. 存储引擎按计划分类型执行
7. 返回结果
8. 写入缓存



#### 查看执行周期

开启缓存配置

修改配置文件 /etc/my.cnf

新增一行： query_cache_type = 1

show variables like '%profiling%';

set profiling = 1;

查询执行过程： 

Show profiles; 得到query_id 用于提供下面的详细查询

查看详细信息：

show profile,cpu,block io for query 2; 

sql真正执行会把sql顺序打乱



五、存储引擎

查看存储引擎：show engines；

外键一般不使用，影响效率，数据迁移就很难受

一般在业务逻辑开发时控制，比较灵活

对比MySQL两个常用的存储引擎：

MyISAM 

+ 是表锁
+ 只缓存索引
+ 不支持外键
+ 不支持事务



InnoDB 

+ 是行锁，只有行锁会发生死锁
+ 不仅缓存索引，还缓存真实数据，对内存要求高
+ 支持外键
+ 支持事务



CSV引擎

将普通的CSV文件当做MySQL表来处理，不支持索引

作为一种数据交换机制非常有用



联合引擎

远程关联查询 只是简单的关联







