package com.beiyelin.shop.modules.sys.entity;

import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.persistence.DataEntity;

import java.util.Date;

/**
 * app短信Entity
 * @author Newmann
 * @version 2017-03-26
 */
public class AppSms extends DataEntity<AppSms> {

	private static final long serialVersionUID = 1L;

	private String mobile;
	private String msg;
	private String type;
	private Date expiredDate;
	private String isReceived;
    private String syncReturnResult;
    private String code; //验证码

	// type 短信类型
    public static final String TYPE_REGISTER = "1"; // 注册验证码短信类型
    public static final String TYPE_FORGET_PASSWORD = "2"; // 注册验证码短信类型

    //注册验证码短信
    //public static final String MSG_REGISTER = "验证码为 " + code + "，恭喜，您正在注册月光茶人，请填写验证码并完成注册。（月光茶人客服绝不会索取此验证码，请勿告知他人）";

	public AppSms(){
		super();
        this.isReceived = Global.NO;
    }

	public AppSms(String id){
		super(id);
        this.isReceived = Global.NO;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public String getIsReceived() {
		return isReceived;
	}

	public void setIsReceived(String isReceived) {
		this.isReceived = isReceived;
	}

    public String getSyncReturnResult() {
        return syncReturnResult;
    }

    public void setSyncReturnResult(String syncReturnResult) {
        this.syncReturnResult = syncReturnResult;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}