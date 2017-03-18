/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.dao;

import com.beiyelin.shop.common.persistence.TreeDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.sys.entity.Area;

import java.util.List;

/**
 * 区域DAO接口
 * @author Tony Wong
 * @version 2014-05-16
 */
@MyBatisDao
public interface AreaDao extends TreeDao<Area> {

    /**
     * 新的接口gets，因为gets已经存在
     * @param id
     * @return
     */
    Area gets(String id);

    /**
     * 新的接口findLists，因为findList已经存在
     * @param entity
     * @return
     */
    List<Area> findLists(Area entity);
}
