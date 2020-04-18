## 前言

按照日常开发习惯，对于不同领域层使用不同JavaBean对象传输数据，避免相互影响，因此基于数据库实体对象User衍生出比如UserDto、UserVo等对象，于是在不同层之间进行数据传输时，不可避免地需要将这些对象进行互相转换操作。

常见的转换方式有：

- 调用getter/setter方法进行属性赋值
- 调用BeanUtil.copyPropertie进行反射属性赋值

第一种方式不必说，属性多了就需要写一大坨getter/setter代码。第二种方式比第一种方式要简便很多，但是坑巨多，比如sources与target写反，难以定位某个字段在哪里进行的赋值，同时因为用到反射，导致性能也不佳。



鉴于此，今天写一写第三种对象转换方式，本文使用的是 MapStruct 工具进行转换，MapStruct 原理也很简单，就是在代码编译阶段生成对应的赋值代码，底层原理还是调用getter/setter方法，但是这是由工具替我们完成，MapStruct在不影响性能的情况下，解决了前面两种方式弊端，很赞~

## 准备工作

为了讲解 MapStruct 工具的使用，本文使用常见的 User 类以及对应 UserDto 对象来演示。

```java
@Data
@Accessors(chain = true)
public class User {
    private Long id;
    private String username;
    private String password; // 密码
    private Integer sex;  // 性别
    private LocalDate birthday; // 生日
    private LocalDateTime createTime; // 创建时间
    private String config; // 其他扩展信息，以JSON格式存储
    private String test; // 测试字段
}
@Data
@Accessors(chain = true)
public class UserVo {
    private Long id;
    private String username;
    private String password;
    private Integer gender;
    private LocalDate birthday;
    private String createTime;
    private List<UserConfig> config;
      private String test; // 测试字段
    @Data
    public static class UserConfig {
        private String field1;
        private Integer field2;
    }
}
```

注意观察这两个类的区别。



## 一、MapStruct 配置以及基础使用

项目中引入 MapStruct 的依赖

```xml
<dependency>
  <groupId>org.mapstruct</groupId>
  <artifactId>mapstruct</artifactId>
  <version>1.3.1.Final</version>
</dependency>
<dependency>
  <groupId>org.mapstruct</groupId>
  <artifactId>mapstruct-processor</artifactId>
  <version>1.3.1.Final</version>
</dependency>
```

因为项目中的对象转换操作基本都一样，因此抽取除了一个转换基类，不同对象如果只是简单转换可以直接继承该基类，而无需覆写基类任何方法，即只需要一个空类即可。如果子类覆写了基类的方法，则基类上的 **@Mapping** 会失效。



```java
@MapperConfig
public interface BaseMapping<SOURCE, TARGET> {
    /**
     * 映射同名属性
     */
    @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    TARGET sourceToTarget(SOURCE var1);
    /**
     * 反向，映射同名属性
     */
    @InheritInverseConfiguration(name = "sourceToTarget")
    SOURCE targetToSource(TARGET var1);
    /**
     * 映射同名属性，集合形式
     */
    @InheritConfiguration(name = "sourceToTarget")
    List<TARGET> sourceToTarget(List<SOURCE> var1);
    /**
     * 反向，映射同名属性，集合形式
     */
    @InheritConfiguration(name = "targetToSource")
    List<SOURCE> targetToSource(List<TARGET> var1);
    /**
     * 映射同名属性，集合流形式
     */
    List<TARGET> sourceToTarget(Stream<SOURCE> stream);
    /**
     * 反向，映射同名属性，集合流形式
     */
    List<SOURCE> targetToSource(Stream<TARGET> stream);
}
```

实现 User 与 UserVo 对象的转换器

```java
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface UserMapping extends BaseMapping<User, UserVo> {
    @Mapping(target = "gender", source = "sex")
    @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Override
    UserVo sourceToTarget(User var1);
    @Mapping(target = "sex", source = "gender")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Override
    User targetToSource(UserVo var1);
    default List<UserConfig> strConfigToListUserConfig(String config) {
        return JSON.parseArray(config, UserConfig.class);
    }
    default String listUserConfigToStrConfig(List<UserConfig> list) {
        return JSON.toJSONString(list);
    }
}
```

本文示例使用的是 Spring 的方式，@Mapper 注解的 componentModel 属性值为 spring，不过应该大多数都用的此模式进行开发。

@Mapping用于配置对象的映射关系，示例中 User 对象性别属性名为 sex，而UserVo对象性别属性名为gender，因此需要配置 target 与 source 属性。

password 字段不应该返回到前台，可以采取两种方式不进行转换，第一种就是在vo对象中不出现password字段，第二种就是在@Mapping中设置该字段 ignore = true。

MapStruct 提供了时间格式化的属性 **dataFormat**，支持Date、LocalDate、LocalDateTime等时间类型与String的转换。示例中birthday 属性为 LocalDate 类型，可以无需指定dataFormat自动完成转换，而LocalDateTime类型默认使用的是ISO格式时间，在国内往往不符合需求，因此需要手动指定一下 dataFormat。

## 二、自定义属性类型转换方法

一般常用的类型字段转换 MapStruct都能替我们完成，但是有一些是我们自定义的对象类型，MapStruct就不能进行字段转换，这就需要我们编写对应的类型转换方法，笔者使用的是JDK8，支持接口中的默认方法，可以直接在转换器中添加自定义类型转换方法。

示例中User对象的config属性是一个JSON字符串，UserVo对象中是List类型的，这需要实现JSON字符串与对象的互转。

```java
default List<UserConfig> strConfigToListUserConfig(String config) {
  return JSON.parseArray(config, UserConfig.class);
}
default String listUserConfigToStrConfig(List<UserConfig> list) {
  return JSON.toJSONString(list);
}
```

如果是 JDK8以下的，不支持默认方法，可以另外定义一个 转换器，然后再当前转换器的 @Mapper 中通过 uses = XXX.class 进行引用。

定义好方法之后，MapStruct当匹配到合适类型的字段时，会调用我们自定义的转换方法进行转换。

## 三、单元测试

```java
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapStructTest {
  @Resource
  private UserMapping userMapping;
  @Test
  public void tetDomain2DTO() {
    User user = new User()
      .setId(1L)
      .setUsername("zhangsan")
      .setSex(1)
      .setPassword("abc123")
      .setCreateTime(LocalDateTime.now())
      .setBirthday(LocalDate.of(1999, 9, 27))
      .setConfig("[{\"field1\":\"Test Field1\",\"field2\":500}]");
    UserVo userVo = userMapping.sourceToTarget(user);
    log.info("User: {}", user);
    //        User: User(id=1, username=zhangsan, password=abc123, sex=1, birthday=1999-09-27, createTime=2020-01-17T17:46:20.316, config=[{"field1":"Test Field1","field2":500}])
    log.info("UserVo: {}", userVo);
    //        UserVo: UserVo(id=1, username=zhangsan, gender=1, birthday=1999-09-27, createTime=2020-01-17 17:46:20, config=[UserVo.UserConfig(field1=Test Field1, field2=500)])
  }
  @Test
  public void testDTO2Domain() {
    UserConfig userConfig = new UserConfig();
    userConfig.setField1("Test Field1");
    userConfig.setField2(500);
    UserVo userVo = new UserVo()
      .setId(1L)
      .setUsername("zhangsan")
      .setGender(2)
      .setCreateTime("2020-01-18 15:32:54")
      .setBirthday(LocalDate.of(1999, 9, 27))
      .setConfig(Collections.singletonList(userConfig));
    User user = userMapping.targetToSource(userVo);
    log.info("UserVo: {}", userVo);
    //        UserVo: UserVo(id=1, username=zhangsan, gender=2, birthday=1999-09-27, createTime=2020-01-18 15:32:54, config=[UserVo.UserConfig(field1=Test Field1, field2=500)])
    log.info("User: {}", user);
    //        User: User(id=1, username=zhangsan, password=null, sex=2, birthday=1999-09-27, createTime=2020-01-18T15:32:54, config=[{"field1":"Test Field1","field2":500}])
  }
```

## 四、常见问题

1. 当两个对象属性不一致时，比如User对象中某个字段不存在与UserVo当中时，在编译时会有警告提示，可以在@Mapping中配置 ignore = true，当字段较多时，可以直接在@Mapper中设置unmappedTargetPolicy属性或者unmappedSourcePolicy属性为 ReportingPolicy.IGNORE即可。
2. 如果项目中也同时使用到了 Lombok，一定要注意 Lombok的版本要等于或者高于**1.18.10**，否则会有编译不通过的情况发生，笔者掉进这个坑很久才爬了出来，希望各位不要重复踩坑。



转载至：https://www.lizenghai.com/archives/57269.html

代码：[mapstruct最佳实践示例代码](https://github.com/Mosiki/learning-modules/tree/master/learning-mapstruct)

