package com.xcc.filter;

import com.alibaba.fastjson.JSON;
import com.xcc.common.JacksonObjectMapper;
import com.xcc.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author xcc
 * @version 1.0
 */

// 定义拦截器名称和需要拦截的路径
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //强转
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取本次请求的 URI
        String url = request.getRequestURI();

        //定义不需要处理的url
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "common/**",
                "/user/sendMsg",
                "/user/login"
        };

        //判断本次请求是否需要处理
        boolean flag = checkURL(urls, url);

        if (flag) {
            log.info("本次请求{}不需要处理",url);
            //放行
            filterChain.doFilter(request, response);
            return;
        }

        //判断是否登录
        Long username = (Long) request.getSession().getAttribute("employee");
        if (null != username) {
            log.info("用户{}已登录",username);
            //不为空==>放行
            filterChain.doFilter(request, response);
            return;
        }


        //判断移动端是否登录
        Object userId = request.getSession().getAttribute("user");
        if (null != userId) {
            log.info("用户{}已登录",userId);
            //不为空==>放行
            filterChain.doFilter(request, response);
            return;
        }


        log.info("用户未登录");
        //拦截此请求
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }


    public static boolean checkURL(String[] urls, String url) {
        for (String str : urls) {
            if (PATH_MATCHER.match(str, url)) {
                //本次请求在不需要处理的urls中==>放行
                return true;
            }
        }
        return false;//不在不需要处理的urls中==>处理
    }

}
