package com.vito16.rpc.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author vito
 * @date 2020/11/10
 */
@Data
@ConfigurationProperties(prefix = "vito.rpc")
public class RpcProperties {

    private Consumer consumer;

    private Provider provider;

    private String zkConn;

    public static class Provider {
        private String host;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }
    public static class Consumer {
        private String host;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }
    }

}
