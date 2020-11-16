package com.vito16.rpc.service;

import com.vito16.rpc.annotation.VitoService;
import com.vito16.rpc.sdk.Hello;
import org.springframework.stereotype.Component;

/**
 * @author vito
 * @date 2020/11/8
 */
@Component
@VitoService(service = Hello.class)
public class HelloImpl implements Hello {

    @Override
    public String world() {
        return "hello world";
    }

}
