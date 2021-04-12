# 动态依赖注入组件

spring-context-config-support是一个可以基于配置中心动态实时修改依赖注入的组件。无需重启，实时生效，性能强悍。

 - starter实现，开箱即用
 - 无代码侵入性 只需三个注解（只加不改）
 - 目前已适配主流的Nacos、Apollo分布式配置中心

# 她的由来
在复杂的实际需求中，我们时常需求根据一定条件注入需要的依赖组件（component）。我们可以基于工厂模式或者策略模式来实现或者之前我所说[基于FactoryBean来实现][1]
但需要不停重复造轮子，太烦了！所以她就应运而生。

# 她好用吗？
  由于她设计之初就定位“无代码侵入性” 所以spring的变成习惯无需改变，你平时怎么注入就怎么注入。你只需要加三个注解即可。所以 是好用的！哈哈哈
  
# 性能如何
 性能可以！因为设计中能不用反射绝不用反射，监听配置中心的配置变化粒度细到只针对修改的key来实现是否要动态修改依赖的注入等等。很多都是基于底层接口来扩展实现。
 
 
# 原理
![动态依赖注入原理][2]

# 如何使用
## 引入pom依赖
```java
  <dependency>
    <groupId>red.honey</groupId>
    <artifactId>spring-context-config-support</artifactId>
    <version>1.0.0</version>
  </dependency>
```

## 举个例子
1、启动类打上开启组件注解@EnableHoneySpringSupport
```java
@SpringBootApplication
@EnableHoneySpringSupport(type = ConfigType.YAML)
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```
2、在你需要动态依赖注入的类上打@RefreshInjected与@FieldRefresh。

 - @RefreshInjected标记需要动态依赖的注入的类。
 - @FieldRefresh标记具体需要动态依赖注入的属性。

```java
@RefreshInjected
public class HoneyComponent {
    @Autowired
    @FieldRefresh(name = "service")
    private SomeService service;
}
```
整合完毕。
3、配置中心修改配置。以Nacos为例
添加一个与 @FieldRefresh(name = "service")中name属性一致的key，值为该属性需要从Spring Ioc中索取并注入的BeanName。以后动态注入就只其值为对应的beanName既可。
Apollo同此。
![Nacos配置中心][3]

> 具体使用方法可以参看我这边博客：[动态依赖注入组件][4]

## 相关具体使用介绍

 1. 主注解@EnableHoneySpringSupport
 
| 注解属性  |  类型 | 含义 |
| :------------ |:--------------- |:---------------:| 
| groupId   |  String   |Nacos 的groupId。 if ConfigCenterType is NACOS 默认 DEFAULT_GROUP |   
| type      | com.alibaba.nacos.api.config.ConfigType       |  Nacos配置中心配置类型。 if ConfigCenterType is NACOS 默认ConfigType.PROPERTIES| 
| converter    | NacosConfigConverter的实现类 | Nacos 的配置转换器 if ConfigCenterType is NACOS       默认 PropertiesNacosConfigConverter| 
| centerType   |red.honey.spring.context.support.commom.ConfigCenterType   | 配置中心类型 默认Nacos       | 


2、RefreshInjected注解

| 注解属性  |  类型 | 含义 |
| :------------ |:--------------- |:---------------:| 
| fields   |  String[]   | 需要动态依赖注入的属性数组。 |

3、FieldRefresh注解
| 注解属性  |  类型 | 含义 |
| :------------ |:--------------- |:---------------:| 
| name   |  String   | 需要动态依赖注入的属性名。 |

> 当需要动态依赖注入的属性比较多时，推荐使用2中的注解以减少过多的FieldRefresh注解.
> 属性名需要注意的是要与配置中心的key一致，不然将导致动态依赖注入失效。同时配置中心key对应的value需为Ioc的所需的BeanName.


  [1]: https://blog.csdn.net/China_eboy/article/details/115513512https://blog.csdn.net/China_eboy/article/details/115513512
  [2]: http://oss.honey.red/public/%E5%8A%A8%E6%80%81%E4%BE%9D%E8%B5%96%E6%B3%A8%E5%85%A5%E5%8E%9F%E7%90%86.png
  [3]: http://oss.honey.red/public/NACOS.png
  [4]: https://blog.csdn.net/China_eboy/article/details/115629440