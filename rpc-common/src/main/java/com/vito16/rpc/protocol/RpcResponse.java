package com.vito16.rpc.protocol;

import lombok.Data;
import lombok.ToString;

/**
 * @author vito
 * @date 2020/11/10
 */
@Data
@ToString
public class RpcResponse {

    /**
     * 响应ID
     */
    private String requestId;
    /**
     * 错误信息
     */
    private String error;
    /**
     * 返回的结果
     */
    private Object result;

}
