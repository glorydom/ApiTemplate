package com.zheng.upms.server.filter;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.servlet.SimpleCookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MySimpleCookie extends SimpleCookie {

    @Override
    public String readValue(HttpServletRequest request, HttpServletResponse ignored) {
        String cookie = request.getParameter("chqsCookie");

        if(StringUtils.isNotBlank(cookie)){
            return cookie;
        }else{
            return super.readValue(request, ignored);
        }

    }

}
