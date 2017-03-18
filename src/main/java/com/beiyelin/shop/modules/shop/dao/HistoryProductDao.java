/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.shop.entity.HistoryProduct;


/**
 * 浏览产品历史DAO接口
 * @author Tony Wong
 * @version 2015-07-28
 */
@MyBatisDao
public interface HistoryProductDao extends CrudDao<HistoryProduct> {

    public HistoryProduct getBy(HistoryProduct entity);

}
