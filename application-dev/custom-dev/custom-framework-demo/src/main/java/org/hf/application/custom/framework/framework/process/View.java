package org.hf.application.custom.framework.framework.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>  </p>
 * 决策使用何种渲染方式
 * @author hufei
 * @date 2022/7/17 19:24
*/
public interface View {

    //渲染方法
    void render(HttpServletRequest request, HttpServletResponse response, Object result);

}
