package com.proxsoftware.webapp.adapters;

import com.proxsoftware.webapp.service.AccountServiceIml1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class ViewInterceptorAdapter extends HandlerInterceptorAdapter {

    @Autowired
    AccountServiceIml1 userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null && !modelAndView.getViewName().contains("error")) {
            modelAndView.addObject("g_user", userService.getLoggedInUser());
        }
    }
}
