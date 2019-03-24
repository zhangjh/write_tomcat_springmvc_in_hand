package com.test.springmvc.servlet.tomcat;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.test.springmvc.annotation.Autowired;
import com.test.springmvc.annotation.Controller;
import com.test.springmvc.annotation.Repository;
import com.test.springmvc.annotation.RequestMapping;
import com.test.springmvc.annotation.Service;
import com.test.springmvc.servlet.domain.Request;
import com.test.springmvc.servlet.domain.Response;

/**
 * @author : zhangjh
 * @date : 2019-03-19
 * Description: 抽象类Tomcat，用来给两种初始化方式的Tomcat继承
 */
public class MyTomCat {
    /** 启动端口 */
    private int port;

    /** 线程池最大线程数 */
    private static final int THREAD_POOL_MAX_SIZE = 200;
    /** 线程池常驻线程数 */
    private static final int THREAD_CORE_POOL_SIZE = 10;
    /** 闲置线程存活时间 */
    private static final int THREAD_KEEP_ALIVE = 30;

    /** 扫包基础路径 */
    private String basePackage;

    /** 基包路径下所有类的全限定类名集合 */
    private List<String> packageNames = new ArrayList<>();

    /** IOC容器，存储实例名和实例映射 */
    private Map<String, Object> instanceMap = new HashMap<>();

    /** 全限定类名和注解名映射 */
    private Map<String, String> nameMap = new HashMap<>();

    /** url路径和方法映射 */
    private Map<String, Method> urlMethodMap = new HashMap<>();

    /** 方法和对象全限定类名音声 */
    private Map<Method, String> methodPackageMap = new HashMap<>();

    public MyTomCat(String basePackage, int port) {
        this.port = port;
        this.basePackage = basePackage;
    }

    private void init() {
        // 扫描基包下所有类全限定名称
        scanBasePackage(basePackage);
        // 实例化并放入ioc容器
        instance();
        // 依赖注入
        springIoc();
        // 完成url和方法的映射绑定
        handleUrlMethodMap();
    }

    /** 将首字母小写 */
    private String lowerFirst(String name) {
        char[] chars = name.toCharArray();
        char first = chars[0];
        if(first >= 'A' && first <= 'Z') {
            first += 32;
        }
        chars[0] = first;
        return String.valueOf(chars);
    }

    /** 如果实例名不为空则使用设置的名称，将首字母转为小写，否则使用类名作为实例名 */
    private String getBeanName(String name, Class<?> clazz) {
        if(name != null && !"".equals(name)) {
            return lowerFirst(name);
        }
        return lowerFirst(clazz.getSimpleName());
    }

    private void scanBasePackage(String basePackage) {
        URL url = this.getClass().getClassLoader().getResource(basePackage.replaceAll("\\.", "/"));

        if(url == null) {
            return;
        }
        File basePackageFile = new File(url.getPath());

        File[] files = basePackageFile.listFiles();
        if(files == null) {
            return;
        }
        for (File file : files) {
            if(file.isDirectory()) {
                scanBasePackage(basePackage + "." + file.getName());
            }else if(file.isFile()) {
                packageNames.add(basePackage + "." + file.getName().replaceAll(".class", "").trim());
            }
        }
    }

    private void instance() {
        if(packageNames.size() <= 0){
            return;
        }
        try {
            for (String name : packageNames) {
                Class<?> clazz = Class.forName(name);

                String beanName = null;
                // 这个类上标注了Controller注解，将类实例化后跟controller名称绑定
                if(clazz.isAnnotationPresent(Controller.class)) {
                    // 获取Controller注解的属性值
                    Controller controller = clazz.getAnnotation(Controller.class);
                    String controllerName = controller.value();
                    beanName = getBeanName(controllerName, clazz);
                }else if (clazz.isAnnotationPresent(Service.class)) {
                    Service service = clazz.getAnnotation(Service.class);
                    String serviceName = service.value();
                    beanName = getBeanName(serviceName, clazz);
                }else if (clazz.isAnnotationPresent(Repository.class)) {
                    Repository repository = clazz.getAnnotation(Repository.class);
                    String repositoryName = repository.value();
                    beanName = getBeanName(repositoryName, clazz);
                }
                if(beanName != null) {
                    instanceMap.put(beanName, clazz.newInstance());
                    nameMap.put(name, beanName);
                }
            }
        }catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void springIoc(){
        for (Entry<String, Object> entry : instanceMap.entrySet()) {
            // 获取所有的字段
            Field[] fields = entry.getValue().getClass().getDeclaredFields();

            try {
                for (Field field : fields) {
                    // 标注Autowired注解的字段需要注入，从IOC容器中找出对应的实例
                    if(!field.isAnnotationPresent(Autowired.class)) {
                        continue;
                    }
                    String beanName = field.getAnnotation(Autowired.class).value();

                    // 如果没有设置注解值，使用字段名去找注入的实例
                    if("".equals(beanName)) {
                        beanName = lowerFirst(field.getType().getSimpleName());
                    }

                    field.setAccessible(true);
                    field.set(entry.getValue(), instanceMap.get(beanName));
                }
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleUrlMethodMap() {
        if(packageNames.size() <= 0) {
            return;
        }
        try {
            for (String name : packageNames) {
                Class<?> clazz = Class.forName(name);
                if(!clazz.isAnnotationPresent(Controller.class)) {
                    continue;
                }
                Method[] methods = clazz.getMethods();
                String urlPrefix = "";
                String fullUrl;

                // 标注在类上的RequestMapping属性值表示url的公共前缀
                if(clazz.isAnnotationPresent(RequestMapping.class)) {
                    urlPrefix = clazz.getAnnotation(RequestMapping.class).value();
                }
                // 标注在方法上的RequestMapping属性值
                for (Method method : methods) {
                    if(!method.isAnnotationPresent(RequestMapping.class)) {
                        continue;
                    }
                    String url = method.getAnnotation(RequestMapping.class).value();
                    fullUrl = (urlPrefix + url).replaceAll("/+", "/");

                    urlMethodMap.put(fullUrl, method);
                    methodPackageMap.put(method, name);
                }
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.init();
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Tomcat 已启动， 地址：localhost, 端口：" + port);

            // 监听，处理任务
            while (true) {
                ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("Tomcat-work-%d").build();
                ExecutorService exec = new ThreadPoolExecutor(
                    THREAD_CORE_POOL_SIZE,
                    THREAD_POOL_MAX_SIZE,
                    THREAD_KEEP_ALIVE,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(),
                    threadFactory
                );
                exec.submit(new ServletProcess(serverSocket.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doGet(Request request, Response response) {
        doPost(request, response);
    }

    private void doPost(Request request, Response response) {

        String url = request.getUrl();

        // 从映射中找出要处理的method
        Method method = urlMethodMap.get(url);
        if(method != null) {
            // 通过method找到controller对象，并执行对象方法
            String packageName = methodPackageMap.get(method);
            String controllerName = nameMap.get(packageName);
            // 从实例容器中找到对应的controller实例
            Object controller = instanceMap.get(controllerName);

            method.setAccessible(true);
            try {
                method.invoke(controller, request, response);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }else {
            response.outPrint("404");
        }
    }

    private void dispatch(Request request, Response response) {
        if("get".equalsIgnoreCase(request.getMethod())) {

            this.doGet(request, response);
        }else {
            this.doPost(request, response);
        }
    }

    class ServletProcess extends Thread {
        private Socket socket;

        public ServletProcess(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread());
                Request request = new Request(this.socket.getInputStream());
                Response response = new Response(this.socket.getOutputStream());
                dispatch(request, response);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
