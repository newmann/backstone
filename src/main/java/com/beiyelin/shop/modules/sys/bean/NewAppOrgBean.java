package com.beiyelin.shop.modules.sys.bean;

import lombok.Data;

/**
 * Created by newmann on 2017/4/15.
 * 新增组织的时候，从前端提交到后台的数据，主要是包括了组织对应管理员的密码
 */
@Data
public class NewAppOrgBean {
    private String code;
    private String name;
    private int orgType;
    private String remarks;
    private String  password;
}
