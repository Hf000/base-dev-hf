package org.hf.application.custom.framework.framework.process;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p> 视图渲染处理实现2 -- json输出 </p>
 *
 * @author hufei
 * @date 2022/7/17 19:26
 */
public class PrintViewHandler implements ViewHandler {

    @Override
    public void print(HttpServletResponse response, Object result) {
        try {
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(result));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
