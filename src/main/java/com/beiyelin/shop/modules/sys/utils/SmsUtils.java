package com.beiyelin.shop.modules.sys.utils;

import com.beiyelin.shop.common.utils.RamdomUtils;
import com.beiyelin.shop.common.utils.SmsClient;
import com.beiyelin.shop.common.utils.SpringContextHolder;
import com.beiyelin.shop.common.utils.ValidateUtils;
import com.beiyelin.shop.modules.sys.entity.Sms;
import com.beiyelin.shop.modules.sys.service.SmsService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 短信工具类
 * @author Tony Wong
 * @version 2014-11-7
 */
public class SmsUtils {

    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger("SmsUtils");

	private static SmsService smsService = SpringContextHolder.getBean(SmsService.class);
	
	/**
	 * 发送用户注册的手机验证码, 过期时间为30分钟
	 * @return
	 */
	public static boolean sendRegisterCode(String mobile) {
        if (ValidateUtils.isMobile(mobile)) {
            String code = RamdomUtils.genRegisterCode();
            String msg = "验证码为 " + code + "，恭喜，您正在注册月光茶人，请填写验证码并完成注册。（月光茶人客服绝不会索取此验证码，请勿告知他人）";

            try {
                SmsClient smsClient = new SmsClient();
                String result = smsClient.sendSms(mobile, msg, "", "");

                logger.debug("发送手机({})验证码返回结果: {}", mobile, result);

                Date date = new Date();
                date.setTime(date.getTime() + 1800000);
                Sms sms = new Sms();
                sms.setMobile(mobile);
                sms.setType(Sms.TYPE_REGISTER);
                sms.setMsg(msg);
                sms.setExpiredDate(date);
                sms.setSyncReturnResult(result);
                sms.setCode(code);
                smsService.save(sms);
                return true;
            } catch (Exception e) {
                logger.debug("发送手机({})验证码失败, Exception: ", mobile, e.getMessage());
                return false;
            }
        } else {
            return false;
        }
	}

    /**
     * 发送重置密码的手机验证码, 过期时间为30分钟
     * @return
     */
    public static boolean sendForgetPasswordCode(String mobile) {
        if (ValidateUtils.isMobile(mobile)) {
            String code = RamdomUtils.genRegisterCode();
            String msg = "验证码为 " + code + "，您正在重置密码，请填写验证码。（月光茶人客服绝不会索取此验证码，请勿告知他人）";

            try {
                SmsClient smsClient = new SmsClient();
                String result = smsClient.sendSms(mobile, msg, "", "");

                logger.debug("发送手机({})验证码返回结果: {}", mobile, result);

                Date date = new Date();
                date.setTime(date.getTime() + 1800000);
                Sms sms = new Sms();
                sms.setMobile(mobile);
                sms.setType(Sms.TYPE_FORGET_PASSWORD);
                sms.setMsg(msg);
                sms.setExpiredDate(date);
                sms.setSyncReturnResult(result);
                sms.setCode(code);
                smsService.save(sms);
                return true;
            } catch (Exception e) {
                logger.debug("发送手机({})验证码失败, Exception: ", mobile, e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

}
