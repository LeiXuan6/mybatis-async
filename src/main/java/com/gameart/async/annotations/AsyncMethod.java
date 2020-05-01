package com.gameart.async.annotations;

import java.lang.annotation.*;

/***异步方法
 *@author JackLei
 *@Date 下午 2:39 2018/8/31
 ***/
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AsyncMethod {
    AsyncType type();
}
