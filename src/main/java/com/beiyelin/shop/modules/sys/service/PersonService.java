/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.service;

import com.beiyelin.shop.common.security.Cryptos;
import com.beiyelin.shop.common.service.CrudService;
import com.beiyelin.shop.modules.sys.dao.PersonDao;
import com.beiyelin.shop.modules.sys.entity.Office;
import com.beiyelin.shop.modules.sys.entity.Person;
import com.beiyelin.shop.modules.sys.entity.Role;
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



    /**=======================
     * 自建会话系统给app用
     * 判断用户是否登录的条件：person.id + person.app_login_token
     =======================*/

    /**
     * 生成APP用户登录令牌（32位）
     */
    public String genAppLoginToken() {
        String token = Cryptos.generateAesKeyString();
        if (token.length() > 32) {
            token = token.substring(0, 31);
        }
        return token;
    }

    /**
     * 更新APP用户登录令牌
     */
    @Transactional(readOnly = false)
    public void updateAppLoginToken(String userId, String token) {
        //remove cache
        Person person = new Person(userId);
        person.setAppLoginToken(token);
        dao.updateAppLoginToken(person);
    }

    /**
     * 更新APP用户登录令牌
     */
    @Transactional(readOnly = false)
    public void updateAppLoginToken(Person person) {
        //remove cache
        dao.updateAppLoginToken(person);
    }

    public boolean isAppLoggedIn(String personId, String token) {
        if (StringUtils.isNotBlank(personId) && StringUtils.isNotBlank(token)) {
            Person person = new Person(personId);
            person.setAppLoginToken(token);
            long count = dao.isAppLoggedIn(person);
            if (count > 0)
                return true;
            else
                return false;
        } else {
            return false;
        }
    }
}