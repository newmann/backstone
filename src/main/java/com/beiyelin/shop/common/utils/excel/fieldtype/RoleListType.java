/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.common.utils.excel.fieldtype;

import java.util.List;

import com.beiyelin.shop.common.utils.Collections3;
import com.beiyelin.shop.common.utils.StrUtils;
import com.beiyelin.shop.modules.sys.entity.Role;
import com.google.common.collect.Lists;
import com.beiyelin.shop.common.utils.SpringContextHolder;
import com.beiyelin.shop.modules.sys.service.SystemService;

/**
 * 字段类型转换
 * @author Tony Wong
 * @version 2013-5-29
 */
public class RoleListType {

	private static SystemService systemService = SpringContextHolder.getBean(SystemService.class);
	
	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		List<Role> roleList = Lists.newArrayList();
		List<Role> allRoleList = systemService.findAllRole();
		for (String s : StrUtils.split(val, ",")){
			for (Role e : allRoleList){
				if (StrUtils.trimToEmpty(s).equals(e.getName())){
					roleList.add(e);
				}
			}
		}
		return roleList.size()>0?roleList:null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null){
			@SuppressWarnings("unchecked")
			List<Role> roleList = (List<Role>)val;
			return Collections3.extractToString(roleList, "name", ", ");
		}
		return "";
	}
	
}
