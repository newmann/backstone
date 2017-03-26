package com.beiyelin.shop.modules.sys.service;

import com.beiyelin.shop.common.service.CrudService;
import com.beiyelin.shop.modules.sys.dao.AppSmsDao;
import com.beiyelin.shop.modules.sys.entity.AppSms;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * App短信Service
 * @author Newmann
 * @version 2017-03-23
 */
@Service
@Transactional(readOnly = true)
public class AppSmsService extends CrudService<AppSmsDao, AppSms> {
    /**
     * 验证注册用户的手机验证码
     */
    public boolean checkRegisterCode(String mobile, String code) {
        if (StringUtils.isBlank(code))
            return false;

        AppSms appSms = findLastOneBy(mobile, AppSms.TYPE_REGISTER);
        if (appSms == null)
            return false;

        if (!code.equals(appSms.getCode()))
            return false;

        Date date = new Date();
        if (appSms.getExpiredDate() == null || appSms.getExpiredDate().getTime() < date.getTime())
            return false;

        return true;
    }

    /**
     * 验证重置密码的手机验证码
     */
    public boolean checkForgetPasswordCode(String mobile, String code) {
        if (StringUtils.isBlank(code))
            return false;

        AppSms appSms = findLastOneBy(mobile, AppSms.TYPE_FORGET_PASSWORD);
        if (appSms == null)
            return false;

        if (!code.equals(appSms.getCode()))
            return false;

        Date date = new Date();
        if (appSms.getExpiredDate() == null || appSms.getExpiredDate().getTime() < date.getTime())
            return false;

        return true;
    }

    /**
     * 通过type，mobile查找最后一条验证码
     */
    public AppSms findLastOneBy(String mobile, String type) {
        if (StringUtils.isBlank(mobile) || StringUtils.isBlank(type))
            return null;

        AppSms appSms = new AppSms();
        appSms.setMobile(mobile);
        appSms.setType(type);
        return dao.findLastOneBy(appSms);
    }
}
