/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.service;

import com.beiyelin.shop.common.service.AppAdminCrudService;
import com.beiyelin.shop.modules.sys.bean.NewAppOrgBean;
import com.beiyelin.shop.modules.sys.dao.AppAdminDao;
import com.beiyelin.shop.modules.sys.dao.AppOrgDao;
import com.beiyelin.shop.modules.sys.entity.AppAdmin;
import com.beiyelin.shop.modules.sys.entity.AppOrg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理员Service
 * @author Newmann
 * @version 2017-04-11
 */
@Service
@Transactional(readOnly = true)
public class AppOrgService extends AppAdminCrudService<AppOrgDao, AppOrg> {
    @Autowired
    protected AppAdminDao appAdminDao;


    public AppOrg getByCode(String code) {
        if (StringUtils.isBlank(code))
            return null;

		AppOrg appOrg = new AppOrg();
        appOrg.setCode(code);
        return dao.getByCode(appOrg);
    }

    /**
     * 新增组织
     * 在新增组织的同时，需要新增对应的管理员账户，管理员账户的名称就是组织的名称
     *
     */
    @Transactional(readOnly = false)
    public void newAppOrg(NewAppOrgBean newAppOrgBean,String curOperatorId){
        AppOrg appOrg = new AppOrg();
        AppAdmin appAdmin = new AppAdmin();
        //生成组织
        BeanUtils.copyProperties(newAppOrgBean,appOrg);
//        appOrg.setCode(newAppOrgBean.getCode());
//        appOrg.setName(newAppOrgBean.getName());
//        appOrg.setOrgType(newAppOrgBean.getOrgType());
//        appOrg.setRemarks(newAppOrgBean.getRemarks());
        appOrg.setCurrentOperatorId(curOperatorId);
        appOrg.preInsert();
        //生成对应的管理员账户
        appAdmin.setLoginName(newAppOrgBean.getCode());
        appAdmin.setName(newAppOrgBean.getName());
        appAdmin.setPassword(newAppOrgBean.getPassword());
        appAdmin.setOrgId(appOrg.getId());//对应的组织id
        appAdmin.setCurrentOperatorId(curOperatorId);

        appAdmin.preInsert();

        dao.insert(appOrg);
        appAdminDao.insert(appAdmin);
    }

}