/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.shop.entity.Sezi;

import java.util.Map;

/**
 * 色子每天摇一摇对象DAO接口
 * @author Tony Wong
 * @version 2015-07-16
 */
@MyBatisDao
public interface SeziDao extends CrudDao<Sezi> {

    public Map<String, Long> countDailyShakeByUserId(Sezi sezi);

}
