/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.entity;

import com.beiyelin.shop.common.config.Global;
import com.beiyelin.shop.common.persistence.DataEntity;
import com.beiyelin.shop.common.supcan.annotation.treelist.cols.SupCol;
import com.beiyelin.shop.common.utils.Collections3;
import com.beiyelin.shop.common.utils.excel.annotation.ExcelField;
import com.beiyelin.shop.common.utils.excel.fieldtype.RoleListType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 个人Entity
 * @author Newmann HU
 * @version 2017-3-18
 */
public class Person extends DataEntity<Person> {

	private static final long serialVersionUID = 1L;
	private String loginName;// 登录名
	private String password;// 密码
	private String name;	// 姓名
	private String email;	// 邮箱
	private String phone;	// 电话
	private String mobile;	// 手机
	private String userType;// 用户类型
	private String loginIp;	// 最后登陆IP
	private Date loginDate;	// 最后登陆日期
	private String loginFlag;	// 是否允许登陆
	private String photo;	// 头像
    private String latestPayType;
    /**
     * 自建会话系统给app用
     * 判断用户是否登录的条件：user.id + user.appLoginToken(每次登录都会生成新的token)
     * 退出登录只要把app的用户信息删除和设置user.appLoginToken=null
     */
	private String appLoginToken; //

	private String oldLoginName;// 原登录名
	private String newPassword;	// 新密码

	private String oldLoginIp;	// 上次登陆IP
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
		map.put("appLoginToken", appLoginToken);
        map.put("level", "铜牌用户");
		return map;
	}
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getLoginFlag() {
		return loginFlag;
	}

	public void setLoginFlag(String loginFlag) {
		this.loginFlag = loginFlag;
	}

	@SupCol(isUnique="true", isHide="true")
	@ExcelField(title="ID", type=1, align=2, sort=1)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	@Length(min=1, max=100, message="登录名长度必须介于 1 和 100 之间")
	@ExcelField(title="登录名", align=2, sort=30)
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@JsonIgnore
	@Length(min=1, max=100, message="密码长度必须介于 1 和 100 之间")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	@ExcelField(title="姓名", align=2, sort=40)
	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}

	@Email(message="邮箱格式不正确")
	@Length(min=0, max=200, message="邮箱长度必须介于 1 和 200 之间")
	@ExcelField(title="邮箱", align=1, sort=50)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Length(min=0, max=200, message="电话长度必须介于 1 和 200 之间")
	@ExcelField(title="电话", align=2, sort=60)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200, message="手机长度必须介于 1 和 200 之间")
	@ExcelField(title="手机", align=2, sort=70)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@ExcelField(title="备注", align=1, sort=900)
	public String getRemarks() {
		return remarks;
	}
	
	@Length(min=0, max=100, message="用户类型长度必须介于 1 和 100 之间")
	@ExcelField(title="用户类型", align=2, sort=80, dictType="sys_user_type")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@ExcelField(title="创建时间", type=0, align=1, sort=90)
	public Date getCreateDate() {
		return createDate;
	}

	@ExcelField(title="最后登录IP", type=1, align=1, sort=100)
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="最后登录日期", type=1, align=1, sort=110)
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getOldLoginName() {
		return oldLoginName;
	}

	public void setOldLoginName(String oldLoginName) {
		this.oldLoginName = oldLoginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldLoginIp() {
		if (oldLoginIp == null){
			return loginIp;
		}
		return oldLoginIp;
	}

	public void setOldLoginIp(String oldLoginIp) {
		this.oldLoginIp = oldLoginIp;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOldLoginDate() {
		if (oldLoginDate == null){
			return loginDate;
		}
		return oldLoginDate;
	}

	public void setOldLoginDate(Date oldLoginDate) {
		this.oldLoginDate = oldLoginDate;
	}

//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}
//
//	@JsonIgnore
//	@ExcelField(title="拥有角色", align=1, sort=800, fieldType=RoleListType.class)
//	public List<Role> getRoleList() {
//		return roleList;
//	}
//
//	public void setRoleList(List<Role> roleList) {
//		this.roleList = roleList;
//	}
//
//	@JsonIgnore
//	public List<String> getRoleIdList() {
//		List<String> roleIdList = Lists.newArrayList();
//		for (Role role : roleList) {
//			roleIdList.add(role.getId());
//		}
//		return roleIdList;
//	}

//	public void setRoleIdList(List<String> roleIdList) {
//		roleList = Lists.newArrayList();
//		for (String roleId : roleIdList) {
//			Role role = new Role();
//			role.setId(roleId);
//			roleList.add(role);
//		}
//	}

	public String getRegisterFrom() {
		return registerFrom;
	}

	public void setRegisterFrom(String registerFrom) {
		this.registerFrom = registerFrom;
	}

	/**
	 * 用户拥有的角色名称字符串, 多个角色名称用','分隔.
	 */
//	public String getRoleNames() {
//		return Collections3.extractToString(roleList, "name", ",");
//	}
	

	@Override
	public String toString() {
		return id;
	}

    public String getAppLoginToken() {
        return appLoginToken;
    }

    public void setAppLoginToken(String appLoginToken) {
        this.appLoginToken = appLoginToken;
    }

    public String getLatestPayType() {
        return latestPayType;
    }

    public void setLatestPayType(String latestPayType) {
        this.latestPayType = latestPayType;
    }
}