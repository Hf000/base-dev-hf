package org.hf.boot.springboot.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hf.boot.springboot.error.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 服务加载工具类
 */
@Slf4j
@Component
public class ServiceLoaderUtil {

    /**
     * 获取当前系统的类加载对象
     */
    private static final URLClassLoader CLASS_LOADER = (URLClassLoader) ClassLoader.getSystemClassLoader();

    /**
     * 调用指定jar下的类方法
     * @param path          jar路径
     * @param methodName    方法名称
     * @param argClass      方法参数类型
     */
    public static void loadJarsInvokeMethod(String path, String methodName, Class<?> argClass) {
        if (StringUtils.isBlank(path)) {
            throw new BusinessException("路径不能为空");
        }
        File file = new File(path);
        // 循环加载所有的文件
        loopLoadFile(file, methodName, argClass);
    }

    /**
     * 循环加载文件夹里的文件
     * @param file 文件对象
     * @param methodName 需要调用的方法名称
     * @param argClass 需要调用的方法参数类型
     */
    private static void loopLoadFile(File file, String methodName, Class<?> argClass) {
        // 判断当前文件是否是问卷夹
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles == null) {
                throw new BusinessException("加载文件夹下的子文件异常");
            }
            for (File subFile : subFiles) {
                // 判断当前是否是文件
                if (subFile.isFile()) {
                    loadJarFile(subFile, methodName, argClass);
                } else {
                    // 循环加载文件夹
                    loopLoadFile(subFile, methodName, argClass);
                }
            }
        } else {
            loadJarFile(file, methodName, argClass);
        }
    }

    private static void loadJarFile(File path, String methodName, Class<?> argClass) {
        try {
            // 获取类的全路径
            URL url = path.toURI().toURL();
            // 加载指定名称的方法
            Method method;
            try {
                //method = URLClassLoader.class.getDeclaredMethod("sendMsg", Map.class);
                method = URLClassLoader.class.getMethod(methodName, argClass);
            } catch (Exception e) {
                throw new BusinessException("未找到指定的方法", e);
            }
            method.setAccessible(true);
            // 通过当前系统默认的类加载对象和指定类的全路径去加载类,然后调用类的指定方法
            method.invoke(CLASS_LOADER, url);
        } catch (Exception e) {
            throw new BusinessException("文件加载异常", e);
        }
    }

    public static void invokeMethodFromPath(String path, String className, String methodName, Object args) {
        URLClassLoader classLoader = null;
        try {
            File f = new File(path);
            URL url = f.toURI().toURL();
            // 获取类加载器
            classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
            // 加载指定的类
            Class<?> product = classLoader.loadClass(className);
            // 获取实例
            Object obj = product.newInstance();
            // 获取方法
            Method method=product.getDeclaredMethod(methodName, args.getClass());
            // 执行方法
            method.invoke(obj, args);
        } catch (Exception e) {
            throw new BusinessException("调用指定类方法异常", e);
        } finally {
            try {
                if (classLoader != null) {
                    classLoader.close();
                }
            } catch (Exception e) {
                log.error("调用指定类方法异常, 关闭资源异常", e);
            }
        }
    }

    /**
     * 获取当前服务文件的的绝对路径
     * @return 服务文件的绝对路径
     */
    public static String getApplicationFolder() {
        String path = ServiceLoaderUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String servicePath = new File(path).getParent();
        log.info("servicePath={}", servicePath);
        return servicePath;
    }

    public static void main(String[] args) throws Exception {
        // 调用指定jar下的指定方法
//        loadJarsInvokeMethod("E:\\work\\customApp\\lib", "testMethod", Map.class);

        // 调用指定路径文件下的指定方法
//        invokeMethodFromPath("E:\\work\\customApp\\lib\\custom-app-1.0-SNAPSHOT.jar", "org.hf.spi.CustomImpl", "testMethod", new HashMap<>());

        // 当前服务文件的绝对路径
//        getApplicationFolder();
    }

    /**
     * properties文件中配置项:custom.className.list = {"org.hf.spi.CustomImpl"}
     */
    @Value("#{${custom.className.list:{}}}")
    private List<String> customClassNameList;

    /**
     * properties文件中配置项:custom.className.list.map = {"custom-app-1.0-SNAPSHOT.jar":"org.hf.spi.CustomImpl"}
     */
    @Value("#{${custom.className.list.map:{}}}")
    private Map<String, String> customClassNameListMap;

    public void doExecuteMethod(String path, String methodName, Object args) {
        if (StringUtils.isBlank(path)) {
            path = "E:\\work\\customApp\\lib";
        }
        // 加载指定位置的jar
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            // 遍历文件夹下的所有文件
            for (File subFile : files) {
                // 获取文件名称
                String name = subFile.getName();
                // 组装类路径
                String classNamePath = path + "\\" + name;
                //执行反射相关的方法
                // ServiceLoaderUtils serviceLoaderUtils = new ServiceLoaderUtils();
                // result = serviceLoaderUtils.loadMethod(classNamePath, "testMethod");
                URLClassLoader classLoader = null;
                try {
                    File f = new File(classNamePath);
                    URL urlB = f.toURI().toURL();
                    classLoader = new URLClassLoader(new URL[]{urlB}, Thread.currentThread().getContextClassLoader());
                    for (Map.Entry<String, String> mapEntry : customClassNameListMap.entrySet()) {
                        if(name.equals(mapEntry.getKey())){
                            Class<?> loadClass = classLoader.loadClass(mapEntry.getValue());
                            if(Objects.isNull(loadClass)){
                                return;
                            }
                            // 获取实例
                            Object obj = loadClass.newInstance();
                            // 获取方法
                            Method method = loadClass.getDeclaredMethod(methodName, args.getClass());
                            // 调用方法
                            method.invoke(obj, args);
                        }
                    }
                } catch (Exception e) {
                    throw new BusinessException("调用指定方法异常", e);
                } finally {
                    try {
                        if (classLoader != null) {
                            classLoader.close();
                        }
                    } catch (Exception e) {
                        log.error("关闭资源异常", e);
                    }
                }
            }
        }
    }

    private void loadMethod(String classNamePath, String methodName, Object args) {
        URLClassLoader classLoader = null;
        try {
            File f = new File(classNamePath);
            URL url = f.toURI().toURL();
            // 获取类加载器
            classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());
            // 遍历配置的类集合
            for(String claName : customClassNameList){
                // 加载指定类
                Class<?> loadClass = classLoader.loadClass(claName);
                if(Objects.isNull(loadClass)) {
                    continue;
                }
                // 获取实例
                Object obj = loadClass.newInstance();
                // 获取方法
                Method method = loadClass.getDeclaredMethod(methodName, args.getClass());
                // 调用指定方法
                method.invoke(obj, args);
            }
        } catch (Exception e) {
            throw new BusinessException("调用指定类的方法异常", e);
        } finally {
            try {
                if (classLoader != null) {
                    classLoader.close();
                }
            } catch (Exception e) {
                log.error("关闭资源异常", e);
            }
        }
    }
}