/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.service;

import com.beiyelin.shop.common.service.CrudService;
import com.beiyelin.shop.modules.shop.dao.OrderStatusProcessDao;
import com.beiyelin.shop.modules.shop.entity.OrderStatusProcess;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单状态流程集Service
 * @author Tony Wong
 * @version 2015-04-19
 */
@Service
@Transactional(readOnly = true)
public class OrderStatusProcessService extends CrudService<OrderStatusProcessDao, OrderStatusProcess> {

    public List<OrderStatusProcess> findByStatusUnion(String statusUnion) {
        OrderStatusProcess process = new OrderStatusProcess();
        process.setStatusUnion(statusUnion);
        return findList(process);
    }

}
