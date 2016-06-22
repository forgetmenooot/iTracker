package com.bugztracker.web.filter;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class AuthorizationInterceptor implements HandlerInterceptor {

    private List<String> openUrls = Arrays.asList("/", "/auth/login", "/auth/register");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = (String) request.getSession().getAttribute("userId");
        boolean isLoggedIn = userId != null;
        String requestURI = request.getRequestURI();
        if (isLoggedIn) {
            if (openUrls.contains(requestURI)) {
                response.sendRedirect("/dashboard");
                return false;
            }
            return true;
        } else {
            if (openUrls.contains(requestURI)) {
                return true;
            }
            response.sendRedirect("/");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //no op
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //no op
    }
}
