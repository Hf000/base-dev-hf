package org.hf.application.custom.framework.framework;

import org.hf.application.custom.framework.framework.factory.XmlBeanFactory;
import org.hf.application.custom.framework.framework.process.View;
import org.hf.application.custom.framework.framework.process.ViewAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * <p> Controller层请求分发及响应控制器 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:26
 */
public class DispatcherServlet extends BaseInit {

    private static final long serialVersionUID = 7264647914169290652L;
    /**
     * 视图渲染对象
     */
    private View view;

    /**
     * 拦截用户所有请求
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        //获取uri
        String uri = req.getRequestURI();
        //反射调用
        Object result = invoke(uri);
        if (result != null) {
            //响应结果给用户
            view = new ViewAdapter();
            view.render(req, resp, result);
        }
    }

    /**
     * 执行uri对应方法的调用
     * @param uri 请求uri
     * @return 响应结果
     */
    public Object invoke(String uri) {
        try {
            //从methods中获取指定的方法
            Method method = XmlBeanFactory.methods.get(uri);
            if (method != null) {
                //通过工厂获取实例
                Object instance = beanFactory.getUrlBean(uri);
                // 执行实例的方法
                return method.invoke(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}