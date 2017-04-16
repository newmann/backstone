/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.entity;

import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.persistence.AppAdminDataEntity;
import com.beiyelin.shop.common.persistence.DataEntity;
import com.beiyelin.shop.common.supcan.annotation.treelist.cols.SupCol;
import com.beiyelin.shop.common.utils.IdGen;
import com.beiyelin.shop.common.utils.StrUtils;
import com.beiyelin.shop.common.utils.excel.annotation.ExcelField;
import com.beiyelin.shop.modules.sys.utils.UserUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 组织管理员Entity
 * @author Newmann HU
 * @version 2017-3-18
 */
@Data
public class AppAdmin extends AppAdminDataEntity<AppAdmin> {



	private static final long serialVersionUID = 1L;


	@ExcelField(title="登录名", align=2, sort=30)
	@Length(min=1, max=100, message="登录名长度必须介于 1 和 100 之间")
	private String loginName;// 登录名

	@Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
	private String password;// 密码

	@ExcelField(title="姓名", align=2, sort=40)
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
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

	@ExcelField(title="最后登录IP", type=1, align=1, sort=100)
	private String loginIp;	// 最后登陆IP

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登录日期", type=1, align=1, sort=110)
	private Date loginDate;	// 最后登陆日期

	private String loginFlag;	// 是否允许登陆

	private String oldLoginIp;	// 上次登陆IP
	private Date oldLoginDate;	// 上次登陆日期
	private  String orgId;	//关联的组织id


}