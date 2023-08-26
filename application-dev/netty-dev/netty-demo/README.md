# netty-demo 
    netty的简单应用

## 1. org.hf.application.netty包下的server、client、codec、bytebuffer为netty样例的综合示例代码
    使用: 1. 启动org.hf.application.netty.test包下TestRpcServer
         2. 启动org.hf.application.netty.test包下TestRpcClient即可看见效果
## 2. org.hf.application.netty.demo为具体场景的netty样例
    1. org.hf.application.netty.demo.simple.codec 编解码器的简单类型解析应用
        使用:
        启动org.hf.application.netty.demo.simple.codec.MyRPCServer.main
        再启动org.hf.application.netty.demo.simple.codec.MyRPCClient.main即可
    2. org.hf.application.netty.demo.http.codec 编码器的http请求解析应用
        使用:
        启动org.hf.application.netty.demo.http.codec.NettyHttpServer.main
        然后浏览器输入:http://localhost:8089/?name=hufei即可
    3. org.hf.application.netty.demo.obj.codec 编解码器的对象解析应用
        使用:
        启动org.hf.application.netty.demo.obj.codec.NettyObjectServer.main
        再启动org.hf.application.netty.demo.obj.codec.NettyObjectClient.main即可
    4. org.hf.application.netty.demo.hessian.codec 编解码器的对象解析,Hessian序列化方式应用
        使用:
        启动org.hf.application.netty.demo.hessian.codec.NettyHessianServer.main
        再启动org.hf.application.netty.demo.hessian.codec.NettyHessianClient.main即可