# netty-tcp-demo
聚合工程

## netty-tcp-demo 基于netty的tcp长连接管理
1. 基于netty、redis和springboot框架构建
2. 数据流: 生产者->消息队列->消费者(客户端)->tcp通道->服务端->tcp通道->客户端
   1> netty-tcp-client: 生产者->消息队列->消费者(客户端)->tcp通道->
   2> netty-tcp-server: ->服务端->tcp通道->客户端
3. 使用说明
   1> 修改netty-tcp-client下的application.yml配置文件，修改redis的连接信息
   2> 先后启动netty-tcp-server和netty-tcp-client项目，访问http://localhost:8088/demo/testOne开始测试