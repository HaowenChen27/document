### ELK使用KQL查询日志

日志通过Kibana这个可视化工具来进行查询

当我们需要查询符合某个条件数据时，就需要用到KQL(Kibana Query Language)

[官方文档](https://www.elastic.co/guide/en/kibana/current/kuery-query.html#kuery-query)

#### 筛选语法

将所有涉及到的语法铺展开来，首先准备好官网文档中的数据如下：

```json
{
  "grocery_name": "Elastic Eats",
  "items": [
    {
      "name": "banana",
      "stock": "12",
      "category": "fruit"
    },
    {
      "name": "peach",
      "stock": "10",
      "category": "fruit"
    },
   {
      "name": "peach test",
      "stock": "10",
      "category": "fruit"
    },
    {
      "name": "carrot",
      "stock": "9",
      "category": "vegetable"
    },
    {
      "name": "broccoli",
      "stock": "5",
      "category": "vegetable"
    }
  ]
}
```



#### 简单查询

简单查询就是 关键字匹配、字符串包含等，比如说如下语句会找出 name 字段是 banana 的所有数据：

```json
name:banana
```



但是如果name包含peach和peach test 下面两个语句查出来会是两个结果

```json
name:peach test
```

上面的查询会把 name为peach 和name为 peach test的都给查出来



精确查询(加上引号) 不进行分词

```json
name: "peach test"
```



#### 条件运算符

条件运算符就是 > >= < <=，在 KQL 里边都支持，使用也很简单，比如如下语句表示 age 字段大于等于 10

```json
age >= 10
```



#### 逻辑运算符

查询语言自然少不了逻辑运算符 与或非，在 KQL 中代表了 and or not

and 的用法：

```json
age >= 10 and age < 100
```

上述语句表示查询出 age 在 10 到 100 的左开右闭区间中的所有数据



or的用法

```json
name: "Jeff" or name: "Kitty"
```



上述语句表示筛选出 name 包含 `Jeff` 或者 `Kitty` 关键字的所有数据

not 的用法：

```json
not age >= 10
```





and 的优先级比 or 的高

```json
age < 100 or name: wang and age >= 10
```

and 优先级高会先结合，所以意思是 满足 name 是wang age >= 10 或者 age < 100。

当然也可以通过小括号来改变优先级，比如：

```json
(age < 100 or name: wang) and age >= 10
```

意思是 age >=10 并且这条数据的 name是wang或者age < 100



#### 同一字段运算符简写

可以用括号将多个逻辑运算符和条件合并到一起

```json
age = 10 or age = 100
# 等价于
age: ( 10 or 100)
```



#### 通配符

通配符可以用于查找出存在某个key的数据

```json
name: *
```

表示查找出所有带 name 字段的数据

```json
system: win*
```

可以匹配到 system: win7，system: win10 等。



#### 字段嵌套查询

首先准备一个多层的数据，比如下面的这几条数据。

```json
{
  "level1": [
    {
      "level2": [
        {
          "prop1": "foo",
          "prop2": "bar"
        },
        {
          "prop1": "baz",
          "prop2": "qux"
        }
      ]
    }
  ]
}
```

比如想筛选 level1.level2.prop1 是 `foo` 或者是 `baz`的，可以这样写：

```json
level1.level2 { prop1: "foo" or prop1: "baz" }
```

