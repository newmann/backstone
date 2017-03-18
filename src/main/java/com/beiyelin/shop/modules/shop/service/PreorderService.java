/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.service;

import com.beiyelin.shop.common.service.CrudService;
import com.beiyelin.shop.modules.shop.entity.Preorder;
import com.beiyelin.shop.modules.shop.dao.PreorderDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 预购订单Service
 * @author Tony Wong
 * @version 2015-04-19
 */
@Service
@Transactional(readOnly = true)
public class PreorderService extends CrudService<PreorderDao, Preorder> {

	@Autowired
	ShopProductService productService;

    public Preorder getWithUserId(String preorderId, String userId) {
        if (StringUtils.isBlank(preorderId) || StringUtils.isBlank(userId))
            return null;

        Preorder preorder = get(preorderId);
        if (preorder != null && preorder.getUser() != null && userId.equals(preorder.getUser().getId()))
            return preorder;
        else
            return null;
    }
}
