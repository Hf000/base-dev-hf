# custom-rpc-demo 自定义实现rpc示例demo

## 注意:  (目前已删除)
    1> 这里的arget -> generated-test-sources -> test-annotations这个三个文件夹是因为在custom-rpc-demo这个pom类型的模块下的test -> 
    java的Test Sources Root文件下写了单元测试类

## 自定义RPC启动使用方式
    1. 启动custom-rpc-server工程下的org.hf.application.custom.rpc.order.MyServer.main
    2. 再启动custom-rpc-client工程下的org.hf.application.custom.rpc.order.ClientServer.main即可实现通讯
    这里是模拟订单提交
