package org.hf.application.custom.framework.framework;

import org.hf.application.custom.framework.framework.factory.BeanFactory;
import org.hf.application.custom.framework.framework.factory.XmlBeanFactory;
import org.hf.application.custom.framework.framework.parse.ParseFile;
import org.hf.application.custom.framework.framework.parse.ParseXml;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.InputStream;

/**
 * <p> 请求初始化 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:25
 */
public class BaseInit extends HttpServlet {

    private static final long serialVersionUID = 5309020125462564726L;

    /**
     * 加载文件的解析对象
     */
    private ParseFile parseFile;

    /**
     * bean工厂对象
     */
    public BeanFactory beanFactory;

    /**
     * 初始化
     * @param config 入参
     */
    @Override
    public void init(ServletConfig config) {
        try {
            //获取要解析的文件
            String conf = config.getInitParameter("contextLocation");
            //通知ParseFile加载文件
            if (conf != null) {
                InputStream is = BaseInit.class.getClassLoader().getResourceAsStream(conf);
                parseFile = new ParseXml();
                parseFile.load(is);
                //实例化工厂
                beanFactory = new XmlBeanFactory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
