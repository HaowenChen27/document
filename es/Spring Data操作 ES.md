### 简介
> Spring Data 是持久层通用解决方案，支持关系型数据库 Oracle、MySQL、非关系型数据库NoSQL、Map-Reduce 框架、云基础数据服务 、搜索服务。Spring Data JPA 框架，主要针对的就是 Spring 唯一没有简化到的业务逻辑代码，至此，开发者连仅剩的实现持久层业务逻辑的工作都省了，唯一要做的，就只是声明持久层的接口，其他都交给 Spring Data JPA 来帮你完成！

### 环境搭建

**1.创建一个新的maven的web的moudle**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20190811133318885.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzMyMTA4MzU3,size_16,color_FFFFFF,t_70)



**项目结构如下**

**2.引入springboot和操作es的的相关依赖**

```xml
<!--注意:升级原有项目中springboot版本-->
  <parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.6.RELEASE</version>
  </parent>
  <dependencies>
    <!--springboot web -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!--通过spring data 操作Es-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    </dependency>
    
    <!--springboot 继承test-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
    </dependency>
    <!--引入lombook-->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
    </dependency>

  </dependencies>
```



**3.配置yml文件**

```yml
spring:
  redis:
    port: 8989
  data:
    elasticsearch:
      cluster-nodes: 192.168.94.160:9300
```



## 编程测试一（实现基本的增删改查）：

**1.编写实体类**

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
/*  索引 、 类型 、映射 、 文档
* 声明当前文档对应的索引和类型
* */
@Document(indexName = "ems",type = "emp")
public class Emp {
    @Id
    private String id;
    @Field(type = FieldType.Keyword,analyzer = "ik_max_word")
    private String name;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String intr;
    private String age;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String content;
    @Field(type = FieldType.Boolean)
    private Boolean sex;
}
```



@Document: 代表一个文档记录

加上了@Document注解之后，默认情况下这个实体中所有的属性都会被建立索引、并且分词。

```java
indexName:  用来指定索引名称

type:		用来指定索引类型
```

@Id: 用来将对象中id和ES中_id映射

@Field: 用来指定ES中的字段对应Mapping

```java
type: 用来指定ES中存储类型

analyzer: 用来指定使用哪种分词器
```



**2.编写EmpRepository**

```java
public interface EmpRepository extends ElasticsearchRepository<Emp,String> {
}
```

自定义的接口只需要继承ElasticsearchRepository<Emp,String>接口即可，不需要进行实现，其实现部分已经由SpringData框架为我们做了底层的封装，供我们完基本的**增删改查**功能。



**3.编写测试类**

**索引or更新一条记录**：

```java
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class TestFunction {

    @Autowired
    private EmpRepository empRepository;
    /*
    * 这种方式会根据实体类中的配置自动在ES中创建索引以及映射
    * */
    @Test
    public void testIndex(){
        Emp emp = new Emp("1", "毕鑫蕊", "全世界独一无二的姑娘", "18", "全是姐独一无二的姑娘", true);
        empRepository.save(emp);
    }
}
```



**删除一条记录：**

```java
		@Test
    public void testDelete(){
        Emp emp = new Emp();
        emp.setId("3");
        //delete方法的参数参数要求是一个对象
        empRepository.delete(emp);
    }
```



**查询一个：**

```java
		@Test
    public void testFindOne(){
        Optional<Emp> byId = empRepository.findById("2");
        System.out.println(byId);
    }
```



**查询所有：**

```java
		@Test
    public void testFindAll(){
        Iterable<Emp> all = empRepository.findAll();
        for (Emp emp : all) {
            System.out.println(emp);
        }
    }
```



查询功能中的查询结果返回的内容是对象的形式，一下是查询结果：

```java
Emp(id=2, name=bxr2, intr=全世界独一无二的姑娘2, age=182, content=全是姐独一无二的姑娘, sex=true)
Emp(id=1, name=bxr, intr=全世界独一无二的姑娘, age=18, content=全是姐独一无二的姑娘, sex=true)
```



**排序查询：**

```java
		@Test
    public void testFindAllOrder(){
        Iterable<Emp> age = empRepository.findAll(Sort.by(Sort.Order.desc("age")));
        age.forEach(emp -> System.out.println(emp));
    }
```



## 编程测试二（实现自定的查询）

| Keyword             | Sample                                |                  Elasticsearch Query String                  |
| ------------------- | ------------------------------------- | :----------------------------------------------------------: |
| And                 | findByNameAndPrice                    | {"bool" : {"must" : [ {"field" : {"name" : "?"}}, {"field" : {"price" : "?"}} ]}} |
| Or                  | findByNameOrPrice                     | {"bool" : {"should" : [ {"field" : {"name" : "?"}}, {"field" : {"price" : "?"}} ]}} |
| Is                  | findByName                            |       {"bool" : {"must" : {"field" : {"name" : "?"}}}}       |
| Not                 | findByNameNot                         |     {"bool" : {"must_not" : {"field" : {"name" : "?"}}}}     |
| Between             | findByPriceBetween                    | {"bool" : {"must" : {"range" : {"price" : {"from" : ?,"to" : ?,"include_lower" : true,"include_upper" : true}}}}} |
| LessThanEqual       | findByPriceLessThan                   | {"bool" : {"must" : {"range" : {"price" : {"from" : null,"to" : ?,"include_lower" : true,"include_upper" : true}}}}} |
| GreaterThanEqual    | findByPriceGreaterThan                | {"bool" : {"must" : {"range" : {"price" : {"from" : ?,"to" : null,"include_lower" : true,"include_upper" : true}}}}} |
| Before              | findByPriceBefore                     | {"bool" : {"must" : {"range" : {"price" : {"from" : null,"to" : ?,"include_lower" : true,"include_upper" : true}}}}} |
| After               | findByPriceAfter                      | {"bool" : {"must" : {"range" : {"price" : {"from" : ?,"to" : null,"include_lower" : true,"include_upper" : true}}}}} |
| Like                | findByNameLike                        | {"bool" : {"must" : {"field" : {"name" : {"query" : "*","analyze_wildcard" : true}}}}} |
| StartingWith        | findByNameStartingWith                | {"bool" : {"must" : {"field" : {"name" : {"query" : "?*","analyze_wildcard" : true}}}}} |
| EndingWith          | findByNameEndingWith                  | {"bool" : {"must" : {"field" : {"name" : {"query" : "*?","analyze_wildcard" : true}}}}} |
| Contains/Containing | findByNameContaining                  | {"bool" : {"must" : {"field" : {"name" : {"query" : "**?**","analyze_wildcard" : true}}}}} |
| In                  | findByNameIn` `(Collectionnames)      | {"bool" : {"must" : {"bool" : {"should" : [ {"field" : {"name" : "?"}}, {"field" : {"name" : "?"}} ]}}}} |
| NotIn               | findByNameNotIn` `(Collectionnames)   | {"bool" : {"must_not" : {"bool" : {"should" : {"field" : {"name" : "?"}}}}}} |
| Near                | findByStoreNear                       |                     Not Supported Yet !                      |
| True                | findByAvailableTrue                   |    {"bool" : {"must" : {"field" : {"available" : true}}}}    |
| False               | findByAvailableFalse                  |   {"bool" : {"must" : {"field" : {"available" : false}}}}    |
| OrderBy             | findByAvailable` `TrueOrderByNameDesc | {"sort" : [{ "name" : {"order" : "desc"} }],"bool" : {"must" : {"field" : {"available" : true}}}} |

**2.自定义的基本查询**

```java
public interface EmpRepository extends ElasticsearchRepository<Emp,String> {
    /*
    * 扩展方法 自定方法  根据方法名可以直观的得知方法的作用
    */
    List<Emp> findByContent(String content);  //等值查询

    List<Emp> findByIntrNot(String intr);  //必须不

    List<Emp> findByIntrAndContent(String intr,String content); //并且

    List<Emp> findByIntrOrContent(String intr,String content);  //或者

    List<Emp> findByAgeBetween(Integer start,Integer end);

    List<Emp> findByContentLike(String string);

    List<Emp> findByNameNotIn(List<String> strs);

    List<Emp> findBySexTrueOrderByNameDesc();
}
```

自定义的基本查询**不需要自己去实现**，但要求其命名时严格按照springData提供的关键字规范进行命名

**3.自定义复杂查询**

自定义的复杂实现区别于简单查询有两点：
1.接口无需进行任何继承
2.实现类需要加上@Component



首先自定义Repository接口，在接口中去定义需要实现的复杂方法

```java
package com.baizhi.esrepository;


import com.baizhi.esentity.Emp;
import java.util.List;
public interface CustomRepository  {
    //关键词查询
    public List<Emp> termQuery();
    //关键词查询 并且分页
    public List<Emp> termQueryPage();

    //关键词查询 并且分页 并且排序
    public List<Emp> termQueryPageSort();

    //关键词查询 并且分页 并且排序  指定字段展示
    public List<Emp> termQueryPageSortSource();

    //关键词查询 并且分页 并且排序  指定字段展示  过滤查询
    public List<Emp> termQueryPageSortSourceFilter();

    //关键词查询 并且分页 并且排序  指定字段展示  过滤查询 高亮查询
    public List<Emp> termQueryPageSortSourceFilterHighLighter();
```



其次是书写实现类自己实现相关的方法，这里有一个需要用到一个关键的对象
ElasticsearchTemplate elasticsearchTemplate

```java
package com.baizhi.esrepository;

import com.baizhi.esentity.Emp;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CustomRepositoryImpl implements CustomRepository {
/*
* 构建复杂查询

*
* */
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;
    @Override
    public List<Emp> termQuery() {

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("content", "橘子");
        SearchQuery searchQuery=new NativeSearchQuery(termQueryBuilder);
        //第一个参数为查询条件
        //第二个参数  是当前操作的类型的类对象
        List<Emp> emps = elasticsearchTemplate.queryForList(searchQuery, Emp.class);
        return emps;
    }

    @Override
    public List<Emp> termQueryPage() {
        /*
        * dsl
        *
        * */
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withIndices("ems")
                .withTypes("emp")
                .withQuery(QueryBuilders.matchAllQuery())
                .withPageable(PageRequest.of(1,5))
                .build();

        List<Emp> emps = elasticsearchTemplate.queryForList(build, Emp.class);

        return emps;
    }

    @Override
    public List<Emp> termQueryPageSort() {

        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("age").order(SortOrder.DESC);


        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withIndices("ems")
                .withTypes("emp")
                .withPageable(PageRequest.of(0,10))
                .withSort(sortBuilder)
                .build();
        List<Emp> emps = elasticsearchTemplate.queryForList(build, Emp.class);

        return emps;
    }

    @Override
    public List<Emp> termQueryPageSortSource() {
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("age").order(SortOrder.DESC);


        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withIndices("ems")
                .withTypes("emp")
                .withQuery(QueryBuilders.matchAllQuery())
                .withSort(sortBuilder)
                .withFields("name", "content", "age","intr")
                .build();

        List<Emp> emps = elasticsearchTemplate.queryForList(build, Emp.class);

        return emps;
    }

    @Override
    public List<Emp> termQueryPageSortSourceFilter() {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("age").lte(200).gte(10));


        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withSort(SortBuilders.fieldSort("age").order(SortOrder.ASC))
                .withPageable(PageRequest.of(0,10))
                .withFields("name","content","age")
                .withFilter(boolQueryBuilder)
                .build();

        List<Emp> emps = elasticsearchTemplate.queryForList(build, Emp.class);


        return emps;
    }

    @Override
    public List<Emp> termQueryPageSortSourceFilterHighLighter() {

        HighlightBuilder.Field field = new HighlightBuilder
                .Field("*")
                .requireFieldMatch(false)
                .preTags("<font color='red'>")
                .postTags("</font>");

        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.termQuery("intr","好人"))
                .withHighlightFields(field)
                .build();



//        List<Emp> emps = elasticsearchTemplate.queryForList(build, Emp.class);


        AggregatedPage<Emp> emps=elasticsearchTemplate.queryForPage(build, Emp.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHit[] hits = searchResponse.getHits().getHits();
                List<Emp> list=new ArrayList<>();
                for (SearchHit hit : hits) {
                    Emp emp = new Emp();
                    //原始数据的对象
                    Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                    if (sourceAsMap.get("sex")!=null){
                        emp.setSex(Boolean.valueOf(sourceAsMap.get("sex").toString()));
                    }
                    emp.setAge(Integer.valueOf(sourceAsMap.get("age").toString()));
                    emp.setName(sourceAsMap.get("name").toString());
                    emp.setContent(sourceAsMap.get("content").toString());
                    emp.setId(sourceAsMap.get("id").toString());
                    emp.setIntr(sourceAsMap.get("intr").toString());


                    /*
                    * 首先拿到高亮数据 判断高亮数据中的每个字段是否高亮 如果高亮则返回高亮值 否则返回原始值
                    * */
//
                    Map<String, HighlightField> highlightFields = hit.getHighlightFields();
//                    HighlightField id = highlightFields.get("id");
//                    if (id!=null){
//                        emp.setId(highlightFields.get("id").getFragments()[0].toString());
//                    }
//                    HighlightField name = highlightFields.get("name");
//                    if (name!=null){
//                        emp.setName(highlightFields.get("name").getFragments()[0].toString());
//                    }
//
//                    HighlightField content = highlightFields.get("content");
//                    if (content!=null){
//                        emp.setContent(highlightFields.get("content").getFragments()[0].toString());
//                    }
//
//                    HighlightField intr = highlightFields.get("intr");
//                    if (intr!=null){
//                        emp.setIntr(highlightFields.get("intr").getFragments()[0].toString());
//                    }
                    for (String key : sourceAsMap.keySet()) {
                        if (highlightFields.get(key)!=null){
                            //高亮后的数据
                            String fragment = highlightFields.get(key).getFragments()[0].toString();
                           //获取方法名
                            String method="set"+key.substring(0,1).toUpperCase()+key.substring(1);
                           //获取类对象
                            Class<? extends Emp> aClass1 = emp.getClass();
                            //set方法
                            Method declaredMethod = null;
                            try {
                                declaredMethod = aClass1.getDeclaredMethod(method, fragment.getClass());
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }
                            try {
                              //调用set方法
                                declaredMethod.invoke(emp,fragment);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    list.add(emp);
                }
                return new AggregatedPageImpl<T>((List<T>)list);
            }
        });


        return emps.getContent();
    }
}
```

