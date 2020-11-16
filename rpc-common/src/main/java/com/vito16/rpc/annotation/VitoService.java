package com.vito16.rpc.annotation;

import java.lang.annotation.*;

/**
 * 服务暴露方
 *
 * @author vito
 * @date 2020/11/8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VitoService {

    Class service();

}
