package com.beiyelin.shop.common.LocaleResolver;

import com.beiyelin.shop.common.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Created by newmann on 2017/4/1.
 */
@Slf4j
public class AppLocaleResolver extends AcceptHeaderLocaleResolver {
    @Autowired
    private MessageService messageService;

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        HttpSession session=request.getSession();
        Locale locale=(Locale)session.getAttribute("locale");
        log.debug("resolveLocale:"+locale.toString());
        //将RequestContext设置给MessageService
        messageService.setCurrentRequestContext(new RequestContext(request));

        if (locale==null){
            session.setAttribute("locale",request.getLocale());
            return request.getLocale();
        }else{
            return locale;
        }
    }

    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {

        log.debug("setLocale:"+locale.toString());
        request.getSession().setAttribute("locale",locale);
    }
}
