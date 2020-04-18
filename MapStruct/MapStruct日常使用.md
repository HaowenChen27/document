## MapStruct 转换 Mapper的实例化

#### 1.如何把Mapper注入Spring IOC容器

```java
@Mapper(componentModel = "spring")
```

易错点： 启动类需要将mapper所在包加到scanBasePackages，不然扫不到



#### 2.接口直接定义，单例

```java
//定义
EpProjectPlanConverter INSTANCE = Mappers.getMapper(EpProjectPlanConverter.class);

//使用
EpProjectPlan plan = EpProjectPlanConverter.INSTANCE.dto2Model(dto);
```



#### 3.相关注解

```java
@Mapper //表示该接口作为映射接口，编译时MapStruct处理器的入口。
@Mappings //一组映射关系
@Mapping //一对映射关系，target：目标类字段，source ：源字段，expression ：target字段使用改表达式获取值
@InheritInverseConfiguration //表示方法继承相应的反向方法的反向配置
@InheritConfiguration //指定映射方法
```



##### @InheritConfiguration可以指定方法 

使用场景：已存在 source -> target 想要得到 List<source> -> List<target>

栗子：

```java
@Mappings({
            @Mapping(target = "expectPaymentTi", expression = "java(DateUtil.timeStr2Long(dto.getExpectPaymentTime()))"),
            @Mapping(target = "planStartTi", expression = "java(DateUtil.timeStr2Long(dto.getPlanStartTime()))"),
            @Mapping(target = "planEndTi", expression = "java(DateUtil.timeStr2Long(dto.getPlanEndTime()))")
    })
    EpProjectPlan dto2Model(EpProjectPlanDTO dto);

    EpProjectPlan subDto2Model(EpProjectPlanDTO dto);
```

以上定义了两个类似的实体类转换方法，此时我们需要进行List<source> -> List<target>

定义一个：

```java
/**
     * EpProjectPlanDTO -> EpProjectPlan list
     * @param dtos
     * @return
     */
    List<EpProjectPlan> toList(List<EpProjectPlanDTO> dtos);
```

运行报错：

```java
Warning:(33, 19) java: Unmapped target properties: "planStage, remark, createTi, createUserId, modifyTi, modifyUserId, version".
Warning:(35, 19) java: Unmapped target properties: "planStage, remark, createTi, createUserId, modifyTi, modifyUserId, version".
Error:(44, 25) java: Ambiguous mapping methods found for mapping collection element to com.jyb.epm.api.model.EpProjectPlan: com.jyb.epm.api.model.EpProjectPlan dto2Model(com.jyb.epm.api.vo.projectplan.EpProjectPlanDTO dto), com.jyb.epm.api.model.EpProjectPlan subDto2Model(com.jyb.epm.api.vo.projectplan.EpProjectPlanDTO dto).
Warning:(52, 22) java: Unmapped target properties: "expectPaymentTime, planStartTime, planEndTime".
```

**Ambiguous mapping methods found for mapping collection**

意思是可以找到多个方法来进行collection的转化（没有显式报错只能踩坑）

现在使用@InheritConfiguration注解试下，指定方法(需要配合@Named注解使用必须加不然还是报错)

```java
@Named(value = "dto2Model")
EpProjectPlan dto2Model(EpProjectPlanDTO dto);
```



成功运行：

```java
@Override
    public List<EpProjectPlan> toList(List<EpProjectPlanDTO> dtos) {
        if ( dtos == null ) {
            return null;
        }

        List<EpProjectPlan> list = new ArrayList<EpProjectPlan>( dtos.size() );
        for ( EpProjectPlanDTO epProjectPlanDTO : dtos ) {
            list.add( subDto2Model( epProjectPlanDTO ) );
        }

        return list;
    }
```





##### @InheritInverseConfiguration //表示方法继承相应的反向方法的反向配置

场景：我们已经定义了一个正向的source2target

```java
@Mappings({
            @Mapping(target = "expectPaymentTi", expression = "java(DateUtil.timeStr2Long(dto.getExpectPaymentTime()))"),
            @Mapping(target = "planStartTi", expression = "java(DateUtil.timeStr2Long(dto.getPlanStartTime()))"),
            @Mapping(target = "planEndTi", source = "expectPaymentTi2"),
    })

    EpProjectPlan dto2Model(EpProjectPlanDTO dto);
```



上面做了一个小改动 expectPaymentTi2 -> planEndTi

如果我们直接定义一个target2source

```java
EpProjectPlanDTO model2dto(EpProjectPlan plan);


//实现层
@Override
    public EpProjectPlanDTO model2dto(EpProjectPlan plan) {
        if ( plan == null ) {
            return null;
        }

        EpProjectPlanDTO epProjectPlanDTO = new EpProjectPlanDTO();

        epProjectPlanDTO.setId( plan.getId() );
        epProjectPlanDTO.setEpId( plan.getEpId() );
        epProjectPlanDTO.setExpectPaymentTi( plan.getExpectPaymentTi() );
        epProjectPlanDTO.setPlanStartTi( plan.getPlanStartTi() );
        epProjectPlanDTO.setPlanEndTi( plan.getPlanEndTi() );
        epProjectPlanDTO.setExpectNegotiationNum( plan.getExpectNegotiationNum() );
        epProjectPlanDTO.setExpectFallNum( plan.getExpectFallNum() );
        epProjectPlanDTO.setLeastFallNum( plan.getLeastFallNum() );
        epProjectPlanDTO.setPlanRemark( plan.getPlanRemark() );

        return epProjectPlanDTO;
    }
```

我们看到基本上还是按照属性名来进行映射的。

如果我们需要进行target2source的逆向操作我们就不得不指定逆向的方法：

```java
@Mappings({
            @Mapping(target = "expectPaymentTi", expression = "java(DateUtil.timeStr2Long(dto.getExpectPaymentTime()))"),
            @Mapping(target = "planStartTi", expression = "java(DateUtil.timeStr2Long(dto.getPlanStartTime()))"),
            @Mapping(target = "planEndTi", source = "expectPaymentTi2"),
    })
    @Named("dto2Model")
    EpProjectPlan dto2Model(EpProjectPlanDTO dto);


    @InheritInverseConfiguration(name = "dto2Model")
    EpProjectPlanDTO model2dto(EpProjectPlan plan);
```



生成的实现类代码：

```java
@Override
    public EpProjectPlanDTO model2dto(EpProjectPlan plan) {
        if ( plan == null ) {
            return null;
        }

        EpProjectPlanDTO epProjectPlanDTO = new EpProjectPlanDTO();

        epProjectPlanDTO.setExpectPaymentTi2( plan.getPlanEndTi() );
        epProjectPlanDTO.setId( plan.getId() );
        epProjectPlanDTO.setEpId( plan.getEpId() );
        epProjectPlanDTO.setExpectPaymentTi( plan.getExpectPaymentTi() );
        epProjectPlanDTO.setPlanStartTi( plan.getPlanStartTi() );
        epProjectPlanDTO.setPlanEndTi( plan.getPlanEndTi() );
        epProjectPlanDTO.setExpectNegotiationNum( plan.getExpectNegotiationNum() );
        epProjectPlanDTO.setExpectFallNum( plan.getExpectFallNum() );
        epProjectPlanDTO.setLeastFallNum( plan.getLeastFallNum() );
        epProjectPlanDTO.setPlanRemark( plan.getPlanRemark() );

        return epProjectPlanDTO;
    }
```

可以看到 ExpectPaymentTi2和PlanEndTi对应上了

*<font color=red>注意：expression里的内容并不能逆向使用</font>*

### 坑点：强依赖lombok

不仅需要使用，甚至不能随便重写get/set方法，如果getset入参和出参不一致会出现奇奇怪怪的问题
