/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.api;

import com.beiyelin.shop.common.bean.ApiResponseData;
import com.beiyelin.shop.common.beanvalidator.BeanValidators;
import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.config.ResultCode;
import com.beiyelin.shop.common.mapper.JsonMapper;
import com.beiyelin.shop.common.utils.DateUtils;
import com.beiyelin.shop.common.utils.StrUtils;
import com.beiyelin.shop.modules.sys.entity.Person;
import com.beiyelin.shop.modules.sys.entity.User;
import com.beiyelin.shop.modules.sys.service.AppLoginService;
import com.beiyelin.shop.modules.sys.service.PersonService;
import com.beiyelin.shop.modules.sys.service.SystemService;
import com.beiyelin.shop.modules.sys.service.UserService;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * API控制器支持类
 * @author Newmann
 * @version 2017-03-27
 */
public abstract class ApiBaseController {


    //总是提交客户端类型参数，标明是什么类型的客户端
    protected final String kTerminalTypeName = "_terminal-type";
    protected final String kTerminalTypeValue_ios = "ios";
    protected final String kTerminalTypeValue_android = "android";
    protected final String kTerminalTypeName_apicloud = "apicloud";


	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * APP授权码
     */
    protected final String appAuthToken = "newmamm-d8exd2ae8_0a218t3c7a3a_f9g4af7wd9bb-byl";


	/**
	*返回的Json结构
	 */
	protected ApiResponseData responseData = new ApiResponseData();

	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	@Autowired
	protected PersonService personService;

	@Autowired
	protected AppLoginService appLoginService;

	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
//	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
//		try{
//			BeanValidators.validateWithException(validator, object, groups);
//		}catch(ConstraintViolationException ex){
//			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
//			list.add(0, "数据验证失败：");
//			addMessage(model, list.toArray(new String[]{}));
//			return false;
//		}
//		return true;
//	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
//	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
//		try{
//			BeanValidators.validateWithException(validator, object, groups);
//		}catch(ConstraintViolationException ex){
//			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
//			list.add(0, "数据验证失败：");
//			addMessage(redirectAttributes, list.toArray(new String[]{}));
//			return false;
//		}
//		return true;
//	}
//
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组，不传入此参数时，同@Valid注解验证
	 * @return 验证成功：继续执行；验证失败：抛出异常跳转400页面。
	 */
	protected void beanValidator(Object object, Class<?>... groups) {
		BeanValidators.validateWithException(validator, object, groups);
	}
	

	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
//			@Override
//			public String getAsText() {
//				Object value = getValue();
//				return value != null ? DateUtils.formatDateTime((Date)value) : "";
//			}
		});
	}


    /**
     * 根据APP传过来的授权码来判断app是否有权限访问
     */
    protected boolean isValidApp(HttpServletRequest request){
        //开发开源的ios时暂时把这个验证屏蔽
        return true;
        /*
        String appAuthToken = request.getParameter("appAuthToken");
        if (StrUtils.isNotBlank(appAuthToken) && appAuthToken.equals(this.appAuthToken)) {
            return true;
        } else {
            return false;
        }
        */
    }



	/**
	 * 如果登录成功返回User，否则返回null
	 * @return
	 */
	protected Person _loginCheck(String loginName, String password){
		if (StringUtils.isBlank(loginName) || StringUtils.isBlank(password))
			return null;


		Person person = personService.getByLoginName2(loginName);

		if (person != null && SystemService.validatePassword(password, person.getPassword())) {
			//更新appLoginToken
//			String token = appLoginService.genAppLoginToken();
//			appLoginService.updateAppLoginToken(person.getId(),token);

			return person;
		}

		return null;
	}

    /**
     * 登出
     * @return
     */
    protected boolean _logout(HttpServletRequest request){
		String personId = request.getHeader(Global.REQUEST_USER_CAPTION);
		String token = request.getHeader(Global.REQUEST_TOKEN_CAPTION);
		try {
			if (StringUtils.isNotBlank(personId) && StringUtils.isNotBlank(token)) {
				appLoginService.logout(personId, token);
			}
			return true;
		}catch (Exception ex){
			return false;
		}
    }


	/**
	 * 个人是否已经登录, 由app传personId和appLoginToken过来
	 * @return
	 */
		protected boolean isLogin(HttpServletRequest request) {
			String personId = request.getHeader(Global.REQUEST_USER_CAPTION);
			String token = request.getHeader(Global.REQUEST_TOKEN_CAPTION);

			if (StringUtils.isNotBlank(personId) && StringUtils.isNotBlank(token)
					&& appLoginService.isAppLoggedIn(personId, token)) {
				return true;
			} else {
				return false;
			}
	}
}
