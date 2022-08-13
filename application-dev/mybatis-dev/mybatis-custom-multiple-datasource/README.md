# mybatis-custom-multiple-datasource
自定义多数据源注解

# 1. springboot整合mybatis和tk.mapper的简单应用
# 2. 自定义注解多数据源, 数据连接池采用springboot默认的HikariPool
    用法: 
    1> 多数据源配置参考application.yml
        spring:     #spring配置项
          datasource:
            master:
              jdbc-url: jdbc:mysql://127.0.0.1:3306/springboot_test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false
              username: root
              password: root
              driver-class-name: com.mysql.jdbc.Driver
            slave1:
              jdbc-url: jdbc:mysql://127.0.0.1:3306/springboot_test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&useSSL=false
              username: root
              password: root
              driver-class-name: com.mysql.jdbc.Driver
    2> 切面配置参考org.hf.application.mybatis.multiple.datasource.config.DataSourceAop类
    3> 如果需要切换主数据源需要在对应方法上添加@Master, 否则默认按2>中的配置类
