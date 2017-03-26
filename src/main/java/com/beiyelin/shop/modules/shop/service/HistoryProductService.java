/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.service;

import com.beiyelin.shop.common.persistence.Page;
import com.beiyelin.shop.common.service.CrudService;
import com.beiyelin.shop.common.utils.StrUtils;
import com.beiyelin.shop.modules.shop.entity.HistoryProduct;
import com.beiyelin.shop.modules.shop.entity.ShopProduct;
import com.beiyelin.shop.modules.shop.utils.ShopProductUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.beiyelin.shop.modules.shop.dao.HistoryProductDao;
import com.beiyelin.shop.modules.sys.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 浏览产品历史Service
 * @author Tony Wong
 * @version 2015-07-16
 */
@Service
@Transactional(readOnly = true)
public class HistoryProductService extends CrudService<HistoryProductDao, HistoryProduct> {

    /**
     * 查找用户的最近浏览的18个产品
     * @param userId
     * @return
     */
    public List<HistoryProduct> findByUserId(String userId) {
        HistoryProduct hp = new HistoryProduct();
        hp.setUser(new User(userId));
        Page<HistoryProduct> page = new Page<HistoryProduct>();
        page.setPageNo(1);
        page.setPageSize(18);
        List<HistoryProduct> list = findPage(page, hp).getList();
        for (HistoryProduct historyProduct : list) {
            ShopProduct product = ShopProductUtils.getProduct(historyProduct.getProduct().getId());
            if (product != null && StrUtils.isNotBlank(product.getId()))
                historyProduct.setProduct(product);
            else
                historyProduct.setProduct(null);
        }
        return list;
    }
    /**
     * 查找用户的所有收藏商品
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findByUserId4SimpleObj(String userId) {
        List<Map<String, Object>> oList = Lists.newArrayList();
        HistoryProduct hp = new HistoryProduct();
        hp.setUser(new User(userId));
        Page<HistoryProduct> page = new Page<HistoryProduct>();
        page.setPageNo(1);
        page.setPageSize(18);
        List<HistoryProduct> list = findPage(page, hp).getList();
        for (HistoryProduct historyProduct : list) {
            Map<String, Object> oHistoryProduct = historyProduct.toSimpleObj();
            Map<String, Object> oProduct = Maps.newHashMap();
            ShopProduct product = ShopProductUtils.getProduct(historyProduct.getProduct().getId());
            if (product != null && StrUtils.isNotBlank(product.getId())) {
                oProduct = product.toSimpleObj();
            }
            oHistoryProduct.put("product", oProduct);
            oList.add(oHistoryProduct);
        }
        return oList;
    }

    /**
     * 通过userId，productId获取浏览过的产品
     */
    public HistoryProduct getBy(String userId, String productId) {
        if (StrUtils.isBlank(userId) || StrUtils.isBlank(productId))
            return null;

        HistoryProduct historyProduct = new HistoryProduct();
        historyProduct.setProduct(new ShopProduct(productId));
        historyProduct.setUser(new User(userId));
        return dao.getBy(historyProduct);
    }

    /**
     * 添加浏览产品历史
     */
    @Transactional(readOnly = false)
    public void add(String userId, String productId) {
        if (StrUtils.isBlank(userId) || StrUtils.isBlank(productId))
            return;

        HistoryProduct historyProduct = getBy(userId, productId);
        if (historyProduct != null) {
            int count = historyProduct.getCount() != null ? historyProduct.getCount() : 0;
            historyProduct.setCount(historyProduct.getCount() + 1);
            historyProduct.setUpdateBy(new User(userId));
        } else {
            historyProduct = new HistoryProduct();
            historyProduct.setProduct(new ShopProduct(productId));
            historyProduct.setUser(new User(userId));
            historyProduct.setCount(1);
            historyProduct.setCreateBy(new User(userId));
            historyProduct.setUpdateBy(new User(userId));
        }
        save(historyProduct);
    }
}
