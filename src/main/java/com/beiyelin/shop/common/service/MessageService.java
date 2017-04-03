package com.beiyelin.shop.common.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

import java.util.Locale;

/**
 * Created by newmann on 2017/4/1.
 * 统一处理i18n的消息
 */
@Service
@Data
public class MessageService {
    /**
     * 获取请求的类
     */
    private RequestContext currentRequestContext;
    /**
     * i18n
     * 根据语言选项获取消息
     */
    public String getMessage(String code,Object[] args){
        return currentRequestContext.getMessage(code,args,getDefaultMessage());
    }

    public String getMessage(String code){
        return currentRequestContext.getMessage(code,getDefaultMessage());
    }

    private String getDefaultMessage(){
        String defaultValue;

        if (currentRequestContext.getLocale().getDisplayLanguage().equals(Locale.CHINA.getDisplayName())) {
            defaultValue = "该消息还没有配置。";
        }else{
            //return en_US
            defaultValue = "This message is not defined.";
        }
        return defaultValue;
    }

}
