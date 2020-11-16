package com.vito16.rpc.registry;

import java.util.Map;

/**
 * @author vito
 * @date 2020/11/10
 */
public interface ServiceRegistry {

    void registry(Map<String, Object> outServices) throws Exception;

}
