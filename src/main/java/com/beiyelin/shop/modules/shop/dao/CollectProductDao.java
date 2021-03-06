/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.shop.entity.CollectProduct;

import java.util.Map;

/**
 * 收藏产品DAO接口
 * @author Tony Wong
 * @version 2015-07-16
 */
@MyBatisDao
public interface CollectProductDao extends CrudDao<CollectProduct> {

    Map<String, Object> hasCollected(CollectProduct entity);

}
