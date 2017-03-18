/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.shop.entity.Order;

/**
 * 订单DAO接口
 * @author Tony Wong
 * @version 2015-04-16
 */
@MyBatisDao
public interface OrderDao extends CrudDao<Order> {

    Order getOldestUnPrint(Order order);

    Order getOldest4AutoPrint(Order order);

    Order getOldest4AutoPrintByStoreId(Order order);

    Order getByPreorderId(Order order);

}
