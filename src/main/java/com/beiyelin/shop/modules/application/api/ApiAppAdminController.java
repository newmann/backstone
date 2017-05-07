/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.application.api;

import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.security.authority.annotation.PermissionControl;
import com.beiyelin.shop.common.utils.StrUtils;
import com.beiyelin.shop.common.utils.ValidateUtils;
import com.beiyelin.shop.modules.application.reqbody.LoginReqBody;
import com.beiyelin.shop.modules.application.resbody.LoginResBody;
import com.beiyelin.shop.modules.application.entity.AppAdmin;
import com.beiyelin.shop.modules.sys.service.SystemService;
import com.beiyelin.shop.modules.application.utils.AppSmsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 登录Controller
 * 注意：因为app端没有用cookie来做session，shiro的登出（SecurityUtils.getSubject().logout();）不起作用，
 *      当改密码再登录时还是会引用之前的密码而导致登录失败。
 * 登录判断：
 *      在Handler中处理
  * @author Newmann HU
 * @version 2017-04-06
 *
 * Module:1000
 */
@RestController
@RequestMapping("/api/admin")
@Api(value="管理员controller",description="管理员账户的相关操作，包括：注册账户，退出登录，修改密码，重置密码，修改管理员信息。")
public class ApiAppAdminController extends ApiAppAdminBaseController {
    private static final String LOGIN_USERNAME_CAPTION="userName";
    private static final String LOGIN_PASSWORD_CAPTION="password";
    private static final String LOGIN_CHECK_CODE_CAPTION="checkCode";
    private static final String LOGIN_MOBILE_CAPTION="mobile";


    /**
     *
     * @param appAdmin
     * @return
     * @throws Throwable
     *
     *  method：01
     *
     */
    @PermissionControl("person:save")
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    @ApiOperation(value = "修改当前登录的管理员信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "系统管理员id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appAdmin.property", value = "AppAdmin类的属性，通过form提交", required = true, paramType = "form", dataType = "object")
    })
    public String saveAppAdmin(@Valid @RequestBody AppAdmin appAdmin, BindingResult validateResult) throws Throwable {

//            AppAdmin person = JsonMapper.getInstance().fromJson(request.getParameter("person"),AppAdmin.class);
        if (appAdmin==null){
//                throw new Exception("没有提交任何信息，不能保存！");
            throw new Exception(messageService.getMessage("ApiAppAdminController.saveAppAdmin.01"));
        }
        if (validateResult.hasErrors()){
            throw new Exception(validateResult.toString());
        }
//            beanValidator(person);

        appAdminService.save(appAdmin);

        return messageService.getMessage("SuccessComplete");

    }

    /**
     *
     * @param request
     * @param response
     * @return
     *
     * Module:09
     */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录的管理员信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "系统管理员id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "person.property", value = "AppAdmin类的属性，通过form提交", required = true, paramType = "form", dataType = "object")
    })
    public AppAdmin getAppAdmin(HttpServletRequest request, HttpServletResponse response) throws Throwable {

        String personId = request.getHeader(Global.REQUEST_USER_CAPTION);
        AppAdmin appAdmin = appAdminService.get(personId);
        return appAdmin;

    }

	/**
	 * 自建会话系统给app用
     * 判断用户是否登录的条件：person.id + person.app_login_token
     * 如果用户登录则重新生成app_login_token，实现单点登录，手机掉了只要再次登录，掉了的手机就不能登录了
     *
     *  method：02
     *
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userName", value = "登录账户，现在只能用手机号", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "form", dataType = "String")
    })
    public LoginResBody login(@RequestBody LoginReqBody loginReqBody, HttpServletResponse response) throws Throwable {

        LoginResBody loginResBody =new LoginResBody();

        String loginName = StrUtils.clean(loginReqBody.getUserName());
        String password = StrUtils.clean(loginReqBody.getPassword());

        //不能为空
        if (StrUtils.isBlank(loginName) || StrUtils.isBlank(password)) {
            throw new Exception("ApiAppAdminController.login.01");
        }

        //核查密码

        AppAdmin appAdmin = appAdminService.getByLoginName2(loginName);
        if (null == appAdmin){
            throw new Exception("ApiAppAdminController.login.NotExists");
        }
        if (SystemService.validatePassword(password, appAdmin.getPassword())) {
            throw new Exception("ApiAppAdminController.login.02");
        }

        //生成登录令牌
        String token = appLoginService.genAppLoginToken();
        appLoginService.updateAppLoginToken(appAdmin.getId(), token);

        loginResBody.setToken(token);
        loginResBody.setId(appAdmin.getId());

        return loginResBody;

    }




//    /**
//     * 注册 - 提交手机号码
//     *  提交的信息的name为“userName”
//     *
//     *  method：03
//     */
//    @RequestMapping(value = "/register-get-check-code",method = RequestMethod.POST)
//    @ApiOperation(value = "注册用户时获取注册码", httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userName", value = "登录账户，现在只能用手机号", required = true, paramType = "form", dataType = "String")
//
//    })
//    public String registerGetCheckCode(HttpServletRequest request, HttpServletResponse response) throws Throwable {
//
//        String userName = StrUtils.clean(request.getParameter(LOGIN_USERNAME_CAPTION));
//
//        if (!ValidateUtils.isMobile(userName)) {
//            throw new Exception(ValidateUtils.getErrMsg());
//
//        }
//
//        AppAdmin appAdmin = appAdminService.getByMobile(userName);
//        if (appAdmin != null && StrUtils.isNotBlank(appAdmin.getId())) {
//            throw new Exception("ApiAppAdminController.registerGetCheckCode.01");
//        } else {
//            //发送手机验证码
//
//            AppSmsUtils.sendRegisterCode(userName);
//            return messageService.getMessage("ApiAppAdminController.registerGetCheckCode.02");
//        }
//
//
//    }
//
//
//	/**
//	 * 注册 - 提交手机号码、密码、验证码
//     * 提交的注册账户的name为“userName”
//     * 提交的密码的name为“password”
//     * 提交的验证码的name为“checkCode”
//     *
//     * module:04
//     *
//	 */
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//    @ApiOperation(value = "提交注册接口", httpMethod = "POST")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "userName", value = "登录账户，现在只能用手机号", required = true, paramType = "form", dataType = "String"),
//            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "form", dataType = "String"),
//            @ApiImplicitParam(name = "checkCode", value = "注册码", required = true, paramType = "form", dataType = "String")
//    })
//	public String register(HttpServletRequest request, HttpServletResponse response) throws Throwable {
//
//        String userName = StrUtils.clean(request.getParameter(LOGIN_USERNAME_CAPTION));
//        String password = StrUtils.clean(request.getParameter(LOGIN_PASSWORD_CAPTION));
//        String code = StrUtils.clean(request.getParameter(LOGIN_CHECK_CODE_CAPTION));
//
//        if (!ValidateUtils.isMobile(userName) || !ValidateUtils.isPassword(password)) {
//            throw new Exception("ApiAppAdminController.register.01");
//        }
//
//        AppAdmin u = appAdminService.getByLoginName2(userName);
//        if (u != null && StrUtils.isNotBlank(u.getId())) {
//            throw new Exception("ApiAppAdminController.register.02");
//        }
//
//        //比较验证码
//        if (AppSmsUtils.checkRegitserCode(userName, code)) {
//            //保存用户
//            AppAdmin appAdmin = new AppAdmin();
//            appAdmin.setLoginName(userName);
//            appAdmin.setPassword(SystemService.entryptPassword(password));
//            appAdmin.setMobile(userName);
//
//            appAdminService.save(appAdmin);
//            return appAdmin.getId();
//
//        } else {
//            throw new Exception("ApiAppAdminController.register.04");
//        }
//
//	}

    /**
     * 重置密码 - 提交手机号码
     * 提交的手机号name为“mobile”
     *
     * module: 05
     *
     */
    @RequestMapping(value = "/forget-password-get-check-code",method = RequestMethod.POST)
    @ApiOperation(value = "忘记密码获取注册码接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, paramType = "form", dataType = "String")
    })

    public String forgetPasswordGetCheckCode(HttpServletRequest request, HttpServletResponse response) throws Throwable {
//        if (!isValidApp(request)) {
//            return renderInvalidApp(response);
//        }

        String mobile = StrUtils.clean(request.getParameter(LOGIN_MOBILE_CAPTION));

        if (!ValidateUtils.isMobile(mobile)) {
            throw new Exception(ValidateUtils.getErrMsg());
        }

        AppAdmin appAdmin = appAdminService.getByMobile(mobile);
        if (appAdmin == null) {
            throw new Exception("ApiAppAdminController.forgetPasswordGetCheckCode.01");
        }

        //发送重置密码的验证码
        AppSmsUtils.sendForgetPasswordCode(mobile);

        return messageService.getMessage("SuccessComplete");


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
//        AppAdmin appAdmin = appAdminService.getByMobile(mobile);
//        if (appAdmin == null) {
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
     *
     * Module:06
     */
    @RequestMapping(value = "/forget-password-reset",method = RequestMethod.POST)
    @ApiOperation(value = "重置密码接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mobile", value = "手机号", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "新密码", required = true, paramType = "form", dataType = "String"),
            @ApiImplicitParam(name = "checkCode", value = "注册码", required = true, paramType = "form", dataType = "String")
    })
    public String forgetPasswordReset(HttpServletRequest request, HttpServletResponse response) throws Throwable {

        String mobile = request.getParameter(LOGIN_MOBILE_CAPTION);
        String password = request.getParameter(LOGIN_PASSWORD_CAPTION);
        String code = request.getParameter(LOGIN_CHECK_CODE_CAPTION);

        if (!ValidateUtils.isMobile(mobile)) {
            throw new Exception(ValidateUtils.getErrMsg());
        }

        if (!ValidateUtils.isPassword(mobile)) {
            throw new Exception(ValidateUtils.getErrMsg());
        }

        AppAdmin appAdmin = appAdminService.getByMobile(mobile);

        if (appAdmin == null) {
            throw new Exception("ApiAppAdminController.forgetPasswordReset.01");
        }

        //比较验证码
        if (AppSmsUtils.checkForgetPasswordCode(mobile, code)) {
            //保存用户新密码
            appAdmin.setPassword(SystemService.entryptPassword(password));
            appAdminService.save(appAdmin);

                return messageService.getMessage("SuccessComplete");

        } else {
            throw new Exception("ApiAppAdminController.forgetPasswordReset.03");
        }


    }

	/**
	 * 退出
     *
     * Module:07
	 */
	@RequestMapping(value = "/logout")
    @ApiOperation(value = "退出登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "个人id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String")
    })
    public String logout(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        if (appLoginService.logout(request)){
            return messageService.getMessage("SuccessComplete");
        } else{
            throw new Exception("ApiAppAdminController.logout.01");
        }

	}

    /**
     * 检查是否登录
     * 将userId和token放到请求头中
     * @return
     *
     * Module:08
     */
    @RequestMapping(value = "/check-login")
    @ApiOperation(value = "验证是否登录接口", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "个人id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String")
    })
    public Boolean checkLogin(HttpServletRequest request, HttpServletResponse response) throws  Throwable {
        return appLoginService.isAppLoggedIn(request);

    }

}
