**关于mapping**

type可以理解为table，每个字段的数据类型由mapping定义

查看mapping

```json
GET movie_index/_mapping/movie
```

如果没有自定义mapping，系统会自动生成mapping。

系统会根据一条数据的格式来推断出应该的数据格式

+ true/false → boolean

+ 1020  → long

+ 20.1 → double

+ “2018-02-01” → date

+ “hello world” → text +keyword

默认只有text会进行分词，keyword是不会进行分词的字符串

mapping 可以手动定义，但是<font color=red>只能对新加的、没有数据的字段进行定义，一旦有数据就不能修改了</font>

<font color=red>注意：虽然每个Field的数据放在不同的type下,但是同一个名字的Field在一个index下只能有一种mapping定义。</font>



