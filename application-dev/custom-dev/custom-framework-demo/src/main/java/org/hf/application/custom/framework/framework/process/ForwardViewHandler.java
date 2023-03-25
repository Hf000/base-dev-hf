package org.hf.application.custom.framework.framework.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p> 视图渲染处理实现1 -- 转发 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:23
 */
public class ForwardViewHandler implements ViewHandler {

    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, Object result) {
        try {
            request.getRequestDispatcher(result.toString()).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
