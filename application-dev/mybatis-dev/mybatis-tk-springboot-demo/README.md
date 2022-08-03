# mybatis-tk-springboot-simple
personal study

# 1. SpringBoot整合MyBatis通用Mapper的简单应用, 
    文档:https://github.com/abel533/Mapper/wiki
    或者 https://mapper.mybatis.io/
# 2. generator通用mapper生成实体和mapper用法：
    1. (推荐使用) 执行org.hf.application.mybatis.tk.springboot.generator.CodeGenerator中的main方法即可
        配置文件src\main\resources\generator\generator.properties要做以下配置
        1> 数据源配置
        jdbc.driverClassName=
        jdbc.url=
        jdbc.username=
        jdbc.password=
        2> 通用mapper生成文件相关参数配置  
        # 生成文件的父包名配置
        tkMapper.targetPackage=org.hf.application.mybatis.tk.springboot
        # 生成文件的绝对根路径配置
        tkMapper.targetProject=D:/IdeaWorkSpace/base-dev-hf/application-dev/mybatis-dev/mybatis-tk-springboot-demo
        # 生成entity继承父类的全类名
        tkMapper.entity.baseEntity=org.hf.common.publi.pojo.entity.BaseEntity
        # 生成对应实体的表名
        tkMapper.tableName=user_info
    2. 运行maven插件命令即可 注意: 需要将generator\generatorConfig.xml文件中的
    自定义插件<plugin type="org.hf.application.mybatis.tk.springboot.generator.CodeGenerator">修改
    成<plugin type="tk.mybatis.mapper.generator.MapperPlugin">
    3. 如果需要支持多表操作, 需要在generator\generatorConfig.xml文件中增加多个<table></table>
