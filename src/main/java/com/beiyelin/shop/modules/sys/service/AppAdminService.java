/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.service;

import com.beiyelin.shop.common.service.AppAdminCrudService;
import com.beiyelin.shop.modules.sys.dao.AppAdminDao;
import com.beiyelin.shop.modules.sys.entity.AppAdmin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理员Service
 * @author Newmann
 * @version 2017-04-8
 */
@Service
@Transactional(readOnly = true)
public class AppAdminService extends AppAdminCrudService<AppAdminDao, AppAdmin> {

    public AppAdmin getByMobile(String mobile) {
        if (StringUtils.isBlank(mobile))
            return null;

		AppAdmin appAdmin = new AppAdmin();
        appAdmin.setMobile(mobile);
        return dao.getByMobile(appAdmin);
    }

	public AppAdmin getByLoginName2(String loginName) {
		if (StringUtils.isBlank(loginName))
			return null;
		AppAdmin appAdmin = new AppAdmin();
		appAdmin.setLoginName(loginName);
		return dao.getByLoginName2(appAdmin);
	}


}