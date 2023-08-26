package org.hf.application.custom.rpc.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 获取接口的所有实现类 理论上也可以用来获取类的所有子类
 * 查询路径有限制，只局限于接口所在模块下，比如pandora-gateway,而非整个pandora（会递归搜索该文件夹下所以的实现类）
 * 路径中不可含中文，否则会异常。若要支持中文路径，需对该模块代码中url.getPath() 返回值进行urldecode.
 */
public class ClassUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ClassUtil.class);

    public static ArrayList<Class<?>> getAllClassByInterface(Class<?> clazz) {
        ArrayList<Class<?>> list = new ArrayList<>();
        // 判断是否是一个接口
        if (clazz.isInterface()) {
            try {
                ArrayList<Class<?>> allClass = getAllClass(clazz.getPackage().getName());
                // 循环判断路径下的所有类是否实现了指定的接口 并且排除接口类自己
                for (Class<?> aClass : allClass) {
                    // 判断是不是同一个接口 isAssignableFrom:判定此 Class 对象所表示的类或接口与指定的 Class   参数所表示的类或接口是否相同，或是否是其超类或超接口
                    if (clazz.isAssignableFrom(aClass)) {
                        if (!clazz.equals(aClass)) {
                            // 自身并不加进去
                            list.add(aClass);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("出现异常" + e.getMessage());
            }
        }
        LOG.info("class list size :" + list.size());
        return list;
    }


    /**
     * 从一个指定路径下查找所有的类
     * @param packageName 包名
     */
    private static ArrayList<Class<?>> getAllClass(String packageName) {
        LOG.info("packageName to search：" + packageName);
        List<String> classNameList = getClassName(packageName);
        ArrayList<Class<?>> list = new ArrayList<>();
        for (String className : classNameList) {
            try {
                list.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("load class from name failed:" + className + e.getMessage());
            }
        }
        LOG.info("find list size :" + list.size());
        return list;
    }

    /**
     * 获取某包下所有类
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName) {
        List<String> fileNames = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String packagePath = packageName.replace(".", "/");
        URL url = loader.getResource(packagePath);
        if (url != null) {
            String type = url.getProtocol();
            LOG.debug("file type : " + type);
            if ("file".equals(type)) {
                String fileSearchPath = url.getPath();
                LOG.debug("fileSearchPath: " + fileSearchPath);
                fileSearchPath = fileSearchPath.substring(0, fileSearchPath.indexOf("/classes"));
                LOG.debug("fileSearchPath: " + fileSearchPath);
                fileNames = getClassNameByFile(fileSearchPath);
            } else if ("jar".equals(type)) {
                try {
                    JarURLConnection jarUrlConnection = (JarURLConnection) url.openConnection();
                    JarFile jarFile = jarUrlConnection.getJarFile();
                    fileNames = getClassNameByJar(jarFile);
                } catch (java.io.IOException e) {
                    throw new RuntimeException("open Package URL failed：" + e.getMessage());
                }
            } else {
                throw new RuntimeException("file system not support! cannot load MsgProcessor！");
            }
        }
        return fileNames;
    }

    /**
     * 从项目文件获取某包下所有类
     * @param filePath 文件路径
     * @return 类的完整名称
     */
    private static List<String> getClassNameByFile(String filePath) {
        List<String> myClassName = new ArrayList<>();
        File file = new File(filePath);
        File[] childFiles = file.listFiles();
        if (childFiles == null) {
            return myClassName;
        }
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                // 递归查找
                myClassName.addAll(getClassNameByFile(childFile.getPath()));
            } else {
                String childFilePath = childFile.getPath();
                if (childFilePath.endsWith(".class")) {
                    childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
                    childFilePath = childFilePath.replace("\\", ".");
                    myClassName.add(childFilePath);
                }
            }
        }
        return myClassName;
    }

    /**
     * 从jar获取某包下所有类
     * @return 类的完整名称
     */
    private static List<String> getClassNameByJar(JarFile jarFile) {
        List<String> myClassName = new ArrayList<String>();
        try {
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();
                LOG.info("entrys jarfile:"+entryName);
                if (entryName.endsWith(".class")) {
                    entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
                    myClassName.add(entryName);
                    LOG.debug("Find Class :"+entryName);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("发生异常:" + e.getMessage());
        }
        return myClassName;
    }
}