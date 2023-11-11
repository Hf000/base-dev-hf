package org.hf.application.netty.tcp.client.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hf
 * 自定义实现tcp消息客户端 - 5 消息模型实体
 */
@Data
@Accessors(chain = true)
public class NettyMsgModel implements Serializable {

    private static final long serialVersionUID = -6817946938300688950L;

    /**
     * 身份序列号
     */
    private String imei;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 业务数据
     */
    private Map<String, Object> bizData;

    public static NettyMsgModel create(String imei, String msg) {
        return new NettyMsgModel().setBizData(new HashMap<>()).setMsg(msg).setImei(imei);
    }
}