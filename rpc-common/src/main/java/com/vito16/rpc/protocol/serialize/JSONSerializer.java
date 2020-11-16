package com.vito16.rpc.protocol.serialize;

import com.alibaba.fastjson.JSON;

/**
 * @author vito
 * @date 2020/11/10
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
