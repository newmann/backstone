/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.cms.service;

import com.beiyelin.shop.common.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beiyelin.shop.modules.cms.dao.ArticleDataDao;
import com.beiyelin.shop.modules.cms.entity.ArticleData;

/**
 * 站点Service
 * @author Tony Wong
 * @version 2013-01-15
 */
@Service
@Transactional(readOnly = true)
public class ArticleDataService extends CrudService<ArticleDataDao, ArticleData> {

}
