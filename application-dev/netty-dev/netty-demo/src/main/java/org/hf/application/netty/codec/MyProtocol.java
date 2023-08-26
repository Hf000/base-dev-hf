package org.hf.application.netty.codec;

/**
 * 自定义协议
 * 用于解决TCP的粘包/拆包问题
 * @author hf
 */
public class MyProtocol {

    /**
     * 数据头：长度
     */
    private Integer length;

    /**
     * 数据体
     */
    private byte[] body;

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}