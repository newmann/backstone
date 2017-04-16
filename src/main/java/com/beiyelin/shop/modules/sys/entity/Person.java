/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.entity;

import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.persistence.AppAdminDataEntity;
import com.beiyelin.shop.common.persistence.DataEntity;
import com.beiyelin.shop.common.supcan.annotation.treelist.cols.SupCol;
import com.beiyelin.shop.common.utils.excel.annotation.ExcelField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.Map;

/**
 * 个人Entity
 * @author Newmann HU
 * @version 2017-3-18
 */
@Data
public class Person extends AppAdminDataEntity<Person> {

	private static final long serialVersionUID = 1L;


	@Length(min=1, max=100, message="{Person.bv.loginName.Length}")
	@ExcelField(title="登录名", align=2, sort=30)
	private String loginName;// 登录名

	@JsonIgnore
	@Length(min=1, max=100, message="{Person.bv.password.Length}")
	private String password;// 密码

	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	@ExcelField(title="姓名", align=2, sort=40)
	private String name;	// 姓名

	@Email(message="邮箱格式不正确")
	@Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
	@ExcelField(title="邮箱", align=1, sort=50)
	private String email;	// 邮箱

	@Length(min=0, max=200, message="电话长度必须介于 1 和 200 之间")
	@ExcelField(title="电话", align=2, sort=60)
	private String phone;	// 电话

	@Length(min=0, max=200, message="手机长度必须介于 1 和 200 之间")
	@ExcelField(title="手机", align=2, sort=70)
	private String mobile;	// 手机

	@Length(min=0, max=100, message="用户类型长度必须介于 1 和 100 之间")
	@ExcelField(title="用户类型", align=2, sort=80, dictType="sys_user_type")
	private String userType;// 用户类型

	private String loginIp;	// 最后登陆IP
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date loginDate;	// 最后登陆日期
	private String loginFlag;	// 是否允许登陆
	private String photo;	// 头像

	private String dbUrl; //所属数据库
	private String oldLoginName;// 原登录名
	private String newPassword;	// 新密码

	private String oldLoginIp;	// 上次登陆IP

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date oldLoginDate;	// 上次登陆日期

	private String registerFrom; //注册来源

//	private Role role;	// 根据角色查询用户条件

//	private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表

	//registerFrom
	public static final String REGISTER_FROM_WEB = "1"; //PC注册用户
	public static final String REGISTER_FROM_APP = "2"; //APP注册用户

	public Person() {
		super();
		this.loginFlag = Global.YES;
	}

	public Person(String id){
		super(id);
	}

	public Person(String id, String loginName){
		super(id);
		this.loginName = loginName;
	}

//	public Person(Role role){
//		super();
//		this.role = role;
//	}

	/**
	 * 只转化当前字段, 方便给json用
	 * @return
	 */
	public Map<String, Object> toSimpleObj() {
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", id);
		map.put("name", name);
		map.put("loginName", loginName);
		map.put("mobile", mobile);
        map.put("level", "铜牌用户");
		return map;
	}


	@Override
	public String toString() {
		return id;
	}



//    public String getAppLoginToken() {
//        return appLoginToken;
//    }
//
//    public void setAppLoginToken(String appLoginToken) {
//        this.appLoginToken = appLoginToken;
//    }
//
//    public String getLatestPayType() {
//        return latestPayType;
//    }
//
//    public void setLatestPayType(String latestPayType) {
//        this.latestPayType = latestPayType;
//    }
}