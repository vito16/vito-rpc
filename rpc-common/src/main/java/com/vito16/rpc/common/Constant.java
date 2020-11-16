package com.vito16.rpc.common;

/**
 * @author vito
 * @date 2020/11/10
 */
public class Constant {

    public static final String LOCALHOST_VALUE = "127.0.0.1";

    /**
     * 远程调用默认端口
     */
    public static final int PROTOCOL_PORT = 20990;
    public static final int ZK_SESSION_TIMEOUT = 5000;
    public static final int ZK_CONNECTION_TIMEOUT = 5000;
    public static final String ZK_REGISTRY_PATH = "/registry";
    public static final String ZK_CHILDREN_PATH = ZK_REGISTRY_PATH + "/vitorpc/";

}
