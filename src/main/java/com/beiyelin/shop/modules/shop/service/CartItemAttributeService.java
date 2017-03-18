/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.service;

import com.beiyelin.shop.common.service.CrudService;
import com.beiyelin.shop.modules.shop.dao.CartItemAttributeDao;
import com.beiyelin.shop.modules.shop.entity.CartItem;
import com.beiyelin.shop.modules.shop.entity.CartItemAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 购物车产品属性Service
 * @author Tony Wong
 * @version 2015-05-28
 */
@Service
@Transactional(readOnly = true)
public class CartItemAttributeService extends CrudService<CartItemAttributeDao, CartItemAttribute> {

    public List<CartItemAttribute> findByItemId(CartItem item) {
        CartItemAttribute attribute = new CartItemAttribute();
        attribute.setItem(item);
        return dao.findList(attribute);
    }

    public List<CartItemAttribute> findByItemId(String itemId) {
        CartItem item = new CartItem(itemId);
        return findByItemId(item);
    }

}
