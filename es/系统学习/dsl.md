**增加一个索引**

```json
PUT /movie_index
```

**删除一个索引**

```json
DELETE /movie_index
```

**新增文档**

```json
##格式
PUT /index/type/id

PUT /movie_index/movie/1
{ "id":1,
  "name":"operation red sea",
  "doubanScore":8.5,
  "actorList":[  
			{"id":1,"name":"zhang yi"},
			{"id":2,"name":"hai qing"},
			{"id":3,"name":"zhang han yu"}
	]
}
```

如果之前没有建过index或者type，es会自动创建



**直接用id查找**

```json
GET movie_index/movie/1
```

**修改之整体替换**

```json
PUT /movie_index/movie/3
{
  "id":"3",
  "name":"incident red sea",
  "doubanScore":"5.0",
  "actorList":[  
		{"id":"1","name":"zhang chen"}
	]
}
```

是用PUT作用是会直接覆盖之前的文本 version + 1

**修改某一个字段**

```json
POST movie_index/movie/3/_update
{ 
  "doc": {
    "doubanScore":"7.0"
  } 
}
```

**删除一个document**

```json
DELETE movie_index/movie/3
```

**搜索type全部数据**

```json
GET movie_index/movie/_search
```

**查询结果分析**

```json
{
  "took": 2,    //耗费时间 毫秒
  "timed_out": false, //是否超时
  "_shards": {
    "total": 5,   //发送给全部5个分片
    "successful": 5,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 3,  //命中3条数据
    "max_score": 1,   //最大评分
    "hits": [  // 结果
      {
        "_index": "movie_index",
        "_type": "movie",
        "_id": 2,
        "_score": 1,
        "_source": {
          "id": "2",
          "name": "operation meigong river",
          "doubanScore": 8.0,
          "actorList": [
            {
              "id": "1",
              "name": "zhang han yu"
            }
          ]
        },
        ........
      ]
      }
```

**按条件查询（全部）**

```json
GET movie_index/movie/_search
{
  "query":{
    "match_all":{}
  }
}
```

**按分词查询**

```json
GET movie_index/movie/_search
{
  "query":{
    "match":{"name":"red"}
  }
}
```

**按分子子属性查询**

```json
GET movie_index/movie/_search
{
  "query":{
    "match":{"actorList.name":"zhang"}
  }
}
```

**match phrase**

```json
GET movie_index/movie/_search
{
    "query":{
      "match_phrase": {"name":"operation red"}
    }
}
```

按短语查询，不再利用分词技术，直接用短语在原始数据中匹配

**fuzzy 查询**

```json
GET movie_index/movie/_search
{
    "query":{
      "fuzzy": {"name":"rad"}
    }
}
```

校正匹配分词，当一个单词无法准确匹配，es通过算法对非常接近的单词给与一定的评分，能够查询出来，但是消耗更多的性能。

**过滤--查询后过滤**

```json
GET movie_index/movie/_search
{
    "query":{
      "match": {"name":"red"}
    },
    "post_filter":{
      "term": {
        "actorList.id": 3
      }
    }
}
```

**过滤--查询前过滤（推荐）**

```json
GET movie_index/movie/_search
{ 
    "query":{
        "bool":{
          "filter":[ {"term": {  "actorList.id": "1"  }},
                     {"term": {  "actorList.id": "3"  }}
           ], 
           "must":{"match":{"name":"red"}}
         }
    }
}
```

**过滤--按范围过滤**

```json
GET movie_index/movie/_search
{
   "query": {
     "bool": {
       "filter": {
         "range": {
            "doubanScore": {"gte": 8}
         }
       }
     }
   }
}
```

关于范围操作符

| gt   | 大于     |
| ---- | -------- |
| lt   | 小于     |
| gte  | 大于等于 |
| lte  | 小于等于 |

**排序**

```json
GET movie_index/movie/_search
{
  "query":{
    "match": {"name":"red sea"}
  }
  , "sort": [
    {
      "doubanScore": {
        "order": "desc"
      }
    }
  ]
}
```

**分页查询**

```json
GET movie_index/movie/_search
{
  "query": { "match_all": {} },
  "from": 0,
  "size": 1
}
```

from = pageNum * size

pageNum从0开始

**指定查询的字段**

```json
GET movie_index/movie/_search
{
  "query": { "match_all": {} },
  "_source": ["name", "doubanScore"]
}
```

**高亮**

```json
GET movie_index/movie/_search
{
    "query":{
      "match": {"name":"red sea"}
    },
    "highlight": {
      "fields": {"name":{} }
    }  
}

自定义标签
GET movie_index/movie/_search
{
    "query":{
      "match": {"name":"red sea"}
    },
    "highlight": {
      "pre_tags": ["<b>"], 
      "post_tags": ["</b>"], 
      "fields": {"name":{} }
    }
}
```

**聚合 （很少用到，性能与关系型数据库差不多）**

取出每个演员共参演了多少部电影

```json
GET movie_index/movie/_search
{ 
  "aggs": {
    "groupby_actor": {
      "terms": {
        "field": "actorList.name.keyword"  
      }
    } 
  }
}
```

每个演员参演电影的平均分是多少，并按评分排序

```json
GET movie_index/movie/_search
{ 
  "aggs": {
    "groupby_actor_id": {
      "terms": {
        "field": "actorList.name.keyword" ,
        "order": {
          "avg_score": "desc"
          }
      },
      "aggs": {
        "avg_score":{
          "avg": {
            "field": "doubanScore" 
          }
        }
       }
    } 
  }
}
```

