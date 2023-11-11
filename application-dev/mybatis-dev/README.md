# mybatis-dev
mybatis应用聚合工程

# 1. mybatis-plus-demo: mybatis及mybatis-plus的简单应用
# 2. mybatis-plus-spring-demo: mybatis-plus整合spring的简单应用
# 3. mybatis-plus-springboot-demo: mybatis-plus整合springboot的简单应用,集成mybatis-plus自动生成代码工具,详见该工程目录下的README文件
# 4. mybatis-tk-springboot-demo: tk-mybatis整合springboot, 集成tk-mybatis生成代码, 详见该工程目录下的README文件
# 5. mybatis-custom-multiple-datasource: 自定义多数据源的简单实现
# 6. 注意点:
    1> mybatis中xml注释要使用<!--  -->;
    2> mybatis中的别名不要打引号;
    3> xml文件integer类型参数,不能在if标签中判断 != '', 否则如果传值为0的话,判断结果为false;
    4> mybatis的xml文件中的<where>标签会过滤多余的 and 关键字;
    5> if标签和SQL语句中的and大小写区分开, if标签中的and只能小写;
    6> mybatis查询, 如果返回结果为集合对象如List或者Map时, 没查询到数据返回的是空集合并不是null; 如果返回结果为普通对象, 没查询到数据返回的是null;
