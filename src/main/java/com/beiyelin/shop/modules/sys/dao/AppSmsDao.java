package com.beiyelin.shop.modules.sys.dao;

import com.beiyelin.shop.common.persistence.CrudDao;
import com.beiyelin.shop.common.persistence.annotation.MyBatisDao;
import com.beiyelin.shop.modules.sys.entity.AppSms;


/**
 * 短信发送接口
 * @author Newmann
 * @version 2017-03-26
 */
@MyBatisDao
public interface AppSmsDao extends CrudDao<AppSms> {

    /**
     * 通过 code，type，mobile查找最后一条验证码
     */
    public AppSms findLastOneBy(AppSms appSms);

}
