package com.vito16.rpc;

import com.vito16.rpc.common.RpcProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author vito
 * @date 2020/11/8
 */
@EnableConfigurationProperties
@SpringBootApplication
@ComponentScan({"com.vito16"})
public class HelloApp {

    public static void main(String[] args) {
        SpringApplication.run(HelloApp.class);
    }

}
