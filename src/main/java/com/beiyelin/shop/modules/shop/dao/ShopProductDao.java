/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.dao;

import com.beiyelin.shop.modules.shop.entity.ShopProduct;
import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 商品DAO接口
 * @author Tony Wong
 * @version 2015-04-06
 */
@MyBatisDao
public interface ShopProductDao extends CrudDao<ShopProduct> {

    List<ShopProduct> findFeaturedPrice(ShopProduct product);
	
}
