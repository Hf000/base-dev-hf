package org.hf.application.custom.framework.framework.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:25
*/
public class XmlBean {

    //文档对象
    private static Document document;

    //解析的数据
    private static List<MapNode> mapNodeList;

    /***
     * 解析before节点
     */
    public static Map<String, String> before() throws Exception {
        List<Element> packageList = document.selectNodes("//before");
        if(packageList!=null && packageList.size()>0){
            Element element = packageList.get(0);
            Map<String,String> aopMap = new HashMap<String,String>();
            aopMap.put("package",element.attributeValue("package"));
            aopMap.put("ref",element.attributeValue("ref"));
            aopMap.put("method",element.attributeValue("method"));
            return aopMap;
        }
        return null;
    }

    /***
     * 解析base-package节点
     */
    public static String scanner() throws Exception {
        if(document==null){
            return "org.hf.application.custom.framework.controller";
        }
        List<Element> packageList = document.selectNodes("//component-scan");
        if(packageList.size()==0){
            return "org.hf.application.custom.framework.controller";
        }
        if(packageList!=null && packageList.size()>0){
            Element element = packageList.get(0);
            String value = element.attributeValue("base-package");
            return value;
        }
        return null;
    }

    /***
     * 加载配置文件，并解析Bean
     * @param is
     * @return
     * @throws Exception
     */
    public static void load(InputStream is) throws Exception{
        //解析xml, 初始化beans集合
        //1. 创建SaxReader对象
        SAXReader saxReader = new SAXReader();
        //2. 读取配置文件 获得document对象
        Document document = saxReader.read(is);
        XmlBean.document = document;
    }

    /***
     * 创建文档中所有对象的实例
     * @return
     */
    public static Map<String,Object> initBeans(){
        //解析并创建实例
        //key=accountController
        //value=org.hf.application.custom.framework.controller.AccountController.newInstance()
        Map<String, Object> beans = newInstance(getMapNodeList());
        return beans;
    }

    /***
     * 解析所有MapNode节点
     */
    public static List<MapNode> getMapNodeList(){
        //如果此时XmlBean.mapNodeList不为空，则直接返回
        if(XmlBean.mapNodeList!=null && XmlBean.mapNodeList.size()>0){
            return XmlBean.mapNodeList;
        }
        //3. 获得所有的bean标签对象List
        List<Element> beanList = document.selectNodes("//bean");
        //4. 遍历
        List<MapNode> mapNodeList = new ArrayList<MapNode>();
        for (Element element : beanList) {
            MapNode mapNode = new MapNode();
            //获得id的属性值作为map的key
            String id = element.attributeValue("id");
            //获得class的属性值,反射得到对象作为map的value
            String className = element.attributeValue("class");
            mapNode.setId(id);
            mapNode.setClassname(className);

            //解析property
            List<Element> property = element.elements("property");
            if(property!=null){
                Map<String,String> propertMap = new HashMap<String,String>();
                for (Element propertyElement : property) {
                    propertMap.put(propertyElement.attributeValue("name"),propertyElement.attributeValue("ref"));
                }
                mapNode.setPropertyMap(propertMap);
            }
            System.out.println(mapNode);
            mapNodeList.add(mapNode);
        }
        XmlBean.mapNodeList =mapNodeList;
        return XmlBean.mapNodeList;
    }

    /***
     * 实例化对象
     * @param mapNodeList
     */
    public static Map<String, Object> newInstance(List<MapNode> mapNodeList){
        try {
            //所有实例
            Map<String,Object> beans = new HashMap<String,Object>();

            //第1次实例化：将每个对象都创建一个实例
            for (MapNode mapNode : mapNodeList) {
                //唯一id
                String id = mapNode.getId();
                //classname
                String classname = mapNode.getClassname();
                beans.put(id,Class.forName(classname).newInstance());
            }

            //第2次没有依赖注入的筛选
            Map<String,Object> realBeans = new HashMap<String,Object>();
            for (MapNode mapNode : mapNodeList) {
                //没有依赖注入的直接存入到realBeans中
                if(mapNode.getPropertyMap()==null || mapNode.getPropertyMap().size()==0){
                    realBeans.put(mapNode.getId(),beans.get(mapNode.getId()));
                    beans.remove(mapNode.getId());
                }
            }

            //第3次依赖注入实现
            int size=beans.size();
            while (size>0){
                //在realBeans中有的对象实现依赖注入
                for (MapNode mapNode : mapNodeList) {
                    //获取实例
                    Object o = beans.get(mapNode.getId());
                    if(o!=null){
                        for (Map.Entry<String, String> entry : mapNode.getPropertyMap().entrySet()) {
                            String name = entry.getKey();
                            String ref = entry.getValue();
                            //如果ref在realBeans中有，则获取出来，并赋值
                            Object refo = realBeans.get(ref);
                            if(refo!=null){
                                Field field = o.getClass().getDeclaredField(name);
                                field.setAccessible(true);
                                field.set(o,refo);
                                //将o添加到realBeans,并从beans中移除
                                beans.remove(mapNode.getId());
                                realBeans.put(mapNode.getId(),o);
                            }
                        }
                    }
                }
                size=beans.size();
            }

            return realBeans;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /****
     * 将对象替换成代理对象
     */
    public static Object replaceProxy(Map<String, Object> beans,String id,Object proxyInstance) throws Exception{
        //将实例中所有的引用对象替换成代理
        List<MapNode> mapNodeList = XmlBean.getMapNodeList();
        for (MapNode mapNode : mapNodeList) {
            //所有键值对
            Map<String, String> map = mapNode.getPropertyMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String name = entry.getKey();
                String ref = entry.getValue();
                if(ref.equals(id)){
                    //将该对象的值替换成代理对象
                    Object o = beans.get(mapNode.getId());
                    Field field = o.getClass().getDeclaredField(name);
                    field.setAccessible(true);
                    field.set(o,proxyInstance);
                    beans.put(mapNode.getId(),o);
                }
            }
        }
        return null;
    }
}
