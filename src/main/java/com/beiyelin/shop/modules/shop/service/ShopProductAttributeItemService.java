/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.service;

import com.beiyelin.shop.common.service.CrudService;
import com.beiyelin.shop.modules.shop.entity.ShopProductAttributeItem;
import com.beiyelin.shop.modules.shop.dao.ShopProductAttributeItemDao;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 公用的商品属性Service
 * @author Tony Wong
 * @version 2015-05-15
 */
@Service
@Transactional(readOnly = true)
public class ShopProductAttributeItemService extends CrudService<ShopProductAttributeItemDao, ShopProductAttributeItem> {

	@Autowired
	ShopProductAttributeItemValueService attrValueService;

	/**
	 * 获取单条数据attrItem, 包括值列表attrValue
	 */
	public ShopProductAttributeItem get(String id) {
		ShopProductAttributeItem item = dao.get(id);
		item.setValueList(attrValueService.findByItemId(item));
		return item;
	}

	/**
	 * 获取所有属性项
	 */
	public List<ShopProductAttributeItem> findAll() {
		ShopProductAttributeItem item = new ShopProductAttributeItem();
		return findList(item);
	}
}
