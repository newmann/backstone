package com.beiyelin.shop.modules.sys.utils;

import com.beiyelin.shop.common.utils.RamdomUtils;
import com.beiyelin.shop.common.utils.SmsClient;
import com.beiyelin.shop.common.utils.SpringContextHolder;
import com.beiyelin.shop.common.utils.ValidateUtils;
import com.beiyelin.shop.modules.sys.entity.AppSms;
import com.beiyelin.shop.modules.sys.service.AppSmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 短信工具类
 * @author Tony Wong
 * @version 2014-11-7
 */
public class AppSmsUtils {

    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger("AppSmsUtils");

	private static AppSmsService appSmsService = SpringContextHolder.getBean(AppSmsService.class);
	
	/**
	 * 发送用户注册的手机验证码, 过期时间为30分钟
	 * @return
	 */
	public static boolean sendRegisterCode(String mobile) {
        if (ValidateUtils.isMobile(mobile)) {
            String code = RamdomUtils.genRegisterCode();
            String msg = "验证码为 " + code + "，恭喜，您正在注册Beiyelin，请填写验证码并完成注册。（Beiyelin客服绝不会索取此验证码，请勿告知他人）";

            try {
                SmsClient smsClient = new SmsClient();
                String result = smsClient.sendSms(mobile, msg, "", "");

                logger.debug("发送手机({})验证码返回结果: {}", mobile, result);

                Date date = new Date();
                date.setTime(date.getTime() + 1800000);
                AppSms appSms = new AppSms();
                appSms.setMobile(mobile);
                appSms.setType(AppSms.TYPE_REGISTER);
                appSms.setMsg(msg);
                appSms.setExpiredDate(date);
                appSms.setSyncReturnResult(result);
                appSms.setCode(code);
                appSmsService.save(appSms);
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
            String msg = "验证码为 " + code + "，您正在重置密码，请填写验证码。（Beiyelin客服绝不会索取此验证码，请勿告知他人）";

            try {
                SmsClient smsClient = new SmsClient();
                String result = smsClient.sendSms(mobile, msg, "", "");

                logger.debug("发送手机({})验证码返回结果: {}", mobile, result);

                Date date = new Date();
                date.setTime(date.getTime() + 1800000);
                AppSms appSms = new AppSms();
                appSms.setMobile(mobile);
                appSms.setType(AppSms.TYPE_FORGET_PASSWORD);
                appSms.setMsg(msg);
                appSms.setExpiredDate(date);
                appSms.setSyncReturnResult(result);
                appSms.setCode(code);
                appSmsService.save(appSms);
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
     * 檢查提交的忘記密碼的驗證碼是否正確
     * @return
     */
    public static boolean checkForgetPasswordCode(String mobile,String code) {
        if (ValidateUtils.isMobile(mobile)) {
            try {
                return appSmsService.checkForgetPasswordCode(mobile,code);
            } catch (Exception e) {
                logger.debug("验证手机({})重置密码的验证码失败, Exception: ", mobile, e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }
    /**
     * 檢查提交的注冊賬戶的驗證碼是否正確
     * @return
     */
    public static boolean checkRegitserCode(String mobile,String code) {
        if (ValidateUtils.isMobile(mobile)) {
            try {
                return appSmsService.checkRegisterCode(mobile,code);
            } catch (Exception e) {
                logger.debug("验证手机({})注册的验证码失败, Exception: ", mobile, e.getMessage());
                return false;
            }
        } else {
            return false;
        }
    }

}
