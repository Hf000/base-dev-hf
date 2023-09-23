package org.hf.application.websocket.springboot.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 连接、消息管理
 * //@ServerEndpoint("/ws/{messageType}/{userId}")  //开启端点的方式进行访问注解
 * //@Component    //将该实例实例化到spring容器中
 */
@ServerEndpoint("/ws/{messageType}/{userId}")
@Component
public class WebSocketServer {

    /**
     * 日志
     */
    static Log log = LogFactory.getLog(WebSocketServer.class);
    /**
     * 在线数量
     */
    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    /**
     * 处理客户端连接socket
     */
    private static final Map<String, WebSocketServer> WEBSOCKET_MAP = new ConcurrentHashMap<String, WebSocketServer>();
    /**
     * 会话信息
     */
    private Session session;
    /**
     * 用户信息
     */
    private String userId = "";
    /**
     * 聊天室
     */
    private static final Map<Integer, ConcurrentHashMap<String, WebSocketServer>> WEBSOCKET_ROOM_MAP = new ConcurrentHashMap<Integer, ConcurrentHashMap<String, WebSocketServer>>();
    /**
     * 当前聊天室频道
     */
    private Integer roomNum = 0;
    /**
     * 聊天室当前在线人数
     */
    private static final Map<Integer, AtomicInteger> ROOM_COUNT_MAP = new ConcurrentHashMap<Integer, AtomicInteger>();

    /**
     * 打开WebSokcet连接
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, @PathParam("messageType") Integer messageType, Session session) {
        try {
            //处理session和用户信息
            this.session = session;
            this.userId = userId;
            this.roomNum = messageType;
            if (roomNum == 0) {
                if (WEBSOCKET_MAP.containsKey(userId)) {
                    WEBSOCKET_MAP.remove(userId);
                    WEBSOCKET_MAP.put(userId, this);
                } else {
                    WEBSOCKET_MAP.put(userId, this);
                    //增加在线人数
                    addOnlineCount(null);
                }
                //处理连接成功消息的发送
                sendMessage("Server>>>>远程WebSoket连接成功");
                log.info("用户" + userId + "成功连接，当前的在线人数为" + getOnlineCount(null));
            } else if (roomNum == 1) {
                ConcurrentHashMap<String, WebSocketServer> roomMap = null;
                AtomicInteger roomOnlineCount = null;
                if (WEBSOCKET_ROOM_MAP.containsKey(roomNum)) {
                    roomMap = WEBSOCKET_ROOM_MAP.get(roomNum);
                    if (ROOM_COUNT_MAP.containsKey(roomNum)) {
                        roomOnlineCount = ROOM_COUNT_MAP.get(roomNum);
                    } else {
                        roomOnlineCount = new AtomicInteger(0);
                        ROOM_COUNT_MAP.put(roomNum, roomOnlineCount);
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
                    WEBSOCKET_ROOM_MAP.put(roomNum, roomMap);
                    ROOM_COUNT_MAP.remove(roomNum);
                    roomOnlineCount = new AtomicInteger(0);
                    addOnlineCount(roomOnlineCount);
                    ROOM_COUNT_MAP.put(roomNum, roomOnlineCount);
                }
                sendMessageMcr("上线成功");
                log.info("用户" + userId + "成功连接，聊天室当前的在线人数为" + getOnlineCount(roomOnlineCount));
            } else {
                sendMessage("Server>>>>远程WebSoket连接失败");
            }
        } catch (Exception e) {
            log.error("打开websocket连接异常！");
        }
    }

    /**
     * 服务端向客户端发送数据
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
     * 增加在线人数
     */
    public static synchronized void addOnlineCount(AtomicInteger roomOnlineCount) {
        if (roomOnlineCount != null) {
            roomOnlineCount.incrementAndGet();
            log.info("聊天室当前在线人数加1，总的在线人数：" + getOnlineCount(roomOnlineCount));
        } else {
            WebSocketServer.ONLINE_COUNT.incrementAndGet();
            log.info("websocket连接成功，当前在线人数加1，总的在线人数：" + getOnlineCount(null));
        }
    }

    /**
     * 获取在线人数的数量
     */
    public static synchronized AtomicInteger getOnlineCount(AtomicInteger roomOnlineCount) {
        if (roomOnlineCount != null) {
            return roomOnlineCount;
        } else {
            return ONLINE_COUNT;
        }
    }

    /**
     * 关闭连接
     */
    @OnClose
    public void onClose() {
        try {
            String msg = "用户退出....";
            if (roomNum == 0) {
                if (WEBSOCKET_MAP.containsKey(userId)) {
                    WEBSOCKET_MAP.remove(userId);
                    subOnlineCount(null);
                }
            } else if (roomNum == 1) {
                if (WEBSOCKET_ROOM_MAP.containsKey(roomNum)) {
                    ConcurrentHashMap<String, WebSocketServer> roomMap = WEBSOCKET_ROOM_MAP.get(roomNum);
                    if (roomMap.containsKey(userId)) {
                        roomMap.remove(userId);
                        if (ROOM_COUNT_MAP.containsKey(roomNum)) {
                            AtomicInteger roomOnlineCount = ROOM_COUNT_MAP.get(roomNum);
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
     * 用户下线，当前在线人数减1
     */
    public static synchronized void subOnlineCount(AtomicInteger roomOnlineCount) {
        if (roomOnlineCount != null) {
            roomOnlineCount.decrementAndGet();
            log.info("聊天室当前在线人数减1，总的在线人数：" + getOnlineCount(roomOnlineCount));
        } else {
            WebSocketServer.ONLINE_COUNT.decrementAndGet();
            log.info("websocket连接关闭成功，当前在线人数减1，总的在线人数：" + getOnlineCount(null));
        }
    }

    /**
     * 消息中转
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
                    if (StringUtils.isNotEmpty(toUserId) && WEBSOCKET_MAP.containsKey(toUserId)) {
                        if (toUserId.equals(userId)) {
                            msg = "不能自己给自己发送消息";
                            WEBSOCKET_MAP.get(toUserId).sendMessage(msg);
                        } else {
                            WEBSOCKET_MAP.get(toUserId).sendMessage(msg);
                            sendMessage("发送消息成功！");
                        }
                    } else {
                        sendMessage("您推送消息的目标用户当前不在线，消息推送失败！");
                    }
                } else if (type == 1) {
                    sendMessageMcr(msg);
                } else {
                    sendMessage("发送消息有误！");
                }
            }
        } catch (Exception e) {
            log.error("消息中转发送异常！");
        }
    }

    /**
     * 多人聊天发送消息
     */
    private void sendMessageMcr(String msg) {
        if (WEBSOCKET_ROOM_MAP.containsKey(roomNum)) {
            ConcurrentHashMap<String, WebSocketServer> roomMap = WEBSOCKET_ROOM_MAP.get(roomNum);
            for (String toUserId : roomMap.keySet()) {
                if (toUserId.equals(userId)) {
                    sendMessage(msg);
                } else {
                    roomMap.get(toUserId).sendMessage(userId + ":" + msg);
                }
            }
        }
    }

    /**
     * 服务器消息推送
     */
    public static boolean sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        boolean flag = false;
        try {
            if (StringUtils.isNotEmpty(userId) && WEBSOCKET_MAP.containsKey(userId)) {
                WEBSOCKET_MAP.get(userId).sendMessage(message);
                flag = true;
            } else {
                log.info("用户" + userId + "不在线");
                return false;
            }
        } catch (Exception e) {
            log.error("消息推送异常！");
        }
        return flag;
    }

    /**
     * 错误处理
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误：" + this.userId + "，原因：" + error.getMessage());
    }
}