/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.api;

import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.security.authority.annotation.PermissionControl;
import com.beiyelin.shop.common.utils.StrUtils;
import com.beiyelin.shop.common.utils.ValidateUtils;
import com.beiyelin.shop.modules.sys.bean.LoginResponse;
import com.beiyelin.shop.modules.sys.bean.NewAppOrgBean;
import com.beiyelin.shop.modules.sys.entity.AppOrg;
import com.beiyelin.shop.modules.sys.service.AppOrgService;
import com.beiyelin.shop.modules.sys.service.SystemService;
import com.beiyelin.shop.modules.sys.utils.AppSmsUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 组织管理Controller
  * @author Newmann HU
 * @version 2017-04-15
 *
  */
@RestController
@RequestMapping("/api/admin/org")
@Api(value="组织管理controller",description="组织的相关操作，包括：创建组织、修改组织信息、锁定组织不可用。")
public class ApiAppOrgController extends ApiAppAdminBaseController {
    private static final String LOGIN_USERNAME_CAPTION="userName";
    private static final String LOGIN_PASSWORD_CAPTION="password";
    private static final String LOGIN_CHECK_CODE_CAPTION="checkCode";
    private static final String LOGIN_MOBILE_CAPTION="mobile";

    @Autowired
    private AppOrgService appOrgService;


    /**
     *
     * @param appOrg
     * @return
     * @throws Throwable
     *
     *  method：01
     *
     */
    @PermissionControl("appOrg:updateAppOrg")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @ApiOperation(value = "修改组织的信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "系统管理员id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appOrg.property", value = "AppOrg类的属性，通过form提交", required = true, paramType = "form", dataType = "object")
    })
    public String updateAppOrg(@Valid @RequestBody AppOrg appOrg, BindingResult validateResult,
                               @RequestHeader("appUserId") String curOperatorId ) throws Throwable {

//            AppOrg person = JsonMapper.getInstance().fromJson(request.getParameter("person"),AppOrg.class);
        if (appOrg==null){
//                throw new Exception("没有提交任何信息，不能保存！");
            throw new Exception(messageService.getMessage("ApiAppOrgController.saveAppOrg.01"));
        }
        if (validateResult.hasErrors()){
            throw new Exception(validateResult.toString());
        }
//            beanValidator(person);
        //设置当前操作员
        appOrg.setCurrentOperatorId(curOperatorId);

        appOrgService.save(appOrg);

        return messageService.getMessage("SuccessComplete");

    }
    /**
     *
     * @param newAppOrgBean
     * @return
     * @throws Throwable
     *
     *  method：01
     *
     */
    @PermissionControl("appOrg:newAppOrg")
    @RequestMapping(value = "/new",method = RequestMethod.POST)
    @ApiOperation(value = "新增组织", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "系统管理员id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appOrg.property", value = "AppOrg类的属性，通过form提交", required = true, paramType = "form", dataType = "object")
    })
    public String newAppOrg(@Valid @RequestBody NewAppOrgBean newAppOrgBean, BindingResult validateResult,
                            @RequestHeader("appUserId") String curOperatorId) throws Throwable {

//            AppOrg person = JsonMapper.getInstance().fromJson(request.getParameter("person"),AppOrg.class);
        if (newAppOrgBean==null){
//                throw new Exception("没有提交任何信息，不能保存！");
            throw new Exception(messageService.getMessage("ApiAppOrgController.newAppOrg.01"));
        }
        if (validateResult.hasErrors()){
            throw new Exception(validateResult.toString());
        }
//            beanValidator(person);
        //对密码进行加密
        newAppOrgBean.setPassword(SystemService.entryptPassword(newAppOrgBean.getPassword()));
        //新增组织
        appOrgService.newAppOrg(newAppOrgBean,curOperatorId);

        return messageService.getMessage("SuccessComplete");

    }
    /**
     *
     * @param request
     * @param code
     * @return
     *
     */
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录的管理员信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appUserId", value = "系统管理员id", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "appLoginToken", value = "登录token", required = true, paramType = "header", dataType = "String"),
            @ApiImplicitParam(name = "code", value = "组织代码，通过form提交", required = true, paramType = "form", dataType = "String")
    })
    public AppOrg getAppOrg(HttpServletRequest request,String code) throws Throwable {
//        String name = request.getHeader("name");
        AppOrg appOrg = appOrgService.getByCode(code);
        return appOrg;

    }






}
