/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.sys.entity.AppAdmin;

/**
 * 系统管理员DAO接口
 * @author Newmann HU
 * @version 2017-04-7
 */
@MyBatisDao
public interface AppAdminDao extends CrudDao<AppAdmin> {

    /**
     * 获得系统管理员表的实体
     */
    public AppAdmin getEntity(String id);

	/**
	 * 根据登录名称查询系统管理员
	 * @param appAdmin
	 * @return
	 */
	public AppAdmin getByLoginName(AppAdmin appAdmin);

    /**
     * 根据手机号查询系统管理员
     * @param appAdmin
     * @return
     */
    public AppAdmin getByMobile(AppAdmin appAdmin);

	/**
	 * 前台根据登录名称查询系统管理员
	 * @param appAdmin
	 * @return
	 */
	public AppAdmin getByLoginName2(AppAdmin appAdmin);


	/**
	 * 查询全部系统管理员数目
	 * @return
	 */
	public long findAllCount(AppAdmin appAdmin);
	
	/**
	 * 更新系统管理员密码
	 * @param appAdmin
	 * @return
	 */
	public int updatePasswordById(AppAdmin appAdmin);
	
	/**
	 * 更新登录信息，如：登录IP、登录时间
	 * @param appAdmin
	 * @return
	 */
	public int updateLoginInfo(AppAdmin appAdmin);

	/**
	 * 更新系统管理员信息
	 * @param appAdmin
	 * @return
	 */
	public int updateAppAdminInfo(AppAdmin appAdmin);


}
