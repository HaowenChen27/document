### ES中如何做到过滤相似度低的field？

*方案*：使用minimum_should_match

### 什么是minimum_should_match？

顾名思义：最小匹配度

```json
"minimum_should_match":3
```

官方解释：Indicates a fixed value regardless of the number of optional clauses.

释义：指示一个固定值，而不考虑可选子句的数量。

#### 什么是 optional clauses（翻译为可选的子句）？

对于被analyzer分解出来的每一个term都会构造成一个should的bool query的查询,每个term变成一个term query子句。

例如

```json
"query": "how not to be"
```

解析成：

```json
{
  "bool": {
    "should": [
      { "term": { "body": "how"}},
      { "term": { "body": "not"}},
      { "term": { "body": "to"}},
      { "term": { "body": "be"}}
    ],
    "minimum_should_match": 3
  }
}
```

查询语句被分为四个词组，minimum_should_match为3 即只要存在3个词组满足则符合匹配

注意：minimum_should_match只能紧跟在should的后面，放其他地方会出异常



### 使用百分比

有时候我们并不确定被分词的语句能拆分多少term，所以我们可以使用百分比来进行相似度筛选

```json
{
  "bool": {
    "should": [
      { "term": { "body": "how"}},
      { "term": { "body": "not"}},
      { "term": { "body": "to"}},
      { "term": { "body": "be"}}
    ],
    "minimum_should_match": "75%"
  }
}
```





