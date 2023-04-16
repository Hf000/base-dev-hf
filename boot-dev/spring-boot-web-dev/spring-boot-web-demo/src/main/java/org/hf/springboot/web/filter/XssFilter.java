package org.hf.springboot.web.filter;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p> XSS过滤器 </p >
 * 通过org.hf.springboot.web.config.XssConfig配置类注册
 * @author hufei
 * @date 2023-04-10
 **/
public class XssFilter implements Filter {
    /**
     * 用于接收配置中的参数，决定这个过滤器是否开启
     */
    private static boolean IS_INCLUDE_RICH_TEXT = false;
    /**
     * 用于接收配置中的参数，决定哪些是不需要过滤的url（在这里，也可以修改handleExcludeURL（）方法中相应的代码，使其变更为只需要过滤的url）
     */
    public List<String> excludes = new ArrayList<>();

    /**
     * 过滤器初始化，从配置类中获取参数，用于初始化两个参数（1.是否开启，2.排除指定的url集合）
     */
    @Override
    public void init(FilterConfig args) throws ServletException {
        String isIncludeRichText = args.getInitParameter("isIncludeRichText");
        if (StringUtils.isNotBlank(isIncludeRichText)) {
            IS_INCLUDE_RICH_TEXT = BooleanUtils.toBoolean(isIncludeRichText);
        }
        String temp = args.getInitParameter("excludes");
        if (temp == null) {
            return;
        }
        excludes.addAll(Arrays.stream(temp.split(",")).collect(Collectors.toList()));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeUrl(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        chain.doFilter(xssRequest, response);
    }

    /**
     * 此方法是决定对当前url是否执行过滤，
     * 在这里没有使用请求方法（post/put）来匹配，因为在本项目中使用url匹配更适合（因为get和其他请求方式也需要进行过滤），如果你有兴趣可以把这个方法更改为匹配请求方法进行过滤
     * @param request   请求对象
     * @param response  响应对象
     * @return true-不需要进行xss过滤, false-需要进行xss过滤
     */
    private boolean handleExcludeUrl(HttpServletRequest request, HttpServletResponse response) {
        if ((excludes == null || excludes.isEmpty()) && IS_INCLUDE_RICH_TEXT) {
            return false;
        }
        String url = request.getServletPath();
        for (String pattern : excludes) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(url);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
    }
}