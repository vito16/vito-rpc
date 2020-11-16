package com.vito16.rpc.client;

import com.vito16.rpc.sdk.Hello;

/**
 * @author vito
 * @date 2020/11/12
 */
public class ClientService {

    Hello hello;

    public void abc(){
        hello.world();
    }

}
