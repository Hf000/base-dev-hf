package org.hf.application.custom.rpc.core.util;

public interface MySerializer {

    /**
     * 将对象序列化成字节数组
     * @param obj 消息内容
     * @return byte[]
     */
    <T> byte[] serialize(T obj);

    /**
     * 将字节数组反序列化成对象
     * @param bytes 字节内容
     * @param clazz 类型
     * @return T
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
