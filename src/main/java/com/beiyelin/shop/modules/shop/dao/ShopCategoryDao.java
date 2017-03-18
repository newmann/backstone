/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.dao;

import com.beiyelin.shop.common.persistence.TreeDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.shop.entity.ShopCategory;

/**
 * 产品目录DAO接口
 * @author Tony Wong
 * @version 2015-04-06
 */
@MyBatisDao
public interface ShopCategoryDao extends TreeDao<ShopCategory> {

    public ShopCategory getFirstSortCategory(ShopCategory category);
	
}
