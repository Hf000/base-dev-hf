package org.hf.application.custom.framework.framework.factory;

import org.apache.commons.lang3.StringUtils;
import org.hf.application.custom.framework.dao.AccountDao;
import org.hf.application.custom.framework.framework.util.ParseAnnotation;
import org.hf.application.custom.framework.framework.util.XmlBean;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p> bean工厂实现 -- xml配置文件解析实现 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:22
 */
public class XmlBeanFactory implements BeanFactory {

    /**
     * 将解析的bean对象实例存储到Map<id,instance>
     */
    private static Map<String, Object> beans;
    /**
     * 解析Controller类的所有加@RequestMapping注解的方法，获取注解的值，并记录注解值（Uri）和方法的映射关系
     */
    public static Map<String, Method> methods;
    /**
     * 存储bean对象id和对应的请求路径uri的映射关系 key->uri， value->bean对象id
     */
    private static Map<String, String> urlIdMaps;

    public XmlBeanFactory() {
        // 初始化
        initBeans();
    }

    /**
     * 初始化
     * @param conf 表示要解析的配置文件
     */
    public XmlBeanFactory(String conf) {
        try {
            //获取指定的xml配置文件流
            InputStream is = XmlBeanFactory.class.getClassLoader().getResourceAsStream(conf);
            //解析xml文件
            XmlBean.load(is);
            initBeans();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 1.解析指定的请求路径注解，获取对应方法
     * 2.解析xml文件，获取实例对象，将解析的实例存储到Map<id,bean>
     * 3.将url映射到指定的实例id
     */
    public void initBeans() {
        try {
            //解析@RequestMapping注解
            methods = ParseAnnotation.parseRequestMapping();
            //将解析的实例存储到Map<id,bean>
            beans = XmlBean.initBeans();
            //uri->id映射配置
            urlIdMaps = ParseAnnotation.parseUrlMappingInstance(methods, beans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据uri获取对应的实例
     * @param uri 请求入参
     * @return uri对应的实例对象
     */
    @Override
    public Object getUrlBean(String uri) {
        String id = urlIdMaps.get(uri);
        if (StringUtils.isNotEmpty(id)) {
            return getBean(id);
        }
        return null;
    }

    /**
     * 根据ID获取实例
     * @param id bean对象id
     * @return 实例对象
     */
    @Override
    public Object getBean(String id) {
        return beans.get(id);
    }

    public static void main(String[] args) {
        BeanFactory beanFactory = new XmlBeanFactory("spring.xml");
        AccountDao accountDao = (AccountDao) beanFactory.getBean("accountDao");
        accountDao.one();
    }
}
