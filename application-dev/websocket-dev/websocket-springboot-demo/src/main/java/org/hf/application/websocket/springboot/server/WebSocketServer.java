package org.hf.application.websocket.springboot.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:hufei
 * @CreateTime:2020-09-30
 * @Description:连接、消息管理
 */
@ServerEndpoint("/ws/{messageType}/{userId}")     //开启端点的方式进行访问注解
@Component                          //将该实例实例化到spring容器中
public class WebSocketServer {
    //日志
    static Log log = LogFactory.getLog(WebSocketServer.class);
    //在线数量
    private static final AtomicInteger onlineCount = new AtomicInteger(0);
    //处理客户端连接socket
    private static Map<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<String, WebSocketServer>();
    //会话信息
    private Session session;
    //用户信息
    private String userId = "";
    //聊天室
    private static Map<Integer, ConcurrentHashMap<String, WebSocketServer>> webSocketRoomMap = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, WebSocketServer>>();
    //当前聊天室频道
    private Integer roomNum = 0;
    //聊天室当前在线人数
    private static Map<Integer, AtomicInteger> roomCountMap = new ConcurrentHashMap<Integer, AtomicInteger>();

    /**
    *@params: [userId, messageType, session]
    *@return: void
    *@description: 打开WebSokcet连接
    *@author: hufei
    *@time: 2020/10/10 10:32
    */
    @OnOpen
    public void onOPen(@PathParam("userId") String userId, @PathParam("messageType") Integer messageType, Session session) {
        try {
            //处理session和用户信息
            this.session = session;
            this.userId = userId;
            this.roomNum = messageType;
            if (roomNum == 0) {
                if (webSocketMap.containsKey(userId)) {
                    webSocketMap.remove(userId);
                    webSocketMap.put(userId, this);
                } else {
                    webSocketMap.put(userId, this);
                    //增加在线人数
                    addOnlineCount(null);
                }
                //处理连接成功消息的发送
                sendMessage("Server>>>>远程WebSoket连接成功");
                log.info("用户" + userId + "成功连接，当前的在线人数为" + getOnlineCount(null));
            } else if(roomNum == 1) {
                ConcurrentHashMap<String, WebSocketServer> roomMap = null;
                AtomicInteger roomOnlineCount = null;
                if (webSocketRoomMap.containsKey(roomNum)) {
                    roomMap = webSocketRoomMap.get(roomNum);
                    if (roomCountMap.containsKey(roomNum)) {
                        roomOnlineCount = roomCountMap.get(roomNum);
                    } else {
                        roomOnlineCount = new AtomicInteger(0);
                        roomCountMap.put(roomNum, roomOnlineCount);
                    }
                    if (roomMap.containsKey(userId)) {
                        roomMap.remove(userId);
                        roomMap.put(userId, this);
                    } else {
                        roomMap.put(userId, this);
                        addOnlineCount(roomOnlineCount);
                    }
                } else {
                    roomMap = new ConcurrentHashMap<String, WebSocketServer>();
                    roomMap.put(userId, this);
                    webSocketRoomMap.put(roomNum, roomMap);
                    if (roomCountMap.containsKey(roomNum)) {
                        roomCountMap.remove(roomNum);
                    }
                    roomOnlineCount = new AtomicInteger(0);
                    addOnlineCount(roomOnlineCount);
                    roomCountMap.put(roomNum, roomOnlineCount);
                }
                sendMessageMCR("上线成功");
                log.info("用户" + userId + "成功连接，聊天室当前的在线人数为" + getOnlineCount(roomOnlineCount));
            } else {
                sendMessage("Server>>>>远程WebSoket连接失败");
            }
        } catch (Exception e) {
            log.error("打开websocket连接异常！");
        }
    }

    /**
    *@params: [message]
    *@return: void
    *@description: 服务端向客户端发送数据
    *@author: hufei
    *@time: 2020/10/10 10:35
    */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message == null ? "" : message);
            log.info("websocket连接成功，服务端向客户端发送连接成功消息");
        } catch (IOException e) {
            log.error("websocket服务端向客户端发送消息异常！");
        }
    }

    /**
    *@params: [roomOnlineCount]
    *@return: void
    *@description: 增加在线人数
    *@author: hufei
    *@time: 2020/10/10 10:34
    */
    public static synchronized void addOnlineCount(AtomicInteger roomOnlineCount) {
        if (roomOnlineCount != null){
            roomOnlineCount.incrementAndGet();
            log.info("聊天室当前在线人数加1，总的在线人数：" + getOnlineCount(roomOnlineCount));
        } else {
            WebSocketServer.onlineCount.incrementAndGet();
            log.info("websocket连接成功，当前在线人数加1，总的在线人数：" + getOnlineCount(null));
        }
    }

    /**
    *@params: [roomOnlineCount]
    *@return: AtomicInteger
    *@description: 获取在线人数的数量
    *@author: hufei
    *@time: 2020/10/10 10:36
    */
    public static synchronized AtomicInteger getOnlineCount(AtomicInteger roomOnlineCount) {
        if (roomOnlineCount != null) {
            return roomOnlineCount;
        } else {
            return onlineCount;
        }
    }

    /**
    *@params: []
    *@return: void
    *@description: 关闭连接
    *@author: hufei
    *@time: 2020/10/10 10:37
    */
    @OnClose
    public void onClose() {
        try {
            String msg = "用户退出....";
            if (roomNum == 0) {
                if (webSocketMap.containsKey(userId)) {
                    webSocketMap.remove(userId);
                    subOnlineCount(null);
                }
            } else if (roomNum == 1) {
                if (webSocketRoomMap.containsKey(roomNum)) {
                    ConcurrentHashMap<String, WebSocketServer> roomMap = webSocketRoomMap.get(roomNum);
                    if (roomMap.containsKey(userId)) {
                        roomMap.remove(userId);
                        if (roomCountMap.containsKey(roomNum)) {
                            AtomicInteger roomOnlineCount = roomCountMap.get(roomNum);
                            subOnlineCount(roomOnlineCount);
                        }
                    }
                }
            } else {
                msg = "用户退出失败....";
            }
            log.info(msg);
        } catch (Exception e) {
            log.error("客户端关闭连接异常！");
        }
    }

    /**
    *@params: [roomOnlineCount]
    *@return: void
    *@description: 用户下线，当前在线人数减1
    *@author: hufei
    *@time: 2020/10/10 10:37
    */
    public static synchronized void subOnlineCount(AtomicInteger roomOnlineCount) {
        if (roomOnlineCount != null) {
            roomOnlineCount.decrementAndGet();
            log.info("聊天室当前在线人数减1，总的在线人数：" + getOnlineCount(roomOnlineCount));
        } else {
            WebSocketServer.onlineCount.decrementAndGet();
            log.info("websocket连接关闭成功，当前在线人数减1，总的在线人数：" + getOnlineCount(null));
        }
    }

    /**
    *@params: [message, session]
    *@return: void
    *@description: 消息中转
    *@author: hufei
    *@time: 2020/10/10 10:38
    */
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            if (StringUtils.isNotEmpty(message)) {
                //解析消息
                JSONObject jsonObject = JSON.parseObject(message);
                String toUserId = jsonObject.getString("toUserId");
                String msg = jsonObject.getString("msg");
                int type = jsonObject.getInteger("type");
                if (type == 0) {
                    if (StringUtils.isNotEmpty(toUserId) && webSocketMap.containsKey(toUserId)) {
                        if (toUserId.equals(userId)) {
                            msg = "不能自己给自己发送消息";
                            webSocketMap.get(toUserId).sendMessage(msg);
                        } else {
                            webSocketMap.get(toUserId).sendMessage(msg);
                            sendMessage("发送消息成功！");
                        }
                    } else {
                        sendMessage("您推送消息的目标用户当前不在线，消息推送失败！");
                    }
                } else if (type == 1) {
                    sendMessageMCR(msg);
                } else {
                    sendMessage("发送消息有误！");
                }
            }
        } catch (Exception e) {
            log.error("消息中转发送异常！");
        }
    }

    /**
    *@params: [msg]
    *@return: void
    *@description: 多人聊天发送消息
    *@author: hufei
    *@time: 2020/10/10 13:12
    */
    private void sendMessageMCR(String msg) {
        if (webSocketRoomMap.containsKey(roomNum)) {
            ConcurrentHashMap<String, WebSocketServer> roomMap = webSocketRoomMap.get(roomNum);
            Iterator<String> iterator = roomMap.keySet().iterator();
            while(iterator.hasNext()) {
                String toUserId = iterator.next();
                if (toUserId.equals(userId)) {
                    sendMessage(msg);
                } else {
                    roomMap.get(toUserId).sendMessage(userId + ":" + msg);
                }
            }
        }
    }

    /**
    *@params: [message, userId]
    *@return: boolean
    *@description: 服务器消息推送
    *@author: hufei
    *@time: 2020/10/10 10:38
    */
    public static boolean sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        boolean flag = false;
        try {
            if (StringUtils.isNotEmpty(userId) && webSocketMap.containsKey(userId)) {
                webSocketMap.get(userId).sendMessage(message);
                flag = true;
            } else {
                log.info("用户" + userId + "不在线");
                flag = false;
            }
        } catch (Exception e) {
            log.error("消息推送异常！");
        }
        return flag;
    }

}
