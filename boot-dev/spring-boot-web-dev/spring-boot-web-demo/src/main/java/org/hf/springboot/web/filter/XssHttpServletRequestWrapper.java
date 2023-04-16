package org.hf.springboot.web.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.hf.springboot.web.utils.JsoupUtil;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p> 包装请求参数 </p >
 * @author hufei
 * @date 2023-04-11
 **/
@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final HttpServletRequest orgRequest;

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        BufferedReader br = null;
        String line;
        StringBuilder result = new StringBuilder();
        try{
            br = new BufferedReader(new InputStreamReader(orgRequest.getInputStream()));
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
        }catch (Exception e){
            log.error("Xss处理异常，原因：", e);
        }finally {
            IOUtils.closeQuietly(br);
        }
        result = new StringBuilder(clean(result.toString()));
        return new WrappedServletInputStream(new ByteArrayInputStream(result.toString().getBytes()));
    }

    @Override
    public String getParameter(String name) {
        if (("content".equals(name) || name.endsWith("WithHtml"))) {
            return super.getParameter(name);
        }
        name = clean(name);
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            value = clean(value);
        }
        return value;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = super.getParameterMap();
        // 返回值Map
        Map<String, String[]> returnMap = new HashMap<>();
        Iterator<Map.Entry<String, String[]>> entries = map.entrySet().iterator();
        Map.Entry<String, String[]> entry;
        String name;
        while (entries.hasNext()) {
            entry = entries.next();
            name = entry.getKey();
            String[] valueArray = entry.getValue();
            if (valueArray != null) {
                for (int i = 0; i < valueArray.length; i++) {
                    valueArray[i] = clean(valueArray[i]);
                }
            }
            returnMap.put(name, valueArray);
        }
        return returnMap;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] arr = super.getParameterValues(name);
        if (arr != null) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = clean(arr[i]);
            }
        }
        return arr;
    }

    /**
     * get org request
     *
     * @return HttpServletRequest
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * wapper request
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest req) {
        if (req instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) req).getOrgRequest();
        }
        return req;
    }

    public String clean(String content) {
        if (StringUtils.isNotBlank(content)) {
            return JsoupUtil.clean(content);
        }
        return content;
    }

    private static class WrappedServletInputStream extends ServletInputStream {

        public void setStream(InputStream stream) {
            this.stream = stream;
        }

        private InputStream stream;

        public WrappedServletInputStream(InputStream stream) {
            this.stream = stream;
        }

        @Override
        public int read() throws IOException {
            return stream.read();
        }

        @Override
        public boolean isFinished() {
            return true;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }
}