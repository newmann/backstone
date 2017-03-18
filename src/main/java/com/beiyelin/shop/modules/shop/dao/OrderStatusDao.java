/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.dao;

import com.beiyelin.shop.modules.shop.entity.OrderStatus;
import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;

/**
 * 订单状态序列集DAO
 * @author Tony Wong
 * @version 2015-04-16
 */
@MyBatisDao
public interface OrderStatusDao extends CrudDao<OrderStatus> {

}
