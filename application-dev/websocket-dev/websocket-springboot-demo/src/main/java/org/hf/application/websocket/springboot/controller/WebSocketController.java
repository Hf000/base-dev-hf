package org.hf.application.websocket.springboot.controller;

import org.hf.application.websocket.springboot.server.WebSocketServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 核心控制器
 * @author HF
 */
@RestController
public class WebSocketController {

    /**
    * 即时通讯
    */
    @RequestMapping("im")
    public ModelAndView pageIm() {
        return new ModelAndView("im");
    }

    /**
    * 消息推送
    */
    @RequestMapping("/push/{toUserId}")
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws Exception {
        return WebSocketServer.sendInfo(message, toUserId) ? ResponseEntity.ok("消息推送成功...") : ResponseEntity.ok("消息推送失败，用户不在线...");
    }

    /**
    * 多人聊天室
    */
    @RequestMapping("mcr")
    public ModelAndView pageMcr() {
        return new ModelAndView("mcr");
    }
}
