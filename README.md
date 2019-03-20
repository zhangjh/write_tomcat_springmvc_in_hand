# write_tomcat_servlet_in_hand

## 一个通过手写Tomcat来熟悉Servlet运行原理的demo框架

支持web.xml和纯Java配置两种初始化方式。

### 主要的内容包括：
1. 根据HTTP协议规范封装请求和响应(`com.test.tomcat.servlet.domain`)

后端服务的响应浏览器是如何识别显示的？demo框架里只以最简单的html文本做了演示，毕竟只是为了学习原理不是要写一个生产环境的Tomcat容器
2. 配置初始化(`com.test.tomcat.servlet.conf;web.xml`)

配置初始化指的是servlet的配置，指定哪个访问路径由哪个servlet class提供服务。这里提供了传统的web.xml方式和纯Java Config的方式。
3. Servlet服务(`com.test.tomcat.servlet.servlet`)

提供服务的Servlet，仿照Servlet提供了service方法及常用的doGet，doPost。
Servlet处理器通过读取配置，根据不同的路径利用反射生成具体的Servlet实例，调用不同实例的service方法。未配置的路径返回404提示。
4. Tomcat容器(`com.test.tomcat.servlet.tomcat`)

TomCat容器负责串起流程开启web服务，利用Socket来监听并处理任务。
demo框架没有提供线程池机制了，每个请求都会新开一个线程提供服务。
通过继承抽象基类实现了两个Tomcat实例，分别通过web.xml及JavaConfig来进行初始化。

### 运行
`com.test.tomcat.servlet.tomcat.MyTomcatWithJavaConfig`
`com.test.tomcat.servlet.tomcat.MyTomCatWithXml`

直接运行两个类的main方法即可。


