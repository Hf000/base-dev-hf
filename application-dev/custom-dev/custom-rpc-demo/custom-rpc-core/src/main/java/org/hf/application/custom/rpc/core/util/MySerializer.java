package org.hf.application.custom.rpc.core.util;

public interface MySerializer {

    /**
     * 将对象序列化成字节数组
     *
     * @param obj
     * @return
     */
    <T> byte[] serialize(T obj);

    /**
     * 将字节数组反序列化成对象
     *
     * @param bytes
     * @param clazz
     * @return
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
