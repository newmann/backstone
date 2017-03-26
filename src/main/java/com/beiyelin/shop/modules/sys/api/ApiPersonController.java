/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.api;

import com.beiyelin.shop.common.bean.ApiResponseData;
import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.security.authority.annotation.PermissionControl;
import com.beiyelin.shop.common.utils.*;
import com.beiyelin.shop.modules.app.web.AppBaseController;
import com.beiyelin.shop.modules.shop.entity.Cart;
import com.beiyelin.shop.modules.shop.entity.CartItem;
import com.beiyelin.shop.modules.shop.service.CartItemService;
import com.beiyelin.shop.modules.shop.service.CartService;
import com.beiyelin.shop.modules.shop.service.CouponUserService;
import com.beiyelin.shop.modules.sys.entity.Person;
import com.beiyelin.shop.modules.sys.security.FormAuthenticationFilter;
import com.beiyelin.shop.modules.sys.service.*;
import com.beiyelin.shop.modules.sys.utils.SmsUtils;
import com.google.common.collect.Maps;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 登录Controller
 * 注意：因为app端没有用cookie来做session，shiro的登出（SecurityUtils.getSubject().logout();）不起作用，
 *      当改密码再登录时还是会引用之前的密码而导致登录失败。
 * 登录判断：
 *      用户登录要用 person.loginName + person.password, 登录后生成新的 person.appLoginToken
 *      判断用户是否登录的session：person.id + person.appLoginToken
 * @author Tony Wong
 * @version 2015-6-13
 */
@RestController
@RequestMapping("admin/api/person")
public class ApiPersonController extends AppBaseController {
    private static final String LOGIN_USERNAME_CAPTION="userName";
    private static final String LOGIN_PASSWORD_CAPTION="password";
    private static final String LOGIN_CHECK_CODE_CAPTION="checkCode";
    private static final String LOGIN_MOBILE_CAPTION="mobile";

	@Autowired
    PersonService personService;

    @Autowired
    AppSmsService appSmsService;


    @Autowired
    AppLoginService appLoginService;
    
//	@RequestMapping(value = "")
//	public String index() {
//		return "modules/app/person/index";
//	}
    @PermissionControl("person:save-person")
    @RequestMapping("/save-person")
    public ApiResponseData savePerson(HttpServletRequest request,@RequestBody Person person, HttpServletResponse response) {

//        if (!isPersonLoggedIn(request)) {
//            return renderNotLoggedIn(response);
//        }

        try {
//            Person person = JsonMapper.getInstance().fromJson(request.getParameter("person"),Person.class);
            if (person==null){
                throw new Exception("没有提交任何信息，不能保存！");
            }

            beanValidator(person);

            personService.save(person);

            responseData.setSuccessMessage("保存个人信息成功。");

        } catch (Exception ex){
            responseData.setErrorMessage(ex.getMessage());
        }

        return responseData;
    }


    @RequestMapping("/get-person")
    public ApiResponseData getPerson(HttpServletRequest request, HttpServletResponse response) {

        try {
            String personId = request.getHeader(Global.REQUEST_USER_CAPTION);
            Person person = personService.get(personId);
            responseData.pushData("person", person);
            responseData.setSuccessMessage("用户信息");
        } catch (Exception ex){
            responseData.setErrorMessage(ex.getMessage());
        }

        return responseData;
    }

	/**
	 * 自建会话系统给app用
     * 判断用户是否登录的条件：person.id + person.app_login_token
     * 如果用户登录则重新生成app_login_token，实现单点登录，手机掉了只要再次登录，掉了的手机就不能登录了
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ApiResponseData loginPost(HttpServletRequest request, HttpServletResponse response) {
//
//        if (!isValidApp(request)) {
//            responseData.setInvalidApp();
//            return responseData;
//        }


		String loginName = StrUtils.clean(request.getParameter(LOGIN_USERNAME_CAPTION));
		String password = StrUtils.clean(request.getParameter(LOGIN_PASSWORD_CAPTION));

		//不能为空
		if (StrUtils.isBlank(loginName) || StrUtils.isBlank(password)) {
            responseData.setErrorMessage("登录名和密码不能为空");
            return responseData;
		}

		//登录


        Person person = _loginCheck(loginName, password);

        if (person == null) {
            responseData.setErrorMessage("用户名或密码错误");
            return responseData;

        }


        responseData.pushData("isLoggedIn", true);
        Map<String, Object> oUser = person.toSimpleObj();
        responseData.pushData("person", oUser);

        return responseData;

	}



    /**
     * 注册 - 提交手机号码
     */
    @RequestMapping(value = "/register-step1-post")
    public ApiResponseData registerStep1(HttpServletRequest request, HttpServletResponse response) {
//        if (!isValidApp(request)) {
//            responseData.setInvalidApp();
//            return responseData;
//        }

        String userName = StrUtils.clean(request.getParameter(LOGIN_USERNAME_CAPTION));

        if (!ValidateUtils.isMobile(userName)) {
            responseData.setErrorMessage(ValidateUtils.getErrMsg());
            return responseData;
        }

        Person person = personService.getByLoginName2(userName);
        if (person != null && StrUtils.isNotBlank(person.getId())) {
            responseData.setErrorMessage("电话号码已存在");
        } else {
            //发送手机验证码

            SmsUtils.sendRegisterCode(userName);
            responseData.setSuccessMessage("发送手机验证码成功，请查阅。");
        }

        return responseData;
    }

	/**
	 * 注册 - 提交手机号码、密码
	 */
	@RequestMapping(value = "/register-step2-post")
	public ApiResponseData registerStep2(HttpServletRequest request, HttpServletResponse response) {
//        if (!isValidApp(request)) {
//            responseData.setInvalidApp();
//            return responseData;
//        }

        String userName = StrUtils.clean(request.getParameter(LOGIN_USERNAME_CAPTION));
        String password = StrUtils.clean(request.getParameter(LOGIN_PASSWORD_CAPTION));
        

		if (!(ValidateUtils.isMobile(userName) && ValidateUtils.isPassword(password))) {
			responseData.setErrorMessage(ValidateUtils.getErrMsg());
			return responseData;
		}

        Person person = personService.getByLoginName2(userName);
        if (person != null && StrUtils.isNotBlank(person.getId())) {
            responseData.setErrorMessage("电话号码已存在");
        }else{
            responseData.setSuccessMessage("正确提交手机号码和密码");
        }

		return responseData;
	}

	/**
	 * 注册 - 提交手机号码、密码、验证码
	 */
	@RequestMapping(value = "/register-step3-post", method = RequestMethod.POST)
	public ApiResponseData registerStep3Post(HttpServletRequest request, HttpServletResponse response) {
//        if (!isValidApp(request)) {
//            responseData.setInvalidApp();
//            return responseData;
//        }

        String userName = StrUtils.clean(request.getParameter(LOGIN_USERNAME_CAPTION));
        String password = StrUtils.clean(request.getParameter(LOGIN_PASSWORD_CAPTION));
        String code = StrUtils.clean(request.getParameter(LOGIN_CHECK_CODE_CAPTION));

        if (!ValidateUtils.isMobile(userName) || !ValidateUtils.isPassword(password)) {
            responseData.setErrorMessage("提交的手机号码或密码不符合规则");
            return responseData;

        }

        Person u = personService.getByLoginName2(userName);
        if (u != null && StrUtils.isNotBlank(u.getId())) {
            responseData.setErrorMessage("电话号码已存在");
            return responseData;
        }

        //比较验证码
        if (appSmsService.checkRegisterCode(userName, code)) {
            //保存用户
            Person person = new Person();
            person.setLoginName(userName);
            person.setPassword(SystemService.entryptPassword(password));
            person.setMobile(userName);

            person.setRegisterFrom(Person.REGISTER_FROM_APP);
            personService.save(person);

            Person loginUser = _loginCheck(userName, password);


            responseData.pushData("person", loginUser);
            responseData.setSuccessMessage("恭喜, 您已经成功注册了");
        } else {
            responseData.setErrorMessage("请输入正确的验证码");
        }

        return responseData;
	}

    /**
     * 重置密码 - 提交手机号码
     */
    @RequestMapping("/forget-password-step1-post")
    public ApiResponseData forgetPasswordStep1Post(HttpServletRequest request, HttpServletResponse response) {
//        if (!isValidApp(request)) {
//            return renderInvalidApp(response);
//        }


        String mobile = StrUtils.clean(request.getParameter(LOGIN_MOBILE_CAPTION));

        if (!ValidateUtils.isMobile(mobile)) {
            responseData.setErrorMessage(ValidateUtils.getErrMsg());
            return responseData;
        }

        Person person = personService.getByMobile(mobile);
        if (person == null) {
            responseData.setErrorMessage("电话号码不存在");
            return responseData;

        }

        //发送重置密码的验证码
        SmsUtils.sendForgetPasswordCode(mobile);

        responseData.setSuccessMessage("");
        responseData.pushData("mobile", mobile);
        return responseData;

    }

    /**
     * 重置密码 - 提交手机号码和密码
     */
    @RequestMapping("/forget-password-step2-post")
    public ApiResponseData forgetPasswordStep2Post(HttpServletRequest request, HttpServletResponse response) {
//        if (!isValidApp(request)) {
//            return renderInvalidApp(response);
//        }


        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");

        if (!ValidateUtils.isMobile(mobile)) {
            responseData.setErrorMessage(ValidateUtils.getErrMsg());
            return responseData;
        }

        if (!ValidateUtils.isPassword(mobile)) {
            responseData.setErrorMessage(ValidateUtils.getErrMsg());
            return responseData;
        }

        Person person = personService.getByMobile(mobile);
        if (person == null) {
            responseData.setErrorMessage("电话号码不存在");
            return responseData;
        }

        responseData.setSuccessMessage("");
        responseData.pushData("mobile", mobile);
        responseData.pushData("password", password);
        return responseData;
    }

    /**
     * 重置密码 - 提交手机号码、密码、验证码
     */
    @RequestMapping("/forget-password-step3-post")
    public ApiResponseData forgetPasswordStep3Post(HttpServletRequest request, HttpServletResponse response) {
//        if (!isValidApp(request)) {
//            return renderInvalidApp(response);
//        }


        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");
        String code = request.getParameter("code");

        if (!ValidateUtils.isMobile(mobile)) {
            responseData.setErrorMessage(ValidateUtils.getErrMsg());
            return responseData;
        }

        if (!ValidateUtils.isPassword(mobile)) {
            responseData.setErrorMessage(ValidateUtils.getErrMsg());
            return responseData;
        }

        Person person = personService.getByMobile(mobile);

        if (person == null) {
            responseData.setErrorMessage("电话号码不存在");
            return responseData;
        }

        //比较验证码
        if (appSmsService.checkForgetPasswordCode(mobile, code)) {
            //保存用户新密码
            person.setPassword(SystemService.entryptPassword(password));
            personService.save(person);

            //用户自动登录
//            String personId = person.getId();
//            UsernamePasswordToken token = new UsernamePasswordToken();
//            token.setUsername(person.getLoginName());
//            token.setPassword(password.toCharArray());
//            token.setRememberMe(true);
//            try {
//                SecurityUtils.getSubject().login(token);
//            }
//            catch (AuthenticationException e) {
//                logger.debug("/app/person/forget-password-step3-post throw AuthenticationException: {}", e.getMessage());
//                result = false;
//                message = "用户名或密码错误";
//                return renderString(response, result, message, data);
//            }
//            catch (Exception e) {
//                logger.debug("/app/person/forget-password-step3-post throw Exception: {}", e.getMessage());
//                result = false;
//                message = e.getMessage();
//                return renderString(response, result, message, data);
//            }
//            //更新app登录令牌
//            person.setAppLoginToken(personService.genAppLoginToken());
//            personService.updateAppLoginToken(person);
            Person loginUser = _loginCheck(mobile, password);


            //重新为客户端生成appCartCookieId
            String oAppCartCookieId = IdGen.uuid();

            responseData.setSuccessMessage("");
            responseData.pushData("person",loginUser);
            responseData.pushData("appCartCookieId", oAppCartCookieId);
            responseData.pushData("mobile", mobile);
            responseData.pushData("password", password);

        } else {
            responseData.setErrorMessage("请输入正确的验证码");
        }

        return responseData;
    }

	/**
	 * 退出
	 */
	@RequestMapping(value = "/logout")
	public ApiResponseData logout(HttpServletRequest request, HttpServletResponse response) throws IOException {

        _logout(request);

		responseData.setSuccessMessage("已成功退出");
        return responseData;
	}

    /**
     * 获得登录的用户对象
     * @return
     */
    @RequestMapping(value = "/check-login")
    public ApiResponseData checkLogin(HttpServletRequest request, HttpServletResponse response) {

        if (isLoggedIn(request)) {
            responseData.setSuccessMessage("当前用户已登录");
        } else {
            responseData.setErrorMessage("当前没有登录用户");
        }

        return responseData;
    }
	
	/**
	 * 是否是验证码登录
	 * @param userName 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isValidateCodeLogin(String userName, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(userName);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(userName, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(userName);
		}
		return loginFailNum >= 3;
	}
}
