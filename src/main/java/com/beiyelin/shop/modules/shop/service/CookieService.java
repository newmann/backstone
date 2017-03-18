/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.shop.service;

import com.beiyelin.shop.common.service.CrudService;
import com.beiyelin.shop.modules.shop.dao.CookieDao;
import com.beiyelin.shop.modules.shop.entity.Cookie;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie Service
 * @author Tony Wong
 * @version 2015-04-16
 */
@Service
@Transactional(readOnly = true)
public class CookieService extends CrudService<CookieDao, Cookie> {
}
