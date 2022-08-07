package org.hf.application.custom.framework.framework.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:24
*/
public class ViewAdapter implements View {

    private ViewHandler viewHandler;

    //渲染实现
    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, Object result) {
        if(result instanceof String){
            //转发
            viewHandler = new ForwardViewHandler();
            viewHandler.forward(request,response,result);
        }else{
            viewHandler = new PrintViewHandler();
            viewHandler.print(response,result);
        }
    }
}
