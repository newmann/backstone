/**
 * Copyright &copy; 2012-2014 <a href="http://www.iwantclick.com">iWantClick</a>iwc.shop All rights reserved.
 */
package com.beiyelin.shop.common.persistence;

import com.beiyelin.shop.common.utils.IdGen;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

import static com.beiyelin.shop.common.config.EntityDelFlagConst.NORMAL;

/**
 * 数据Entity类
 * @author Newmann
 * @version 2017-04-08
 */
@Data
public abstract class AppAdminDataEntity<T> extends BaseEntity<T> {

	private static final long serialVersionUID = 1L;
	@Length(min=0, max=255)
	protected String remarks;	// 备注
	@JsonIgnore
	protected String createBy;	// 创建者
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date createDate;	// 创建日期
	@JsonIgnore
	protected String  updateBy;	// 更新者
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	protected Date updateDate;	// 更新日期

	@JsonIgnore
	@Length(min=1, max=1)
	protected String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）

	/**
	 * 当前操作员id
	 */
	protected String currentOperatorId;


	public AppAdminDataEntity() {
		super();
		this.delFlag = NORMAL;
	}

	public AppAdminDataEntity(String id) {
		super(id);
		this.delFlag = NORMAL;
	}
	
	/**
	 * 插入之前执行方法，需要手动调用
	 */
	@Override
	public void preInsert(){
		// 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
		if (!this.isNewRecord){
			setId(IdGen.uuid());
		}
		this.createBy = getCurrentOperatorId();
        this.updateBy = getCurrentOperatorId();
		this.createDate = new Date();
		this.updateDate = this.createDate;
	}
	
	/**
	 * 更新之前执行方法，需要手动调用
	 */
	@Override
	public void preUpdate(){
		this.updateBy = getCurrentOperatorId();
		this.updateDate = new Date();
	}
	

}
