# mybatis-plus-springboot-simple
personal study

# 1. SpringBoot整合MyBatis-Plus的简单应用,mybatis-plus文档:https://baomidou.com/pages/24112f/#%E7%89%B9%E6%80%A7
# 2. mybatis-plus自动生成代码工具, 支持模板VelocityTemplateEngine(模板文件.vm), 也支持模板FreemarkerTemplateEngine(模板文件.ftl)、BeetlTemplateEngine(模板文件.btl)
       .btl为后缀的文件对应的是BeetlTemplateEngine引擎类
       .ftl为后缀的文件对应的是FreemarkerTemplateEngine引擎类
       .vm为后缀的文件为VelocityTemplateEngine引擎类(默认支持)
       可在mybatis-plus-generator依赖包的templates文件夹拷对应模板进行修改
       使用方法:
       1> 执行org.hf.application.mybatis.plus.springboot.generator.CodeGenerator类main方法
       2> 根据提示输入以下信息
           请输入模块磁盘路径：
           D:\IdeaWorkSpace\base-dev-hf\application-dev\mybatis-dev\mybatis-plus-springboot-demo
           请输入author信息：
           hf
           请输入项目父包名：
           org.hf
           请输入当前模块包名：
           application.mybatis.plus.springboot
           请输入表名，多个英文逗号分割：
           user_info,tb_user
           请输入表前缀：
           tb_
# 3. 整合druid
# 4. 整合pageHelper