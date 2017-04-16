/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.api;

import com.beiyelin.shop.common.beanvalidator.BeanValidators;
import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.service.MessageService;
import com.beiyelin.shop.common.utils.DateUtils;
import com.beiyelin.shop.modules.sys.entity.AppAdmin;
import com.beiyelin.shop.modules.sys.entity.Person;
import com.beiyelin.shop.modules.sys.service.AppAdminService;
import com.beiyelin.shop.modules.sys.service.AppLoginService;
import com.beiyelin.shop.modules.sys.service.PersonService;
import com.beiyelin.shop.modules.sys.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * 系统管理员控制器支持类
 * @author Newmann
 * @version 2017-04-09
 */
@Slf4j
public abstract class ApiAppAdminBaseController {


	@Autowired
	protected AppAdminService appAdminService;

	@Autowired
	protected AppLoginService appLoginService;

	@Autowired
	protected MessageService messageService;


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
