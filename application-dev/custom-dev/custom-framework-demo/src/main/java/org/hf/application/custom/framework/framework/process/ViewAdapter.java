package org.hf.application.custom.framework.framework.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 视图渲染适配器 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:24
 */
public class ViewAdapter implements View {

    /**
     * 视图渲染处理器
     */
    private ViewHandler viewHandler;

    @Override
    public void render(HttpServletRequest request, HttpServletResponse response, Object result) {
        if (result instanceof String) {
            //转发 如果是字符串一般是路径，所以转发
            viewHandler = new ForwardViewHandler();
            viewHandler.forward(request, response, result);
        } else {
            //回显结果
            viewHandler = new PrintViewHandler();
            viewHandler.print(response, result);
        }
    }
}
