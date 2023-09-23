# socket-demo socket的简单应用

## 1. socket的简单应用展示示例
## 2. org.hf.application.socket.SocketClient: socket客户端demo
## 3. org.hf.application.socket.SocketServer: socket服务端demo,这里是同步处理客户端连接,如果有客户端连接未释放,则无法处理下一个客户端连接
## 4. org.hf.application.socket.SocketNioServer: socket服务端demo,这里采用NIO异步方式处理客户端连接
## 5. 使用方式:
        1> 先启动socket服务端demo: org.hf.application.socket.SocketServer.main或者org.hf.application.socket.SocketNioServer.main
        2> 然后启动socket客户端demo: org.hf.application.socket.SocketClient.main,在控制台输入内容即可,服务就会接收到消息