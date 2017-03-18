package com.beiyelin.shop.modules.shop.entity;

import com.beiyelin.shop.common.persistence.DataEntity;
import com.google.common.collect.Maps;
import com.beiyelin.shop.modules.sys.entity.User;
import org.restlet.data.Product;

import java.util.Map;

/**
 * @author Tony Wong
 */
public class CollectProduct extends DataEntity<CollectProduct> {

    private static final long serialVersionUID = 1L;

    private User user;
    private ShopProduct product;

    public CollectProduct() {
        super();
    }

    public CollectProduct(String id) {
        super();
        this.id = id;
    }

    /**
     * 只转化当前字段, 方便给json用
     * @return
     */
    public Map<String, Object> toSimpleObj() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("id", id);
        return map;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ShopProduct getProduct() {
        return product;
    }

    public void setProduct(ShopProduct product) {
        this.product = product;
    }
}
