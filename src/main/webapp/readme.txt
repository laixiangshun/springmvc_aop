Spring MVC 和 Spring 整合的时候，SpringMVC的springmvc.xml文件中 配置扫描包，不要包含 service的注解，
Spring的applicationContext.xml文件中 配置扫描包时，不要包含controller的注解，如下所示：
SpringMVC的xml配置：
<context:component-scan base-package="com.xxx">
  <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Service"/>
</context:component-scan>
Spring MVC启动时的配置文件，包含组件扫描、url映射以及设置freemarker参数，让spring不扫描带有@Service注解的类。为什么要这样设置？
因为springmvc.xml与applicationContext.xml不是同时加载，如果不进行这样的设置，那么，
spring就会将所有带@Service注解的类都扫描到容器中，等到加载applicationContext.xml的时候，
会因为容器已经存在Service类，使得cglib将不对Service进行代理，直接导致的结果就是在applicationContext 中的事务配置不起作用，
发生异常时，无法对数据进行回滚。以上就是原因所在。
同样的在Spring的xml配置如下：
<context:component-scan base-package="com.insigma">           
 <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
</context:component-scan>
扫描包路径，不扫描带有@Controller注解的类。因为这些类已经随容器启动时，在springmvc.xml中扫描过一遍了;


另外以上所述的是对controller进行切面时的配置，如果是对service进行切面，那么

<context:component-scan base-package="com.java1234.aop" />
<aop:aspectj-autoproxy />

这两个注释就要用在ApplicationContext.xml里面了，注意此时不要开启aop的cglib代理模式。

1.SpringMVC这个框架很好用，没问题，他的注解也简化了很多的配置，但是请注意@Controller和@Service都是SpringMVC框架包里面的，
也就是说，这些类的实例化以及注入也是由SpringMVC这个框架完成的（确切的来说是这个框架自己有的上下文的IoC容器完成的）
2.对AOP和事务的支持是Spring框架本身完成的，是Spring框架的应用上下文所扫描并处理的


Spring中的切面类固然要用@Aspect标注，但也不要忘了用@Componet标注，这样才能被注册到容器中
