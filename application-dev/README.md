# application-dev
应用聚合工程

## 1. java-base-demo java基础知识样例
## 2. mybatis-dev mybatis的相关应用
## 3. websocket-dev websocket的相关应用
## 4. dubbo-dev dubbo的相关应用
## 5. netty-dev netty的相关应用
## 6. mongodb-dev mongoDB的相关应用
## 7. custom-dev 自定义实现的一些组件或者应用功能

## 注意:
    1> 如果在pom类型的模块下的test -> java的Test Sources Root文件下写了单元测试类, 那么会在此工程目录下额外
        生成arget -> generated-test-sources -> test-annotations这三个文件夹, 所以一般避免这样做(不推荐)