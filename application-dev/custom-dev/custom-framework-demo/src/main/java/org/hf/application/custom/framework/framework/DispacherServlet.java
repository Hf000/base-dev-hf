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
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:26
*/
public class DispacherServlet extends BaseInit {

    private View view;

    /***
     * 拦截用户所有请求
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取uri
        String uri = req.getRequestURI();

        //反射调用
        Object result = invoke(uri);
        if(result!=null){
            //响应结果给用户
            view = new ViewAdapter();
            view.render(req,resp,result);
        }
    }

    //执行反射调用
    public Object invoke(String uri){
        try {
            //从methods中获取指定的方法
            Method method = XmlBeanFactory.methods.get(uri);
            if(method!=null){
                //执行反射调用
                //Class<?> clazz = method.getDeclaringClass(); //获取方法所在类
                //return method.invoke(clazz.newInstance());

                //通过工厂获取实例
                Object instance = beanFactory.getUrlBean(uri);
                return method.invoke(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
