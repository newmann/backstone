/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.api;

import com.beiyelin.shop.common.bean.ApiResponseData;
import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.security.authority.annotation.PermissionControl;
import com.beiyelin.shop.common.utils.*;
import com.beiyelin.shop.modules.sys.bean.LoginResponse;
import com.beiyelin.shop.modules.sys.entity.Person;


import com.beiyelin.shop.modules.sys.service.AppLoginService;
import com.beiyelin.shop.modules.sys.service.SystemService;
import com.beiyelin.shop.modules.sys.utils.AppSmsUtils;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
@Api(value="用户controller",description="个人相关操作，包括：注册账户，退出登录，修改密码，重置密码，修改个人信息。")
public class ApiPersonController extends ApiBaseController {
    private static final String LOGIN_USERNAME_CAPTION="userName";
    private static final String LOGIN_PASSWORD_CAPTION="password";
    private static final String LOGIN_CHECK_CODE_CAPTION="checkCode";
    private static final String LOGIN_MOBILE_CAPTION="mobile";

//	@Autowired
//    PersonService personService;


    
//	@RequestMapping(value = "")
//	public String index() {
//		return "modules/app/person/index";
//	}
    @PermissionControl("person:save-person")
    @RequestMapping(value = "/save-person",method = RequestMethod.POST)
    @ApiOperation(value = "修改当前登录个人信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "个人id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "person.property", value = "Person类的属性，通过form提交", required = true, paramType = "form", dataType = "object")
    })
    public ApiResponseData savePerson(HttpServletRequest request,@RequestBody Person person, HttpServletResponse response) {

//        if (!isPersonLoggedIn(request)) {
//            return renderNotLoggedIn(response);
//        }
        responseData = new ApiResponseData();
        try {
//            Person person = JsonMapper.getInstance().fromJson(request.getParameter("person"),Person.class);
            if (person==null){
//                throw new Exception("没有提交任何信息，不能保存！");
                throw new Exception(messageService.getMessage("ApiPersonController.savePerson.noPerson"));
            }

            beanValidator(person);

            personService.save(person);

            responseData.setSuccessMessage(messageService.getMessage("ApiPersonController.savePerson.success"));

        } catch (Exception ex){
            responseData.setErrorMessage(ex.getMessage());
        }

        return responseData;
    }


    @RequestMapping(value = "/get-person",method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录个人信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "个人id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "person.property", value = "Person类的属性，通过form提交", required = true, paramType = "form", dataType = "object")
    })
    public ApiResponseData getPerson(HttpServletRequest request, HttpServletResponse response) {
        responseData = new ApiResponseData();
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
    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "登录账户，现在只能用手机号", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "form", dataType = "String")
    })
    public LoginResponse login(HttpServletRequest request, HttpServletResponse response) throws Throwable {

        LoginResponse loginResponse =new LoginResponse();

        String loginName = StrUtils.clean(request.getParameter(LOGIN_USERNAME_CAPTION));
        String password = StrUtils.clean(request.getParameter(LOGIN_PASSWORD_CAPTION));

        //不能为空
        if (StrUtils.isBlank(loginName) || StrUtils.isBlank(password)) {
            throw new Exception("ApiPersonController.login.notNull");
        }

        //登录


        Person person = _loginCheck(loginName, password);

        if (person == null) {
            throw new Exception("ApiPersonController.login.nameOrPasswordError");

        }

        //生成登录令牌
        String token = appLoginService.genAppLoginToken();
        appLoginService.updateAppLoginToken(person.getId(), token);

        loginResponse.setToken(token);
        loginResponse.setPersonId(person.getId());

        return loginResponse;

    }

//	public ApiResponseData login(HttpServletRequest request, HttpServletResponse response) {
//
//        responseData = new ApiResponseData();
//        try {
//
//            String loginName = StrUtils.clean(request.getParameter(LOGIN_USERNAME_CAPTION));
//            String password = StrUtils.clean(request.getParameter(LOGIN_PASSWORD_CAPTION));
//
//            //不能为空
//            if (StrUtils.isBlank(loginName) || StrUtils.isBlank(password)) {
//                responseData.setErrorMessage(messageService.getMessage("ApiPersonController.login.notNull"));
//                return responseData;
//            }
//
//            //登录
//
//
//            Person person = _loginCheck(loginName, password);
//
//            if (person == null) {
//                responseData.setErrorMessage(messageService.getMessage("ApiPersonController.login.nameOrPasswordError"));
//                return responseData;
//
//            }
//            //生成登录令牌
//            String token = appLoginService.genAppLoginToken();
//            appLoginService.updateAppLoginToken(person.getId(), token);
//
//            responseData.setSuccessMessage(messageService.getMessage("ApiPersonController.login.success"));
//            responseData.pushData("token", token);
//            responseData.pushData("personId", person.getId());
//        } catch (Exception ex){
//            responseData.setErrorMessage(ex.toString());
//        }
//
//        return responseData;
//
//	}



    /**
     * 注册 - 提交手机号码
     *  提交的信息的name为“userName”
     *
     */
    @RequestMapping(value = "/register-get-check-code",method = RequestMethod.POST)
    @ApiOperation(value = "注册用户时获取注册码", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "登录账户，现在只能用手机号", required = true, paramType = "form", dataType = "String")

    })
    public ApiResponseData registerGetCheckCode(HttpServletRequest request, HttpServletResponse response) {
//        if (!isValidApp(request)) {
//            responseData.setInvalidApp();
//            return responseData;
//        }
        responseData = new ApiResponseData();
        String userName = StrUtils.clean(request.getParameter(LOGIN_USERNAME_CAPTION));

        if (!ValidateUtils.isMobile(userName)) {
            responseData.setErrorMessage(ValidateUtils.getErrMsg());
            return responseData;
        }

        Person person = personService.getByMobile(userName);
        if (person != null && StrUtils.isNotBlank(person.getId())) {
            responseData.setErrorMessage("电话号码已存在");
        } else {
            //发送手机验证码

            AppSmsUtils.sendRegisterCode(userName);
            responseData.setSuccessMessage("发送手机验证码成功，请查阅。");
        }

        return responseData;
    }

//	/**
//	 * 注册 - 提交手机号码、密码
//	 */
//	@RequestMapping(value = "/register-step2-post")
//	public ApiResponseData registerStep2(HttpServletRequest request, HttpServletResponse response) {
////        if (!isValidApp(request)) {
////            responseData.setInvalidApp();
////            return responseData;
////        }
//
//        String userName = StrUtils.clean(request.getParameter(LOGIN_USERNAME_CAPTION));
//        String password = StrUtils.clean(request.getParameter(LOGIN_PASSWORD_CAPTION));
//
//
//		if (!(ValidateUtils.isMobile(userName) && ValidateUtils.isPassword(password))) {
//			responseData.setErrorMessage(ValidateUtils.getErrMsg());
//			return responseData;
//		}
//
//        Person person = personService.getByLoginName2(userName);
//        if (person != null && StrUtils.isNotBlank(person.getId())) {
//            responseData.setErrorMessage("电话号码已存在");
//        }else{
//            responseData.setSuccessMessage("正确提交手机号码和密码");
//        }
//
//		return responseData;
//	}

	/**
	 * 注册 - 提交手机号码、密码、验证码
     * 提交的注册账户的name为“userName”
     * 提交的密码的name为“password”
     * 提交的验证码的name为“checkCode”
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "提交注册接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "登录账户，现在只能用手机号", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "checkCode", value = "注册码", required = true, paramType = "form", dataType = "String")
    })
	public ApiResponseData register(HttpServletRequest request, HttpServletResponse response) {
//        if (!isValidApp(request)) {
//            responseData.setInvalidApp();
//            return responseData;
//        }
        responseData = new ApiResponseData();
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
        if (AppSmsUtils.checkRegitserCode(userName, code)) {
            //保存用户
            Person person = new Person();
            person.setLoginName(userName);
            person.setPassword(SystemService.entryptPassword(password));
            person.setMobile(userName);

            person.setRegisterFrom(Person.REGISTER_FROM_APP);
            personService.save(person);
            //重新登录验证一下
            Person loginUser = _loginCheck(userName, password);

            if (loginUser.equals(null)){
                responseData.setErrorMessage("出现未知异常，从重新提交注册。");
            }else {
                responseData.pushData("mobile", userName);
                responseData.pushData("personId",person.getId());
                responseData.setSuccessMessage("恭喜, 您已经成功注册了");
            }
        } else {
            responseData.setErrorMessage("请输入正确的验证码");
        }

        return responseData;
	}

    /**
     * 重置密码 - 提交手机号码
     * 提交的手机号name为“mobile”
     */
    @RequestMapping(value = "/forget-password-get-check-code",method = RequestMethod.POST)
    @ApiOperation(value = "忘记密码获取注册码接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, paramType = "form", dataType = "String")
    })

    public ApiResponseData forgetPasswordGetCheckCode(HttpServletRequest request, HttpServletResponse response) {
//        if (!isValidApp(request)) {
//            return renderInvalidApp(response);
//        }
        responseData = new ApiResponseData();

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
        AppSmsUtils.sendForgetPasswordCode(mobile);

        responseData.setSuccessMessage("");
        responseData.pushData(LOGIN_MOBILE_CAPTION, mobile);
        return responseData;

    }

    /**
     * 重置密码 - 提交手机号码和密码
     * 提交的手机号name为“mobile”
     * 提交的密码name为“password”
     */
//    @RequestMapping("/forget-password-step2-post")
//    public ApiResponseData forgetPasswordStep2Post(HttpServletRequest request, HttpServletResponse response) {
////        if (!isValidApp(request)) {
////            return renderInvalidApp(response);
////        }
//
//
//        String mobile = request.getParameter(LOGIN_MOBILE_CAPTION);
//        String password = request.getParameter(LOGIN_PASSWORD_CAPTION);
//
//        if (!ValidateUtils.isMobile(mobile)) {
//            responseData.setErrorMessage(ValidateUtils.getErrMsg());
//            return responseData;
//        }
//
//        if (!ValidateUtils.isPassword(mobile)) {
//            responseData.setErrorMessage(ValidateUtils.getErrMsg());
//            return responseData;
//        }
//
//        Person person = personService.getByMobile(mobile);
//        if (person == null) {
//            responseData.setErrorMessage("电话号码不存在");
//            return responseData;
//        }
//
//        responseData.setSuccessMessage("重置密码成功");
//
//        return responseData;
//    }

    /**
     * 重置密码 - 提交手机号码、密码、验证码
     * 提交的手机号name为“mobile”
     * 提交的密码name为“password”
     * 提交的验证码name为“checkCode”
     */
    @RequestMapping(value = "/forget-password-reset",method = RequestMethod.POST)
    @ApiOperation(value = "重置密码接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "新密码", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "checkCode", value = "注册码", required = true, paramType = "form", dataType = "String")
    })
    public ApiResponseData forgetPasswordReset(HttpServletRequest request, HttpServletResponse response) {
        responseData = new ApiResponseData();

        String mobile = request.getParameter(LOGIN_MOBILE_CAPTION);
        String password = request.getParameter(LOGIN_PASSWORD_CAPTION);
        String code = request.getParameter(LOGIN_CHECK_CODE_CAPTION);

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
        if (AppSmsUtils.checkForgetPasswordCode(mobile, code)) {
            //保存用户新密码
            person.setPassword(SystemService.entryptPassword(password));
            personService.save(person);
            Person loginUser = _loginCheck(mobile, password);

            if (loginUser.equals(null)){
                responseData.setErrorMessage("出现未知异常，从重新重置密码。");
            }else {
                responseData.pushData("mobile", mobile);
                responseData.pushData("personId",person.getId());
                responseData.setSuccessMessage("密码重置成功");
            }

        } else {
            responseData.setErrorMessage("请输入正确的验证码");
        }

        return responseData;
    }

	/**
	 * 退出
	 */
	@RequestMapping(value = "/logout")
    @ApiOperation(value = "退出登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "个人id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String")
    })
    public ApiResponseData logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        responseData = new ApiResponseData();
        if (_logout(request)){
            responseData.setSuccessMessage("已成功退出");
        } else{
            responseData.setErrorMessage("退出错误！");
        }



        return responseData;
	}

    /**
     * 检查是否登录
     * 将userId和token放到请求头中
     * @return
     */
    @RequestMapping(value = "/check-login")
    @ApiOperation(value = "验证是否登录接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "个人id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String")
    })
    public ApiResponseData checkLogin(HttpServletRequest request, HttpServletResponse response) {
        responseData = new ApiResponseData();
        if (isLogin(request)) {
            responseData.setSuccessMessage("当前用户已登录");
        } else {
            responseData.setErrorMessage("当前没有登录用户");
        }

        return responseData;
    }

}
