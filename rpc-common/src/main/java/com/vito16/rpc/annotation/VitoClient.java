package com.vito16.rpc.annotation;

import java.lang.annotation.*;

/**
 * 服务调用方
 *
 * @author vito
 * @date 2020/11/8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VitoClient {

    Class service();

}
