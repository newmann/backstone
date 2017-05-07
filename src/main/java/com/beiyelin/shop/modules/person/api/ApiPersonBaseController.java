/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.person.api;

import com.beiyelin.shop.common.beanvalidator.BeanValidators;
import com.beiyelin.shop.common.service.MessageService;
import com.beiyelin.shop.common.utils.DateUtils;
import com.beiyelin.shop.modules.application.service.AppLoginService;
import com.beiyelin.shop.modules.person.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * API控制器支持类
 * @author Newmann
 * @version 2017-03-27
 */
@Slf4j
public abstract class ApiPersonBaseController {


    //总是提交客户端类型参数，标明是什么类型的客户端
    protected final String kTerminalTypeName = "_terminal-type";
    protected final String kTerminalTypeValue_ios = "ios";
    protected final String kTerminalTypeValue_android = "android";
    protected final String kTerminalTypeName_apicloud = "apicloud";



    /**
     * APP授权码
     */
    protected final String appAuthToken = "newmamm-d8exd2ae8_0a218t3c7a3a_f9g4af7wd9bb-byl";



	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	@Autowired
	protected PersonService personService;

	@Autowired
	protected AppLoginService appLoginService;

	@Autowired
	protected MessageService messageService;

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


}
