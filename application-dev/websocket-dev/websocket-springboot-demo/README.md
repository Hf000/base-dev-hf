# websocket-springboot-demo websocket的简单应用

## 1. websocket项目：集成websocket基础方法，连接，消息转发，关闭
## 2. 启动项目, 访问http://localhost/im, 为即时通讯页面
    使用方法: 
        1> 打开两个页面, 
        2> 修改不同页面的发送人和接收人用户名称,即可模拟对话
## 3. 启动项目, 访问http://localhost/mcr, 为多人聊天室页面
    使用方法:     
        1> 打开多个页面
        2> 修改用户名称并连接websocket即为进入聊天室
        3> 发送休息即开始对话
## 4. 集成证书配置: org.hf.application.websocket.springboot.config.HttpRedirectHttps, 配置文件application.yml文件需要新增证书相关配置
## 5. websocket相关面试题:
    (1) 如何保证websocket的性能稳定?
        1> 增加心跳机制、间隔轮询、自动重连等方式
