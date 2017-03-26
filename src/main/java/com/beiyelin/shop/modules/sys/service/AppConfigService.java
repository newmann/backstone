/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.service;

import com.beiyelin.shop.common.utils.JedisUtils;

import org.springframework.stereotype.Service;


/**
 * 系统配置Service
 * @author Newmann
 * @version 2017-03-18
 */
@Service
public class AppConfigService {


    public static final String ORG_CONFIG_NAME = "orgId";
    private static final String PERSON_CONFIG_NAME = "personId";
    /**
     * 獲取最新的組織序號
     */
    public Long getLastOrgID() {
        Long id = JedisUtils.Incr(ORG_CONFIG_NAME);
        if (id.equals(0)){
            //设置组织的起始序号
            JedisUtils.set(ORG_CONFIG_NAME, "100000");
            id = 100000L;
        }
        return  id;
    }

    /**
     * 獲取最新的个人序號
     */
    public Long getLastPersonID() {
        Long id = JedisUtils.Incr(PERSON_CONFIG_NAME);
        if (id.equals(0)){
            //设置组织的起始序号
            JedisUtils.set(PERSON_CONFIG_NAME, "100000");
            id = 100000L;
        }
        return  id;
    }

}