package com.benyq.guochat.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author benyq
 * @time 2020/7/30
 * @e-mail 1520063035@qq.com
 * @note
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionCheck {
    public String[] checkString();
}