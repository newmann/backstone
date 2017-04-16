/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.sys.entity.AppAdmin;
import com.beiyelin.shop.modules.sys.entity.AppOrg;

/**
 * 組織DAO接口
 * @author Newmann HU
 * @version 2017-04-7
 */
@MyBatisDao
public interface AppOrgDao extends CrudDao<AppOrg> {

    /**
     * 获得组织的实体
     */
    public AppOrg getEntity(String id);

	/**
	 * 根据代码查询组织
	 * @param appOrg
	 * @return
	 */
	public AppOrg getByCode(AppOrg appOrg);


	/**
	 * 查询全部组织数目
	 * @return
	 */
	public long findAllCount(AppOrg appOrg);
	
	/**
	 * 更新组织的信息
	 * @param appOrg
	 * @return
	 */
	public int updateAppOrgInfo(AppOrg appOrg);


}
