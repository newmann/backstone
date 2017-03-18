/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.common.persistence;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

/**
 * 可排序的数据Entity类
 * @author Tony Wong
 * @version 2014-05-16
 */
public abstract class SortEntity<T> extends DataEntity<T> {

	private static final long serialVersionUID = 1L;

	protected Integer sort;		// 排序

	public SortEntity() {
		super();
		sort = 999;
	}

	public SortEntity(String id) {
		super(id);
		sort = 999;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
