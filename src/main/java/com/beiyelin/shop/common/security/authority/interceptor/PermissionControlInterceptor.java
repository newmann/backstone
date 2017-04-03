package com.beiyelin.shop.common.security.authority.interceptor;

import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.security.authority.annotation.PermissionControl;
import com.beiyelin.shop.common.utils.JsonUtils;
import com.beiyelin.shop.common.utils.StrUtils;
import com.beiyelin.shop.modules.sys.service.AppLoginService;
import com.beiyelin.shop.modules.sys.service.PersonService;
import com.beiyelin.shop.common.config.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by newmann on 2017/3/19.
 */
public class PermissionControlInterceptor  extends HandlerInterceptorAdapter {
    @Autowired
    private AppLoginService appLoginService;

    private final static Logger logger = LoggerFactory.getLogger(PermissionControlInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("开始处理身份认证判断");
        //客户端提交的用户和token
        String token = StrUtils.clean(request.getHeader(Global.REQUEST_TOKEN_CAPTION));
        String userID = StrUtils.clean(request.getHeader(Global.REQUEST_USER_CAPTION));
        if (StrUtils.isBlank(token) && StrUtils.isBlank(userID)){
            responseFail(response,"用戶ID或token不全，請提交完整信息。");
            return false;
        }

        if (!appLoginService.isAppLoggedIn(userID, token)) {

            responseFail(response,"用户没有登录，请重新登录。");
            return false;
        }


        if(handler.getClass().isAssignableFrom(HandlerMethod.class)){
            PermissionControl permissionControl = ((HandlerMethod) handler).getMethodAnnotation(PermissionControl.class);
            //没有声明需要权限,或者声明不验证权限
            if(permissionControl == null || permissionControl.value().length == 0)
                return super.preHandle(request, response, handler);
            else{
                //在这里实现自己的权限验证逻辑
                boolean isPermitted = true;
                for(int i = 0 ; i < permissionControl.value().length ;i++){
                    logger.info("开始判断权限：" + permissionControl.value()[i]);

                }
                return super.preHandle(request, response, handler);
//                else//如果验证失败
//                {
//                    //返回登录权限错误的json包
//                    response.sendRedirect("account/login");
//                    return false;
//                }
            }
        }else {
            return super.preHandle(request, response, handler);
        }

    }

    //登录失败时默认返回401状态码
    private void responseTokenFail(HttpServletResponse response) throws IOException {
        String msg ="用户不存在或token验证错误，请提交正确的用户ID和Token。";
        responseFail(response,msg);
    }

    //登录失败时默认返回401状态码
    private void responseFail(HttpServletResponse response,String msg) throws IOException {
        response.reset();
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        JsonUtils.setResponse(response);
        response.getWriter().write(JsonUtils.toString(false, ResultCode.Failure,msg));
    }

    //登录失败时默认返回401状态码
    private void responsePermissionFail(HttpServletResponse response,String perm) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("没有权限 + "+ perm);
    }
}
