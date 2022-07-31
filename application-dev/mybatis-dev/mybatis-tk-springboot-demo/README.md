# mybatis-tk-springboot-simple
personal study

# 1. SpringBoot整合MyBatis通用Mapper的简单应用, 
    文档:https://github.com/abel533/Mapper/wiki
    或者 https://gitee.com/free/Mapper
# 2. generator通用mapper生成实体和mapper用法：
    1. 执行org.hf.application.mybatis.tk.springboot.generator.CodeGenerator中的main方法即可
    2. 运行maven插件命令即可 注意: 需要将generator\generatorConfig.xml文件中的
    自定义插件<plugin type="org.hf.application.mybatis.tk.springboot.generator.CodeGenerator">修改
    成<plugin type="tk.mybatis.mapper.generator.MapperPlugin">
