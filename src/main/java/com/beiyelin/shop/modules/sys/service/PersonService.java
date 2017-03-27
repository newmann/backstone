/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.service;

import com.beiyelin.shop.common.security.Cryptos;
import com.beiyelin.shop.common.service.CrudService;
import com.beiyelin.shop.modules.sys.dao.PersonDao;
import com.beiyelin.shop.modules.sys.entity.Person;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 个人Service
 * @author Newmann
 * @version 2017-03-18
 */
@Service
@Transactional(readOnly = true)
public class PersonService extends CrudService<PersonDao, Person> {

    public Person getByMobile(String mobile) {
        if (StringUtils.isBlank(mobile))
            return null;

        Person person = new Person();
        person.setMobile(mobile);
        return dao.getByMobile(person);
    }

	public Person getByLoginName2(String loginName) {
		if (StringUtils.isBlank(loginName))
			return null;
		Person person = new Person();
		person.setLoginName(loginName);
		return dao.getByLoginName2(person);
	}

	/**
	 * 保存前台用户, 为了权限分配，该用户的company_id, office_id必须为Office.ID_FRONTEND_USER
	 * @param person
	 */
//	@Transactional(readOnly = false)
//	public void saveFrontendUser(Person person) {
//		Office company = new Office(Office.ID_FRONTEND_OFFICE);
//		Office office = new Office(Office.ID_FRONTEND_OFFICE);
//		person.setCompany(company);
//		person.setOffice(office);
//		save(person);
//
//		person.setRole(new Role(Role.ID_FRONTEND_ROLE));
//		dao.insertUserRole4Frontend(person);
//	}


}