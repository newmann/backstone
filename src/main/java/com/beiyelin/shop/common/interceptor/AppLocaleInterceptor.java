package com.beiyelin.shop.common.interceptor;

import com.beiyelin.shop.common.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by newmann on 2017/4/1.
 */
public class AppLocaleInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private MessageService messageService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //将当前的RequestContext设置给MessageService
        messageService.setCurrentRequestContext(new RequestContext(request));

        return super.preHandle(request, response, handler);
    }
}
