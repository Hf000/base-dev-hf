package org.hf.application.custom.framework.framework.factory;

import org.apache.commons.lang3.StringUtils;
import org.hf.application.custom.framework.dao.AccountDao;
import org.hf.application.custom.framework.framework.util.ParseAnnotation;
import org.hf.application.custom.framework.framework.util.XmlBean;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:22
*/
public class XmlBeanFactory implements BeanFactory {

    //实例存储到Map<id,instance>
    private static Map<String,Object> beans;
    //所有方法
    public static Map<String,Method> methods;

    //Map uri->id
    private static Map<String,String> urlIdMaps;


    public XmlBeanFactory() {
        initBeans();
    }

    //conf表示要解析的配置文件
    public XmlBeanFactory(String conf) {
        try {
            InputStream is = XmlBeanFactory.class.getClassLoader().getResourceAsStream(conf);
            XmlBean.load(is);
            initBeans();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****
     * 1、解析spring.xml
     * 2、将解析的实例存储到Map<id,instance>
     */
    public void initBeans(){
        try {
            //解析@RequestMapping注解
            methods = ParseAnnotation.parseRequestMapping();
            //将解析的实例存储到Map<id,instance>
            beans = XmlBean.initBeans();
            //uri->id映射配置
            urlIdMaps = ParseAnnotation.parseUrlMappingInstance(methods, beans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 根据uri获取对应的实例
     * @param uri
     * @return
     */
    @Override
    public Object getUrlBean(String uri) {
        String id = urlIdMaps.get(uri);
        if(StringUtils.isNotEmpty(id)){
            return getBean(id);
        }
        return null;
    }

    /***
     * 根据ID获取实例
     * @param id
     * @return
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
