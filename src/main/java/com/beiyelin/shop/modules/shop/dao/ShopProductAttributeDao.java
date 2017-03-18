/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.shop.entity.ShopProductAttribute;

/**
 * 商品属性DAO接口
 * @author Tony Wong
 * @version 2015-04-07
 */
@MyBatisDao
public interface ShopProductAttributeDao extends CrudDao<ShopProductAttribute> {

    public void deleteByProductId(String productId);

}
