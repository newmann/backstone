package com.beiyelin.shop.modules.sys.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by newmann on 2017/4/3.
 */
@ApiModel
@Data
public class LoginResponse {
    @ApiModelProperty(value = "登录后获取的用户ID", example = "300c5327fb304c1b8781d5ddf23ba865")
    private String personId;
    @ApiModelProperty(value = "登录后获取的登录令牌，以后每次登录都需要提供。系统根据令牌判断该用户是否登录", example = "2a16f215668c423a851e02a7c8b067eb")
    private String token;

}
