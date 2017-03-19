package com.beiyelin.shop.common.security.authority.annotation;

import java.lang.annotation.*;

/**
 * Created by newmann on 2017/3/19.
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionControl {
    String[] value();
}
