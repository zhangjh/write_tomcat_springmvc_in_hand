### 手写实现springMVC

结合上一个[手写tomcat框架](https://github.com/zhangjh/write_tomcat_springmvc_in_hand/tree/master/tomcat)实现

### 主要包括的内容：
1. 手写tomcat框架(见[手写实现tomcat框架]()仓库)
    这里为了跟springmvc框架结合使用，做了一些修改适配，如去除了xml配置方式，初始化增加bean实例化等springmvc操作
2. 自定义实现常用注解(`@Autowired、@Configuration、@Controller、@Repository、@RequestMapping、@Service`)
    从入口配置、数据层、应用层、Web请求层自定义实现SpringMVC中各主要注解
3. Servlet
    在手写tomcat框架中，Servlet的url映射配置是配置文件配置的，在这里将实现根据@RequestMapping注解定义的属性值自动匹配，并实现请求的自动分发
4. Spring IOC、DI
    实现Spring中的核心机制之一，自动实例化并实现依赖注入

### 运行
类似SpringBoot的启动方式，启动文件为`Application.java`，直接运行`main`方法即可。
