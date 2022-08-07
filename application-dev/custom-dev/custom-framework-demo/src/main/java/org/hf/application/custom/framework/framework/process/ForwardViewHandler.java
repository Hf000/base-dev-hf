package org.hf.application.custom.framework.framework.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:23
*/
public class ForwardViewHandler implements ViewHandler {

    //转发
    @Override
    public void forward(HttpServletRequest request, HttpServletResponse response, Object result) {
        try {
            request.getRequestDispatcher(result.toString()).forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
