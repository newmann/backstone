package com.beiyelin.shop.modules.sys.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.sys.entity.Sms;

/**
 * 短信发送接口
 * @author Tony Wong
 * @version 2014-05-16
 */
@MyBatisDao
public interface SmsDao extends CrudDao<Sms> {

    /**
     * 通过 code，type，mobile查找最后一条验证码
     */
    public Sms findLastOneBy(Sms sms);

}
