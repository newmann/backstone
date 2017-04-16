/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.modules.sys.entity;

import com.beiyelin.shop.common.persistence.AppAdminDataEntity;
import com.beiyelin.shop.common.utils.excel.annotation.ExcelField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * 组织Entity
 * @author Newmann HU
 * @version 2017-4-11
 */
@Data
public class AppOrg extends AppAdminDataEntity<AppOrg> {



	private static final long serialVersionUID = 1L;

	@ExcelField(title="组织代码", align=2, sort=40)
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	private String code;	// 组织名称

	@ExcelField(title="组织名称", align=2, sort=40)
	@Length(min=1, max=100, message="姓名长度必须介于 1 和 100 之间")
	private String name;	// 组织名称

	@ExcelField(title="组织类型", align=1, sort=50)
	private int orgType;	// 组织类型

	private  String dbUrl;	//所属数据库


}