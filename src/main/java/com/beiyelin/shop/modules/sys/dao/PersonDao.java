/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.sys.entity.Person;


import java.util.List;

/**
 * 个人DAO接口
 * @author Newmann HU
 * @version 2017-03-18
 */
@MyBatisDao
public interface PersonDao extends CrudDao<Person> {

    /**
     * 获得用户表的实体
     */
    public Person getEntity(String id);

	/**
	 * 根据登录名称查询用户
	 * @param person
	 * @return
	 */
	public Person getByLoginName(Person person);

    /**
     * 根据手机号查询用户
     * @param person
     * @return
     */
    public Person getByMobile(Person person);

	/**
	 * 前台根据登录名称查询用户
	 * @param person
	 * @return
	 */
	public Person getByLoginName2(Person person);


	/**
	 * 查询全部用户数目
	 * @return
	 */
	public long findAllCount(Person person);
	
	/**
	 * 更新用户密码
	 * @param person
	 * @return
	 */
	public int updatePasswordById(Person person);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param person
	 * @return
	 */
	public int updateLoginInfo(Person person);

	/**
	 * 删除用户角色关联数据
	 * @param person
	 * @return
	 */
//	public int deleteUserRole(Person person);

	/**
	 * 插入用户角色关联数据
	 * @param person
	 * @return
	 */
//	public int insertUserRole(Person person);

	/**
	 * 前台用户插入用户角色关联数据
	 * @param person
	 * @return
	 */
//	public int insertUserRole4Frontend(Person person);
	
	/**
	 * 更新用户信息
	 * @param person
	 * @return
	 */
	public int updatePersonInfo(Person person);



    /**=======================
     * 自建会话系统给app用
     * 判断用户是否登录的条件：person.id + person.app_login_token
     =======================*/

	/**
	 * 更新APP用户登录令牌
	 * @param person
	 * @return
	 */
	public int updateAppLoginToken(Person person);

	public long isAppLoggedIn(Person person);

}
