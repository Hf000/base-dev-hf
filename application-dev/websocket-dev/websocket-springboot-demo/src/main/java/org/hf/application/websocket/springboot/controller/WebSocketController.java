package org.hf.application.websocket.springboot.controller;

import org.hf.application.websocket.springboot.server.WebSocketServer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author:hufei
 * @CreateTime:2020-09-30
 * @Description:核心控制器
 */
@RestController
public class WebSocketController {
    /**
    *@params: []
    *@return: ModelAndView
    *@description: 即时通讯
    *@author: hufei
    *@time: 2020/10/10 10:23
    */
    @RequestMapping("im")
    public ModelAndView pageIM() {
        return new ModelAndView("im");
    }

    /**
    *@params: [message, toUserId]
    *@return: ResponseEntity<String>
    *@description: 消息推送
    *@author: hufei
    *@time: 2020/10/10 10:19
    */
    @RequestMapping("/push/{toUserId}")
    public ResponseEntity<String> pushToWeb(String message, @PathVariable String toUserId) throws Exception {
        boolean flag = WebSocketServer.sendInfo(message, toUserId);
        return flag == true ? ResponseEntity.ok("消息推送成功...") : ResponseEntity.ok("消息推送失败，用户不在线...");
    }

    /**
    *@params: []
    *@return: ModelAndView
    *@description: 多人聊天室
    *@author: hufei
    *@time: 2020/10/10 11:49
    */
    @RequestMapping("mcr")
    public ModelAndView pageMCR() {
        return new ModelAndView("mcr");
    }


}
