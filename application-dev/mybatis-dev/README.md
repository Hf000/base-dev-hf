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
    7> mybatis查询, 如果返回类型为resultType, 可以直接将字符串映射成为对应的枚举类型;
    8> mybatis查询, 如果返回类型为实体对应的BaseResultMap的id, 那么即使map中没有定义表中的字段, 查询时进行查询了也会返回, mapper方法返回类型要是表对应的实体对象, 且符合驼峰命名规则
# 7.Mybatis加载
    1. 加载Mybatis配置文件,创建XMLConfigBuilder解析Xml文件,将mapper中的标签信息加载到Configuration对象中,创建SqlSessionFactory,
        1> 不整合Spring,是通过SqlSessionFactoryBuilder进行Mybatis配置的加载,然后创建SqlSessionFactory对象,
        2> 整合Spring时,通过SqlSessionFactoryBean加载Mybatis配置,此类实现FactoryBean接口,在Spring中进行Bean实例化时调用getObject方法来
        自定义对象的创建,将SqlSessionFactory注入到Spring环境中,
    执行查询或者更新操作时,通过动态代理创建mapper接口的实现代理类进行方法调用,从Configuration对象中获取MappedStatement,通过Executor进行数据库操作
    2. 一级缓存:
       在进行openSession时, 会创建一个一级缓存对象
       一级缓存只能针对同一会话session下同一namespace下的查询生效
       如果在同一个session会话中存在更新数据操作,那么会进行缓存清除
       线程不安全
    3. 二级缓存:
       二级缓存针对同一namespace下的查询生效
       如果在同一namespace下进行更新数据操作,会进行缓存清除
       线程不安全
