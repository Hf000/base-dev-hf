package org.hf.application.custom.framework.framework.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hf.application.custom.framework.pojo.bo.MapNode;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> xml文件解析bean对象处理 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:25
 */
public class XmlBean {

    /**
     * 文档对象
     */
    private static Document document;

    /**
     * 解析的各个bean节点数据
     */
    private static List<MapNode> mapNodeList;

    /**
     * 解析xml文件的before节点
     * @return before节点元素对应的属性信息集合
     */
    public static Map<String, String> before() {
        List<?> packageList = document.selectNodes("//before");
        if (packageList != null && packageList.size() > 0) {
            Object obj = packageList.get(0);
            if (obj instanceof Element) {
                Element element = (Element) obj;
                Map<String, String> aopMap = new HashMap<>();
                aopMap.put("package", element.attributeValue("package"));
                aopMap.put("ref", element.attributeValue("ref"));
                aopMap.put("method", element.attributeValue("method"));
                return aopMap;
            }
        }
        return null;
    }

    /**
     * 解析base-package节点
     * @return 返回指定节点对应的包属性的值（包名称）
     */
    public static String scanner() {
        //如果文档对象为空则返回指定的包名称
        String packageName = "org.hf.application.custom.framework.controller";
        if (document == null) {
            return packageName;
        }
        List<?> packageList = document.selectNodes("//component-scan");
        if (packageList == null || packageList.size() == 0) {
            return packageName;
        }
        Object obj = packageList.get(0);
        if (obj instanceof Element) {
            Element element = (Element) obj;
            return element.attributeValue("base-package");
        }
        return packageName;
    }

    /**
     * 加载配置文件，并解析Bean
     * @param is 指定文件的输入流
     * @throws Exception 异常
     */
    public static void load(InputStream is) throws Exception {
        //1. 创建SaxReader对象
        SAXReader saxReader = new SAXReader();
        //防止XXE攻击
        saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        //2. 读取配置文件 获得document对象
        XmlBean.document = saxReader.read(is);
    }

    /**
     * 初始化bean，创建文档中所有对象的实例
     * @return Map<String, Object> bean的id和实例化对象的映射集合
     */
    public static Map<String, Object> initBeans() {
        //解析并创建实例
        return newInstance(getMapNodeList());
    }

    /**
     * 解析所有bean节点构造成节点对象信息数据
     * @return List<MapNode> 构造好的节点对象集合
     */
    public static List<MapNode> getMapNodeList() {
        //如果此时XmlBean.mapNodeList不为空，则直接返回
        if (XmlBean.mapNodeList != null && XmlBean.mapNodeList.size() > 0) {
            return XmlBean.mapNodeList;
        }
        //获得所有的bean标签对象List
        List<?> beanList = document.selectNodes("//bean");
        if (beanList == null || beanList.size() == 0) {
            return new ArrayList<>();
        }
        //遍历获取所有bean节点的所有属性信息
        List<MapNode> mapNodeList = new ArrayList<>();
        for (Object object : beanList) {
            if (!(object instanceof Element)) {
                continue;
            }
            Element element = (Element) object;
            MapNode mapNode = new MapNode();
            //获得id的属性值作为map的key
            String id = element.attributeValue("id");
            //获得class的属性值,反射得到对象作为map的value
            String className = element.attributeValue("class");
            mapNode.setId(id);
            mapNode.setClassname(className);
            //解析property
            List<?> property = element.elements("property");
            if (property != null) {
                Map<String, String> propertyMap = new HashMap<>();
                for (Object propertyObject : property) {
                    if (propertyObject instanceof Element) {
                        Element propertyElement = (Element) propertyObject;
                        propertyMap.put(propertyElement.attributeValue("name"), propertyElement.attributeValue("ref"));
                    }
                }
                mapNode.setPropertyMap(propertyMap);
            }
            System.out.println(mapNode);
            mapNodeList.add(mapNode);
        }
        XmlBean.mapNodeList = mapNodeList;
        return XmlBean.mapNodeList;
    }

    /**
     * 实例化对象, 并做依赖注入
     * @param mapNodeList 解析bean节点得到的节点属性信息集合
     * @return 实例化对象集合 key->实例bean的id，value->实例化后的bean对象
     */
    public static Map<String, Object> newInstance(List<MapNode> mapNodeList) {
        try {
            //所有实例
            Map<String, Object> beans = new HashMap<>();
            //第1次实例化：将每个对象都创建一个实例
            for (MapNode mapNode : mapNodeList) {
                //唯一id
                String id = mapNode.getId();
                //classname
                String classname = mapNode.getClassname();
                beans.put(id, Class.forName(classname).newInstance());
            }
            //第2次没有依赖注入的筛选,这里做第二次依赖注入的原因：是为了防止引用的实例在第一次依赖注入的时候还没有实例化，记录哪些需要进行依赖注入
            Map<String, Object> realBeans = new HashMap<>();
            for (MapNode mapNode : mapNodeList) {
                //没有依赖注入的直接存入到realBeans中
                if (mapNode.getPropertyMap() == null || mapNode.getPropertyMap().size() == 0) {
                    realBeans.put(mapNode.getId(), beans.get(mapNode.getId()));
                    beans.remove(mapNode.getId());
                }
            }
            //第3次依赖注入实现 进行依赖注入
            int size = beans.size();
            while (size > 0) {
                //在realBeans中有的对象实现依赖注入
                for (MapNode mapNode : mapNodeList) {
                    //获取实例
                    Object obj = beans.get(mapNode.getId());
                    if (obj != null) {
                        for (Map.Entry<String, String> entry : mapNode.getPropertyMap().entrySet()) {
                            String name = entry.getKey();
                            String ref = entry.getValue();
                            //如果ref在realBeans中有，则获取出来，并赋值
                            Object refObj = realBeans.get(ref);
                            if (refObj != null) {
                                Field field = obj.getClass().getDeclaredField(name);
                                field.setAccessible(true);
                                field.set(obj, refObj);
                                //将o添加到realBeans,并从beans中移除
                                beans.remove(mapNode.getId());
                                realBeans.put(mapNode.getId(), obj);
                            }
                        }
                    }
                }
                size = beans.size();
            }
            return realBeans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将对象替换成代理对象
     * @param beans 所有实例化的bean对象集合，key->id，value->bean
     * @param id    指定的实例化对象bean的id
     * @param proxyInstance 代理对象
     * @throws Exception 异常
     */
    public static void replaceProxy(Map<String, Object> beans, String id, Object proxyInstance) throws Exception {
        //将实例中所有的引用对象替换成代理
        List<MapNode> mapNodeList = XmlBean.getMapNodeList();
        for (MapNode mapNode : mapNodeList) {
            //所有键值对
            Map<String, String> map = mapNode.getPropertyMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String name = entry.getKey();
                String ref = entry.getValue();
                if (ref.equals(id)) {
                    //将该对象的值替换成代理对象
                    Object o = beans.get(mapNode.getId());
                    Field field = o.getClass().getDeclaredField(name);
                    field.setAccessible(true);
                    field.set(o, proxyInstance);
                    beans.put(mapNode.getId(), o);
                }
            }
        }
    }
}