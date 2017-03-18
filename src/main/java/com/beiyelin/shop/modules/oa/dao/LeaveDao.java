/**
 * There are <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop code generation
 */
package com.beiyelin.shop.modules.oa.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.modules.oa.entity.Leave;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;

/**
 * 请假DAO接口
 * @author liuj
 * @version 2013-8-23
 */
@MyBatisDao
public interface LeaveDao extends CrudDao<Leave> {
	
	/**
	 * 更新流程实例ID
	 * @param leave
	 * @return
	 */
	public int updateProcessInstanceId(Leave leave);
	
	/**
	 * 更新实际开始结束时间
	 * @param leave
	 * @return
	 */
	public int updateRealityTime(Leave leave);
	
}
