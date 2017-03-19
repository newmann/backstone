/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.api;

import com.alibaba.fastjson.JSONObject;
import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.mapper.JsonMapper;
import com.beiyelin.shop.common.security.authority.annotation.PermissionControl;
import com.beiyelin.shop.common.utils.CacheUtils;
import com.beiyelin.shop.common.utils.IdGen;
import com.beiyelin.shop.common.utils.StringUtils;
import com.beiyelin.shop.common.utils.ValidateUtils;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
 *      因为对shiro还不熟悉，暂时用加密再比较密码的方法来进行登录。
 * 登录判断：
 *      用户登录要用 person.loginName + person.password, 登录后生成新的 person.appLoginToken
 *      判断用户是否登录的session：person.id + person.appLoginToken
 * @author Tony Wong
 * @version 2015-6-13
 */
@Controller
@RequestMapping("${adminPath}/api/person")
public class ApiPersonController extends AppBaseController {

	@Autowired
    PersonService personService;

    @Autowired
    SmsService smsService;

    @Autowired
    CouponUserService couponUserService;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemService cartItemService;

//	@RequestMapping(value = "")
//	public String index() {
//		return "modules/app/person/index";
//	}
    @PermissionControl("person:save-person")
    @RequestMapping("/save-person")
    public String savePerson(HttpServletRequest request,@RequestBody Person person, HttpServletResponse response) {

        boolean result;
        int resultCode;
        String message;
        Map<String, Object> data = Maps.newHashMap();
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
            result = true;
            resultCode = ResultCode.Success;
            message = "保存个人信息成功。";

        } catch (Exception ex){
            result = false;
            resultCode = ResultCode.Failure;
            message = ex.getMessage();
        }

        return renderString(response, result, resultCode, message, data);
    }


    @RequestMapping("/get-person")
    public String getPerson(HttpServletRequest request, HttpServletResponse response) {
        boolean result;
        int resultCode;
        String message;
        Map<String, Object> data = Maps.newHashMap();

        try {
            if (isPersonLoggedIn(request)) {
                result = true;
                resultCode = ResultCode.Success;
                message = "用户信息";
                String personId = request.getParameter(Global.REQUEST_USER_CAPTION);
                Person person = personService.get(personId);
                Map<String, Object> oUser = person.toSimpleObj();
                data.put("person", oUser);
            } else {
                result = false;
                resultCode = ResultCode.Failure;
                message = "当前没有登录用户";
            }
        } catch (Exception ex){
            result = false;
            resultCode = ResultCode.Failure;
            message = ex.getMessage();
        }

        return renderString(response, result, resultCode, message, data);
    }

	/**
	 * 自建会话系统给app用
     * 判断用户是否登录的条件：person.id + person.app_login_token
     * 如果用户登录则重新生成app_login_token，实现单点登录，手机掉了只要再次登录，掉了的手机就不能登录了
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginPost(HttpServletRequest request, HttpServletResponse response) {
        if (!isValidApp(request)) {
            return renderInvalidApp(response);
        }

		boolean result;
		String message;
		Map<String, Object> data = Maps.newHashMap();

		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
		String password = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM);

		//不能为空
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
			result = false;
			message = "手机号和密码不能为空";
            return renderString(response, result, message, data);
		}

		//登录
//		UsernamePasswordToken token = new UsernamePasswordToken();
//		token.setUsername(username);
//		token.setPassword(password.toCharArray());
//		token.setRememberMe(true);
//		try {
//			SecurityUtils.getSubject().login(token);
//        }
//        catch (AuthenticationException e) {
//            logger.debug("/app/person/login-post throw AuthenticationException: {}", e.getMessage());
//            result = false;
//            message = "用户名或密码错误";
//            return renderString(response, result, message, data);
//        }
//        catch (Exception e) {
//            logger.debug("/app/person/login-post throw Exception: {}", e.getMessage());
//            result = false;
//            message = e.getMessage();
//            return renderString(response, result, message, data);
//        }

        Person person = _loginCheck(username, password);

        if (person == null) {
            result = false;
            message = "用户名或密码错误";
            return renderString(response, result, message, data);
        }

        //转移购物车项给用户
        String personId = person.getId();
        String appCartCookieId = getAppCartCookieId(request);
        if (StringUtils.isNotBlank(appCartCookieId)) {
            List<CartItem> cartItemList = cartItemService.findByAppCartCookieId(appCartCookieId, null);
            if (cartItemList != null && !cartItemList.isEmpty()) {
                Cart cart = cartService.getByUserId(personId);
                if (cart != null) { //清空用户的购物车项
                    cartItemService.clearByUserId(personId);
                } else { //创建用户购物车
                    cart = new Cart();
//                    cart.setUser(person);
                    cartService.save(cart);
                }
                //把产品转给该用户
                for (CartItem cartItem : cartItemList) {
                    cartItem.setUserId(personId);
                    cartItemService.save(cartItem);
                }
            }
        }

        //重新为客户端生成appCartCookieId
        String oAppCartCookieId = IdGen.uuid();

        //为了app能获得更好的体验，为app的购物车页面准备数据，app从购物车页面跳转到登录页时用
        //代码来自AppCartController.index
        int oCountUsefulCoupon = couponUserService.countUsefulCoupon(personId);
        data = cartItemService.findByUserIdWithCount4Json(personId, null);
        data.put("isLoggedIn", true);
        data.put("countUsefulCoupon", oCountUsefulCoupon);

        Map<String, Object> oUser = person.toSimpleObj();
        result = true;
        message = "成功登录";
        data.put("person", oUser);
        data.put("appCartCookieId", oAppCartCookieId);

        return renderString(response, result, message, data);
	}

//	/**
//	 * 登录失败，真正登录的POST请求由Filter完成
//	 */
//	@RequestMapping(value = "/login-post", method = RequestMethod.POST)
//	public String loginPost(HttpServletRequest request, HttpServletResponse response, Model model) {
//		String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
//		String password = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM);
//
//		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
//			model.addAttribute("message", "手机号和密码不能为空");
//			return "modules/app/person/login-post";
//		}
//
//		UsernamePasswordToken token = new UsernamePasswordToken();
//		token.setUsername(username);
//		token.setPassword(password.toCharArray());
//		try {
//			SecurityUtils.getSubject().login(token);
//		}
//		catch (Exception e) {
//			logger.debug("/app/person/login-post throw exception: {}", e.getMessage());
//			model.addAttribute("message", "手机号或密码不正确");
//
//			// 非授权异常，登录失败，验证码加1。
//			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
//
//			return "modules/app/person/login-post";
//		}
//
//		// 如果已经登录，则跳转
//		if(SecurityUtils.getSubject().isAuthenticated()){
//			logger.debug("/app/person/login-post success");
//			return "modules/app/person/index";
//		}
//
//		//String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
//		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
//		boolean mobile = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_MOBILE_PARAM);
//		String exception = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
//		String message = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM);
//
//		if (StringUtils.isBlank(message) || StringUtils.equals(message, "null")){
//			message = "用户或密码错误, 请重试.";
//		}
//
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM, rememberMe);
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_MOBILE_PARAM, mobile);
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, exception);
//		model.addAttribute(FormAuthenticationFilter.DEFAULT_MESSAGE_PARAM, message);
//
//		if (logger.isDebugEnabled()){
//			logger.debug("/app/person/login-post fail, active session size: {}, message: {}, exception: {}",
//					sessionDAO.getActiveSessions(false).size(), message, exception);
//		}
//
//		// 非授权异常，登录失败，验证码加1。
//		if (!UnauthorizedException.class.getName().equals(exception)){
//			model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(username, true, false));
//		}
//
//		// 验证失败清空验证码
//		request.getSession().setAttribute(ValidateCodeServlet.VALIDATE_CODE, IdGen.uuid());
//
//		// 如果是手机登录，则返回JSON字符串
////		if (mobile){
////	        return renderString(response, model);
////		}
//
//		return "modules/app/person/login-post";
//	}

    /**
     * 注册 - 提交手机号码
     */
    @RequestMapping(value = "/register-step1-post")
    public String registerStep1(HttpServletRequest request, HttpServletResponse response) {
        if (!isValidApp(request)) {
            return renderInvalidApp(response);
        }

        boolean result;
        String message;
        Map<String, Object> data = Maps.newHashMap();
        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        data.put("userLoginName", username);

        if (ValidateUtils.isMobile(username)) {
            result = true;
            message = "";
        } else {
            result = false;
            message = ValidateUtils.getErrMsg();
        }

        Person person = personService.getByLoginName2(username);
        if (person != null && StringUtils.isNotBlank(person.getId())) {
            result = false;
            message = "电话号码已存在";
        } else {
            //发送手机验证码
            SmsUtils.sendRegisterCode(username);
        }

        return renderString(response, result, message, data);
    }

	/**
	 * 注册 - 提交手机号码、密码
	 */
	@RequestMapping(value = "/register-step2-post")
	public String registerStep2(HttpServletRequest request, HttpServletResponse response) {
        if (!isValidApp(request)) {
            return renderInvalidApp(response);
        }

		boolean result;
		String message;
		Map<String, Object> data = Maps.newHashMap();
        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        String password = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM);
        data.put("userLoginName", username);
        data.put("userPassword", password);

		if (ValidateUtils.isMobile(username) && ValidateUtils.isPassword(password)) {
			result = true;
			message = "";
		} else {
			result = false;
			message = ValidateUtils.getErrMsg();
		}

        Person person = personService.getByLoginName2(username);
        if (person != null && StringUtils.isNotBlank(person.getId())) {
            result = false;
            message = "电话号码已存在";
        }

		return renderString(response, result, message, data);
	}

	/**
	 * 注册 - 提交手机号码、密码、验证码
	 */
	@RequestMapping(value = "/register-step3-post", method = RequestMethod.POST)
	public String registerStep3Post(HttpServletRequest request, HttpServletResponse response) {
        if (!isValidApp(request)) {
            return renderInvalidApp(response);
        }

		boolean result;
		String message;
		Map<String, Object> data = Maps.newHashMap();
        String username = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_USERNAME_PARAM);
        String password = WebUtils.getCleanParam(request, FormAuthenticationFilter.DEFAULT_PASSWORD_PARAM);
        String code = request.getParameter("code");

        if (!ValidateUtils.isMobile(username) || !ValidateUtils.isPassword(password)) {
            result = false;
            message = "提交的手机号码或密码不符合规则";
            return renderString(response, result, message, data);
        }

        Person u = personService.getByLoginName2(username);
        if (u != null && StringUtils.isNotBlank(u.getId())) {
            result = false;
            message = "电话号码已存在";
            return renderString(response, result, message, data);
        }

        //比较验证码
        if (smsService.checkRegisterCode(username, code)) {
            //保存用户
            Person person = new Person();
            person.setLoginName(username);
            person.setPassword(SystemService.entryptPassword(password));
            person.setMobile(username);
//            person.setRemarks("前台用户");
            person.setRegisterFrom(Person.REGISTER_FROM_APP);
            personService.save(person);

            //用户自动登录
//            String personId = person.getId();
//            UsernamePasswordToken token = new UsernamePasswordToken();
//            token.setUsername(username);
//            token.setPassword(password.toCharArray());
//            token.setRememberMe(true);
//            try {
//                SecurityUtils.getSubject().login(token);
//            }
//            catch (AuthenticationException e) {
//                logger.debug("/app/person/register-step3-post throw AuthenticationException: {}", e.getMessage());
//                result = false;
//                message = "用户名或密码错误";
//                return renderString(response, result, message, data);
//            }
//            catch (Exception e) {
//                logger.debug("/app/person/register-step3-post throw Exception: {}",  e.getMessage());
//                result = false;
//                message = e.getMessage();
//                return renderString(response, result, message, data);
//            }
//            //更新app登录令牌
//            person.setAppLoginToken(personService.genAppLoginToken());
//            personService.updateAppLoginToken(person);
            Person loginUser = _loginCheck(username, password);

            //给新注册用户发送优惠券
            if (!STOP_COUPON_BUY_ONE_SEND_ONE) {
                couponUserService.send4NewUser(loginUser.getId());
            }

            //转移购物车项给用户
            String appCartCookieId = getAppCartCookieId(request);
            if (StringUtils.isNotBlank(appCartCookieId)) {
                List<CartItem> cartItemList = cartItemService.findByAppCartCookieId(appCartCookieId, null);
                if (cartItemList != null && !cartItemList.isEmpty()) {
                    //创建用户购物车
                    Cart cart = new Cart();
//                    cart.setUser(u);
                    cartService.save(cart);
                    //把产品转给该用户
                    for (CartItem cartItem : cartItemList) {
                        cartItem.setUserId(loginUser.getId());
                        cartItemService.save(cartItem);
                    }
                }
            }

            //重新为客户端生成appCartCookieId
            String oAppCartCookieId = IdGen.uuid();

            Map<String, Object> oUser = loginUser.toSimpleObj();

            result = true;
            message = "恭喜, 您已经成功注册了";
            data.put("person", oUser);
            data.put("appCartCookieId", oAppCartCookieId);
        } else {
            result = false;
            message = "请输入正确的验证码";
        }

        return renderString(response, result, message, data);
	}

    /**
     * 重置密码 - 提交手机号码
     */
    @RequestMapping("/forget-password-step1-post")
    public String forgetPasswordStep1Post(HttpServletRequest request, HttpServletResponse response) {
        if (!isValidApp(request)) {
            return renderInvalidApp(response);
        }

        boolean result;
        String message;
        Map<String, Object> data = Maps.newHashMap();

        String mobile = request.getParameter("mobile");

        if (!ValidateUtils.isMobile(mobile)) {
            result = false;
            message = ValidateUtils.getErrMsg();
            return renderString(response, result, message, data);
        }

        Person person = personService.getByMobile(mobile);
        if (person == null) {
            result = false;
            message = "电话号码不存在";
            return renderString(response, result, message, data);
        }

        //发送重置密码的验证码
        SmsUtils.sendForgetPasswordCode(mobile);

        result = true;
        message = "";
        data.put("mobile", mobile);
        return renderString(response, result, message, data);
    }

    /**
     * 重置密码 - 提交手机号码和密码
     */
    @RequestMapping("/forget-password-step2-post")
    public String forgetPasswordStep2Post(HttpServletRequest request, HttpServletResponse response) {
        if (!isValidApp(request)) {
            return renderInvalidApp(response);
        }

        boolean result;
        String message;
        Map<String, Object> data = Maps.newHashMap();

        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");

        if (!ValidateUtils.isMobile(mobile)) {
            result = false;
            message = ValidateUtils.getErrMsg();
            return renderString(response, result, message, data);
        }

        if (!ValidateUtils.isPassword(mobile)) {
            result = false;
            message = ValidateUtils.getErrMsg();
            return renderString(response, result, message, data);
        }

        Person person = personService.getByMobile(mobile);
        if (person == null) {
            result = false;
            message = "电话号码不存在";
            return renderString(response, result, message, data);
        }

        result = true;
        message = "";
        data.put("mobile", mobile);
        data.put("password", password);
        return renderString(response, result, message, data);
    }

    /**
     * 重置密码 - 提交手机号码、密码、验证码
     */
    @RequestMapping("/forget-password-step3-post")
    public String forgetPasswordStep3Post(HttpServletRequest request, HttpServletResponse response) {
        if (!isValidApp(request)) {
            return renderInvalidApp(response);
        }

        boolean result;
        String message;
        Map<String, Object> data = Maps.newHashMap();

        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");
        String code = request.getParameter("code");

        if (!ValidateUtils.isMobile(mobile)) {
            result = false;
            message = ValidateUtils.getErrMsg();
            return renderString(response, result, message, data);
        }

        if (!ValidateUtils.isPassword(mobile)) {
            result = false;
            message = ValidateUtils.getErrMsg();
            return renderString(response, result, message, data);
        }

        Person person = personService.getByMobile(mobile);

        if (person == null) {
            result = false;
            message = "电话号码不存在";
            return renderString(response, result, message, data);
        }

        //比较验证码
        if (smsService.checkForgetPasswordCode(mobile, code)) {
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

            //转移购物车项给用户
            String personId = loginUser.getId();
            String appCartCookieId = getAppCartCookieId(request);
            if (StringUtils.isNotBlank(appCartCookieId)) {
                List<CartItem> cartItemList = cartItemService.findByAppCartCookieId(appCartCookieId, null);
                if (cartItemList != null && !cartItemList.isEmpty()) {
                    Cart cart = cartService.getByUserId(personId);
                    if (cart != null) { //清空用户的购物车项
                        cartItemService.clearByUserId(personId);
                    } else { //创建用户购物车
                        cart = new Cart();
//                        cart.setUser(person);
                        cartService.save(cart);
                    }
                    //把产品转给该用户
                    for (CartItem cartItem : cartItemList) {
                        cartItem.setUserId(personId);
                        cartItemService.save(cartItem);
                    }
                }
            }

            //重新为客户端生成appCartCookieId
            String oAppCartCookieId = IdGen.uuid();

            result = true;
            message = "";
            Map<String, Object> oUser = loginUser.toSimpleObj();
            data.put("person", oUser);
            data.put("appCartCookieId", oAppCartCookieId);
            data.put("mobile", mobile);
            data.put("password", password);
        } else {
            result = false;
            message = "请输入正确的验证码";
        }

        return renderString(response, result, message, data);
    }

	/**
	 * 退出
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean result;
        String message;
        Map<String, Object> data = Maps.newHashMap();

        _logout(request);

        //重新为客户端生成appCartCookieId
        String oAppCartCookieId = IdGen.uuid();

        result = true;
		message = "已成功退出";
        data.put("appCartCookieId", oAppCartCookieId);
        return renderString(response, result, message, data);
	}

    /**
     * 获得登录的用户对象
     * @return
     */
    @RequestMapping(value = "/check-login")
    public String checkLogin(HttpServletRequest request, HttpServletResponse response) {
        boolean result;
        String message;
        Map<String, Object> data = Maps.newHashMap();

        if (isLoggedIn(request)) {
            result = true;
            message = "";
        } else {
            result = false;
            message = "当前没有登录用户";
        }

        return renderString(response, result, message, data);
    }
	
	/**
	 * 是否是验证码登录
	 * @param username 用户名
	 * @param isFail 计数加1
	 * @param clean 计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean isValidateCodeLogin(String username, boolean isFail, boolean clean){
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils.get("loginFailMap");
		if (loginFailMap==null){
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(username);
		if (loginFailNum==null){
			loginFailNum = 0;
		}
		if (isFail){
			loginFailNum++;
			loginFailMap.put(username, loginFailNum);
		}
		if (clean){
			loginFailMap.remove(username);
		}
		return loginFailNum >= 3;
	}
}
