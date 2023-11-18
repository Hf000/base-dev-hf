# spring-boot-demo 简单使用 (springboot没有父子容器的概念了)
    
1. SpringBoot项目搭建，注意：启动类位置应该高于扫描包中类的位置级别     
2. 整合SpringMVC拦截器    
3. 整合事务和连接池（hikari（默认）、druid）    
4. 整合mybatis、通用Mapper    
5. 整合Junit   
6. 整合redis     
8. 整合多数据源读写分离操作 
9. (已删除)整合elastic-job 注意guava和curator-client的版本兼容问题, 这里guava的版本号是18.0, curator-client的版本号是2.10.0
10. 整合swagger3.0.0, 注意SpringBoot版本是2.6.0以上的需要使用swagger3.0.0版本, swagger本地文档地址: http://localhost:8088/springboot/swagger-ui/index.html
11. org.hf.boot.springboot.annotations包下新增CustomAvoidRepeat防重复提交, CustomPrefixRedisLock自定义锁前缀及名称(支持SpEL)redis锁, CustomRedisLock自定义redis锁(支持锁续期)
12. org.hf.boot.springboot.controller.RequestController request请求接收参数示例
13. 默认集成logback日志框架, 如果需要使用别的日志框架需要先排除logback相关依赖;
14. SpringBoot中使用AopContext.currentProxy()需要在启动类开启@EnableAspectJAutoProxy(exposeProxy = true);
15. SpringBoot中集成线程池 org.hf.boot.springboot.config.AsyncExecutorConfig
    15.1 启动类上添加@EnableAsync注解, 开启异步支持, 然后直接在对应的@Service方法上添加@Async, 进行异步调用(此时使用的是默认线程池SimpleAsyncTaskExecutor);
    15.2 在@Configuration配置类上添加@EnableAsync注解, 自定义线程池对象上添加@Bean(name = "多个线程池对象需要根据name区分"), 然后直接在对应的@Service方法上添加@Async("线程池name")进行调用;
    15.3 SpringBoot中集成线程池失效
        1> 异步方法使用static修饰;
        2> 异步方法类没有使用@Service注解（或其他注解）导致spring无法扫描到异步类
        3> controller中需要使用@Autowired或@Resource等注解自动注入service类，不能自己手动new对象
16. spring整合redis缓存配置: org.hf.boot.springboot.config.CustomRedisCacheConfig
17. org.hf.boot.springboot.error.handler.GlobalExceptionHandler 全局异常统一处理,并且集成对响应返回的处理(继承ResponseBodyAdvice,org.hf.boot.springboot.error.handler.GlobalExceptionHandler#beforeBodyWrite自定义响应结果处理)
18. org.hf.boot.springboot.proxy包下为代理转发相关实现
    整体流程:
    ProxyServletHandlerMapping通过urlPath匹配ProxyServletController -> ProxyServletController调用AbstractProxyServlet的service方法 -> 获取urlPath并记录目标地址 -> 解析request请求体,转化为json对象 -> 执行继承AbstractProxyServlet的handler方法(处理目标服务器的请求响应封装) -> 获取handler的处理结果并写入response -> ProxyServletController组装ModelAndView -> 执行DispatcherServlet后续处理
    使用方法: 
        1> org.hf.boot.springboot.proxy.impl.servlet.CustomServiceProxyServlet 自定义实现servlet请求转发
        2> org.hf.boot.springboot.proxy.impl.controller.CustomServiceProxyServletController 自定义实现controller进行请求映射注册
        3> org.hf.boot.springboot.proxy.impl.config.ProxyServletConfiguration 自定义配置类进行自定义代理controller的bean初始化和自定义代理处理映射器注册
        4> src/main/resources/proxy/application-proxy-conf.yml 代理转发路径映射配置, 支持多个server的配置
        5> src/main/resources/application.yml 中新增请求代理相关配置
        6> 直接请求path路径,会转发到target-path
    包下除以上的类,其他均为公共实现
19. org.hf.boot.springboot.config.AbstractEventSubscriber 拓展spring事件分发和订阅
20. org.hf.boot.springboot.annotations.CustomTransactional 自定义事务注解
21. org.hf.boot.springboot.annotations.CustomRedisLock 自定义redis锁实现,支持锁续期
22. org.hf.boot.springboot.annotations.CustomPrefixRedisLock 自定义redis锁实现,支持spel表达式
23. org.hf.boot.springboot.retry.CustomRetryException 自定义异步记录需要重试的异常记录
    使用方法:
        1> 在可能需要异常重试的方法上添加@CustomRetryException注解
        2> 调用方法进行异步方法重试: org.hf.boot.springboot.service.RetryExceptionRecordService.retryExceptionRecord
24. spring重试
    使用方法见：org.hf.boot.springboot.service.impl.RetryExceptionRecordServiceImpl.springExceptionRetry注释内容
25. 基于redis+lua脚本实现限流注解@RedisLimitAnnotation **(待验证)**
    使用方法: 在对应的类方法上添加org.hf.boot.springboot.currentlimit.redis.RedisLimitAnnotation注解
26. javax.validation.Valid的@Valid和org.springframework.validation.annotation.Validated的@Validated注解区别:
    1> 加载方式差异: @Valid是SpringMVC在进行参数加载的时候, 对验证器接口通过SPI的方式加载的hibernate参数验证器实现; @Validated是Spring通过自动配置类去加载的;
    2> 实现方式: 在controller层, @Valid和@Validated是SpringMVC处理器生成参数对象的时候进行参数验证生效的; 在service层, @Validated是通过Spring AOP的方式进行切面拦截@Valid生效的;
    3> 使用方式: 在controller层, 都可以直接在方法入参前加上@Valid或者@Validated注解; 在service层要使用方法的@Valid生效, 需要在类上加上@Validated注解;
    4> 使用区别: @Valid注解可以实现对参数对象的嵌套校验; @Validated注解可以实现对参数对象的分组校验;
    5> 需要引入的依赖: spring-boot-starter-validation
    6> 使用实例: org.hf.boot.springboot.controller.ValidatedController
    7> 扩展: 自定义校验注解, 需要在自定义注解上指定校验器实现类@Constraint(validatedBy = 类名.class), 然后实现类实现接口ConstraintValidator<注解类型, 参数类型>



扩展: 
    1. immutables
        1.1 生成不可变对象；
        1.2 没有提供对应的setter和getter方法，不能进行对象属性的修改，如果想要修改当前immutables对象的某个属性的值，只能重新生成一个对象；
        1.3 DAO框架mybatis做值的映射的时候是根据setter和getter方法做赋值，所以采用immutables对象可能无法直接做值得映射;
        1.4 可以进行对象的序列化。
    2. Linux
        2.1 查询指定的进程信息: ps -ef | grep 进程关键字
        2.2 实时查询日志文件: tail -f 日志文件名; 退出: ctrl + c
        2.3 查看日志文件的后100行: tail -f -n 100 日志文件名称; 退出: ctrl + c
        2.4 查询当前文件夹下的所有文件列表: ls
        2.5 切换到指定的文件夹下: cd /文件夹名称
        2.6 管道符号: | (可以将命令连接起来执行)
        2.7 在指定文件中查找自定关键字: grep -i(忽略大小写[还有其他参数,自行百度]) 关键字 文件名称
        2.8 进入指定的文件: vi 文件名称; 查询关键字: /关键字(n查找下一行,N查询上一行); 退出: :q!
        2.9 查询指定名称文件: find [指定查找目录] [查找规则] [查找完成会后执行的action] 例如查找当前目录指定文件名称(忽略文件名称大小写): find . -iname 文件名称 -print
        2.10 查询指定文件指定时间段的日志: sed -n '/2022-08-23 11:35/,/2022-08-23 11:38/p' all-172.13.159.40.log
        2.11 调用请求地址: curl -v "请求地址,例如: http://localhost:8000/ferris/family/getFamilyEvalReport?batchNo=523832"
    3. PGSQL
        3.1 忽略传值的大小写模糊查询关键字: ilike, 例: select * from corehr_emp_info where um ilike '%fuGU%';
    4. MAVEN
        4.1 如果依赖坐标相同: 遵循依赖路劲最短原则;
        4.2 不同依赖中的同包同类: JVM的类加载器的随机加载;
        4.3 原则上不要出现重复依赖引用;
        4.4 在<packaging>pom</packaging>类型的工程下不要在test -> java的源文件夹(Test Sources Root)中写单元测试类,不然编译时会生成三个文件夹,一个arget文件夹,其下子文件夹generated-test-sources,再其下子文件夹test-annotations
    5. idea使用
        5.1 工程名称和文件路径不一致：
            5.1.1 方法一：直接将该模块路径文件夹名称修改的跟模块名称一致；
            5.1.2 方法二：先移除掉该模块，然后重新将此模块加入到工程中；
        5.2 idea项目文件报红, 点击进去又不报红了问题
            5.2.1 原因: 可能是项目文件.iml文件导致的;
            5.2.2 解决办法: 打开idea的Terminal 终端, cd到对应的module模块路径下, 然后执行命令: mvn idea:module 即可.
    6. Git使用
        6.1 本地合并远程分支代码
            6.1.1 右键选择git bash here
            6.1.2 git fetch: 获取远程所有分支
            6.1.3 git merge origin/master: 合并master分支代码，origin/后面接需要合并的分支名称
        6.2 将本地代码push到远程分支
            6.2.1 git push origin HEAD:hrx-perf-server-gzw-HUFEI: 将本地分支代码push到远程分支上，如果没有此分支就新建改分支（hrx-perf-server-gzw-HUFEI分支名称）
            6.2.2 git push origin mylocal:db-script-630: 将本地分支push到指定远程分支, 有冲突不会覆盖.
        6.3 将代码commit到本地分支
            6.3.1 git status: 查询当前分支修改记录;
            6.3.2 git branch ASSESSGROUPCHANGE-HUFEI-630: 新建本地分支;
            6.3.3 git checkout ASSESSGROUPCHANGE-HUFEI-630: 切换到新建的本地分支;
            6.3.4 gti commit: 提交修改记录;
            6.3.5 git merge ASSESSGROUPCHANGE-HUFEI-630: 将本地分支合并到当前分支;
            6.3.6 git cherry-pick 提交记录标识: 将指定提交合并到当前分支;
            6.3.7 git rebase -ontomaster 提交记录标识^: 表示将从指定的提交记录标识开始到当前分支的最后提交记录为止, 合并到master分支;
    7. SQL相关
        7.1 explain关键字查询计划解析, 参考文章: https://www.cnblogs.com/gomysql/p/3720123.html
            例: explain select oatc.*, (SELECT ci.real_name from customer_info ci where 1=1 and ci.state = 0 and ci.id = oatc.user_id) from opr_activity_task_clock oatc WHERE 1=1 and oatc.task_id in (select oat.id from opr_activity_task oat where oat.user_id = 523481 and oat.state = 0);
            执行结果: 12列字段信息
                数据行名称   id  select_type          table    partitions    type    possible_keys       key                  key_len    ref                     rows    filtered    Extra
                数据行       1	PRIMARY	             oat	  NULL          ALL	    PRIMARY				NULL                 NULL       NULL                    581	          1	    Using where
                            1	PRIMARY	             oatc	  NULL          ref	    uk_task_clock_app	uk_task_clock_app	 4	        customer.oat.id	        5	        100     NULL
                            2	DEPENDENT SUBQUERY	 ci		  NULL          eq_ref	PRIMARY,idx_state	PRIMARY	             4	        customer.oatc.user_id	1	      98.85	    Using where
            解析: 
                1> 第一列id字段表示: 查询中执行select子句或操作表的顺序(第一个select是1, 子查询是2, 子子查询是3, 依次类推), id如果相同, 可以认为是一组, 从上往下顺序执行; 在所有组中, id值越大, 优先级越高,越先执行
                2> 第二列select_type字段表示: 查询中每个select子句的类型（简单OR复杂）
                    a. SIMPLE: 查询中不包含子查询或者UNION
                    b. PRIMARY: 查询中若包含任何复杂的子部分, 最外层查询则被标记为PRIMARY
                    c. SUBQUERY: 在SELECT或WHERE列表中包含了子查询, 该子查询被标记为SUBQUERY
                    d. DERIVED: 在FROM列表中包含的子查询被标记为DERIVED(衍生), 用来表示包含在from子句中的子查询的select, mysql会递归执行并将结果放到一个临时表中. 服务器内部称为"派生表", 因为该临时表是从子查询中派生出来的
                    e. UNION: 若第二个SELECT出现在UNION之后, 则被标记为UNION; 若UNION包含在FROM子句的子查询中, 外层SELECT将被标记为:DERIVED
                    f. UNION RESULT:从UNION表获取结果的SELECT被标记为UNION RESULT
                    此外: SUBQUERY和UNION还可以被标记为DEPENDENT和UNCACHEABLE. DEPENDENT意味着select依赖于外层查询中发现的数据. UNCACHEABLE意味着select中的某些特性阻止结果被缓存于一个item_cache中.
                3> 第三列table字段表示: 该SQL语句是作用于那张表的, 取值为: 表名, 表别名, 衍生表名等
                4> 第四列partitions字段表示: 涉及到分区的表的数据来源于哪些分区
                5> 第五列type字段表示: 在表中找到所需行的方式, 又称"访问类型", 常见类型如下: 
                    a. ALL: 将遍历全表以找到匹配的行
                    b. index: index与ALL区别为index类型只遍历索引树
                    c. range: 索引范围扫描, 对索引的扫描开始于某一点, 返回匹配值域的行. 显而易见的索引范围扫描是带有between或者where子句里带有<, >查询. 当mysql使用索引去查找一系列值时, 例如IN()和OR列表, 也会显示range(范围扫描), 当然性能上面是有差异的.
                    d. ref: 使用非唯一索引扫描或者唯一索引的前缀扫描, 返回匹配某个单独值的记录行
                    e. eq_ref: 类似ref, 区别就在于使用的索引是唯一索引, 对于每个索引键值, 表中只有一条记录匹配, 简单来说, 就是多表连接中使用primary key或者unique key作为关联条件
                    f. const: 当MySQL对查询某部分进行优化, 并转换为一个常量时, 使用这些类型访问. 如将主键置于where列表中, MySQL就能将该查询转换为一个常量
                    g. system: system是const类型的特例, 当查询的表只有一行的情况下, 使用system
                    h. NULL: MySQL在优化过程中分解语句, 执行时甚至不用访问表或索引, 例如从一个索引列里选取最小值可以通过单独索引查找完成.
                    i. 查询效率从高到底的取值为; 所有的type字段取值: NULL > system > const > eq_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_subquery > range > index > ALL; 一般情况下type字段取值: system > const > eq_ref > ref > range > index > ALL
                6> 第六列possible_keys字段表示: 指出MySQL能使用哪个索引在表中找到记录, 查询涉及到的字段上若存在索引, 则该索引将被列出, 但不一定被查询使用
                7> 第七列key字段表示: 显示MySQL在查询中实际使用的索引, 若没有使用索引, 显示为NULL
                8> 第八列key_len字段表示: 索引中使用的字节数, 可通过该列计算查询中使用的索引的长度(key_len显示的值为索引字段的最大可能长度, 并非实际使用长度, 即key_len是根据表定义计算而得, 不是通过表内检索出的)
                9> 第九列ref字段表示: 上述表的连接匹配条件, 哪些列或常量被用于查找索引列上的值, 即某表的某个字段引用到了本表的索引字段
                10> 第十列字段表示: MySQL根据表统计信息及索引选用情况，估算的找到所需的记录所需要读取的行数
                11> 第十一列filtered字段表示: 返回结果与实际结果的差值占总记录数的百分比
                12> 第十二列Extra字段表示: 包含不适合在其他列中显示但十分重要的额外信息
                    a. Using index: 该值表示相应的select操作中使用了覆盖索引(Covering Index: MySQL可以利用索引返回select列表中的字段, 而不必根据索引再次读取数据文件, 包含所有满足查询需要的数据的索引称为覆盖索引), 注意: 如果要使用覆盖索引, 一定要注意select列表中只取出需要的列, 不可select *, 因为如果将所有字段一起做索引会导致索引文件过大, 查询性能下降
                    b. Using where: 表示mysql服务器将在存储引擎检索行后再进行过滤, 即扫描全表. 如果where条件里涉及索引中的列, 当读取索引时就能被存储引擎检索, 因此不是所有带where字句的查询都会显示"Using where". 有时"Using where"的出现就是一个暗示: 查询用到了不同的索引. 注意: 索引设置不合理也可能导致全表扫描, 例如删除状态或者性别字段设置索引也会导致全表扫描
                    c. Using temporary: 表示MySQL需要使用临时表来存储结果集, 常见于排序和分组查询
                    d. Using filesort: MySQL中无法利用索引完成的排序操作称为"文件排序"
                    e. Using join buffer: 该值强调了在获取连接条件时没有使用索引, 并且需要连接缓冲区来存储中间结果. 如果出现了这个值, 那应该注意, 根据查询的具体情况可能需要添加索引来改进能.
                    f. Impossible where: 这个值强调了where语句会导致没有符合条件的行.
                    g. Select tables optimized away: 这个值意味着仅通过使用索引, 优化器可能仅从聚合函数结果中返回一行.
                    h. Index merges: 当MySQL决定要在一个给定的表上使用超过一个索引的时候, 就会出现以下格式中的一个，详细说明使用的索引以及合并的类型。
                    i. NULL: 没有用到额外的附加条件
                    j. 常见情况的性能对比: Using index > NULL > Using where >= Using temporary > Using filesort
        7.2 MySQL索引存在在硬盘中(会在硬盘的指定文件中存储), 这样能避免服务宕机导致数据丢失, 当服务启动时会加载到内存中, 这样能提高索引查询效率
        7.3 MySQL的主键的最大值存储在表结构信息中, 同理7.2, 服务启动时会将表结构信息加载到内存中
        7.4 MySQL的varchar转int类型, 如果用字符串和数值进行比较, 此时字符串和数值都会转换成浮点数(此操作会导致该字段索引失效), 转换如下:
            1> SELECT CAST('abc123' AS SIGNED); -> 0
            2> SELECT CAST('123abc' AS SIGNED); -> 123
            3> SELECT CAST('12' AS SIGNED); -> 12
    8. Doris相关, 参考文档1: https://www.bookstack.cn/read/doris-1.2-zh/157a16c97e044495.md 参考文档2: https://doris.apache.org/zh-CN/docs/dev/lakehouse/multi-catalog/jdbc/
        8.1 doris建表, 三种模型: Unique - 唯一索引模型; Aggregate - 数据聚合模型; Duplicate - 数据明细模型;
            8.1.1 创建doris数据库表, 模型UNIQUE KEY
            CREATE TABLE IF NOT EXISTS base_dev.user_info
            (
            `id` int(11) NOT NULL COMMENT '主键id',
            `user_name` varchar(100) DEFAULT NULL COMMENT '姓名',
            )
            UNIQUE KEY(`id`)
            DISTRIBUTED BY HASH(`id`) BUCKETS 1
            PROPERTIES (
            "replication_allocation" = "tag.location.default: 3"
            );
            -- "replication_allocation" = "tag.location.default: 3" 这里的数字3表示存储数据的副本数
            -- 创建唯一索引模型时, 如果有多个索引, 那么索引字段的顺序和建表语句的字段顺序需要保持一致
            -- 经常用来where的列可以作为三种模型的索引列
            -- PARTITION BY RANGE(`canc_date`)(), range分区: 数据划分第一层, 也可采用一个分区; 第二层数据划分为分桶
            -- DISTRIBUTED BY HASH(`id`) BUCKETS 1, 分桶: 逻辑存储的最小单位, 对查询条件有优化, 如果查询字段指定的是分桶字段, 那么就只会查询具体桶的数据
        8.2 doris数据同步方式
            8.2.1 导入数据一: hive同步数据到doris库
                LOAD LABEL user_label_data_test
                (
                  DATA INFILE("hdfs://hdfs01-stg/user/hive/warehouse/jt_kj_jjyl_safe.db/dws_custprofile_label_info/*/*")
                  INTO TABLE dws_custprofile_label_info
                  COLUMNS TERMINATED BY "\\x01"
                  FORMAT AS "orc"
                  (id,user_id,label_id,label_name)
                  SET
                  (id=id,user_id=user_id,label_id=label_id,label_name=label_name)
                )
                WITH BROKER "hdfs_broker"
                (
                  "username" = "hduser3717",
                  "password" = "2Mm4l67143"
                )
                PROPERTIES
                (
                  "timeout"="1200",
                  "max_filter_ratio"="0.1"
                );
                -- COLUMNS TERMINATED BY "\\x01" \\x01表示分隔符
                -- FORMAT AS "orc" 表示数据存储格式为ORC格式
                -- "max_filter_ratio"="0.1" 表示容忍的导入的数据错误率
                -- 注意事项: 1.这种导入方式需要配置HDFS集群的hdfs-site文件;2.hive库表和doris库表的导入字段映射要一致,hive库表不需要的字段可以不写;
                -- username和password是hive库的用户名及明文密码
                -- WITH BROKER 可以通过以下命令进行查询
                SHOW PROC "/brokers";
                -- 通过命令查询任务信息, 关注:1.ErrorMsg-错误信息;2.State-任务状态;3.URL-错误日志文件地址
                SHOW LOAD;
            8.2.2 导入数据二: 通过flink CDC方式从mysql同步数据到doris
                -- mysql-cdc mysql相关表及连接信息
                drop table if exists mysql_cdc_customer_info;
                CREATE TABLE mysql_cdc_customer_info (
                  id int primary key,
                  user_name varchar(100),
                  nickname varchar(100)
                )
                WITH (
                  'connector' = 'mysql-cdc',
                  'hostname' = 'T3MJARVIS-mysql.dbstg.paic.com.cn',
                  'port' = '3116',
                  'username' = 'pub_test',
                  'password' = 'Jvs2021!@Test',
                  'database-name' = 'customer',
                  'table-name' = 'customer_info',
                  'jdbc.properties.tinyInt1isBit' = 'false'
                );
                -- jdbc.properties.tinyInt1isBit = false : 将mysql的tinyint(1)转换成boolean类型
                -- doris sink doris相关表及连接信息
                drop table if exists doris_from_mysql_customer_info;
                CREATE TABLE doris_from_mysql_customer_info (
                  id int,
                  user_name varchar(100),
                  nickname varchar(100)
                )
                WITH (
                  'connector' = 'doris',
                  'fenodes' = '30.104.226.30:8030',
                  'table.identifier' = 'analysis.customer_info',
                  'username' = 'root',
                  'password' = 'root#2021',
                  'sink.label-prefix' = 'doris_label',
                  'sink.properties.format' = 'json',
                  'sink.properties.read_json_by_line' = 'true'
                );
                -- sink.label-prefix的含义: 每一批写进Doris的数据都有个label，用来标记这次导入任务。性质等同于Broker Load时的load label。这里的label-prefix就是给一个label前缀
                -- 'sink.properties.format' = 'json' : 指定字段的格式为json
                -- 'sink.properties.read_json_by_line' = 'true' : 将字段值以json格式解析
                -- flinksql的int和tinyint不需要指定长度
                -- 插入数据
                insert into doris_from_mysql_customer_info select * from mysql_cdc_customer_info;
            8.2.3 导入数据三: kafka数据同步到doris
                CREATE ROUTINE LOAD analysis.cust_data_test ON customer_info
                COLUMNS(id,user_name,nickname)
                PROPERTIES
                (
                  "desired_concurrent_number"="3",
                  "max_batch_interval" = "20",
                  "max_batch_rows" = "300000",
                  "max_batch_size" = "209715200",
                  "max_error_number"="15",
                  "strict_mode" = "false",
                  "format" = "json",
                  "json_root" = "$.after",
                  "jsonpaths" = "[\"$.id\",\"$.user_name\",\"$.nickname\"]"
                )
                FROM KAFKA
                (
                  "kafka_broker_list" = "30.104.225.11:9092,30.104.225.12:9092,30.104.225.13:9092",
                  "kafka_topic" = "sx_jarvis_core-jarvis_customer_data_sync",
                  "property.group.id" = "customer_info",
                  "property.client.id" = "sx_jarvis_core.jarvis_customer_data_sync.jJL6CWtfFB",
                  "property.kafka_default_offsets" = "OFFSET_BEGINNING"
                );
                -- 指定json根节点: "json_root" = "$.after"
                -- 指定json数据对应的COLUMNS: "jsonpaths" =
                -- 指定最大允许的错误数: "max_error_number"="15",
                -- 可以通过from_unixtime将时间戳转换成doris的datetime(还未尝试)
                -- 查询创建的作业信息
                SHOW ROUTINE LOAD;
                -- 查询指定作业下子任务的状态
                SHOW ROUTINE LOAD TASK WHERE JobName = 'cust_data_test';
                -- 停止指定的作业
                STOP ROUTINE LOAD FOR cust_data_test;
            8.2.4 导入数据四: tidb同步数据到doris
                -- tidb-cdc tidb相关表及连接信息
                drop table if exists tidb_cdc_user_label_nature;
                CREATE TABLE tidb_cdc_user_label_nature (
                  label_id varchar(1000),
                  label_name varchar(1000),
                  label_value varchar(1000),
                  PRIMARY KEY(label_id, label_value) NOT ENFORCED
                )
                WITH (
                  'connector' = 'tidb-cdc',
                  'tikv.grpc.timeout_in_ms' = '20000',
                  'pd-addresses' = '30.69.108.17:3632',
                  'username' = 'pub_test',
                  'password' = 'Z0*==)Lvy7',
                  'database-name' = 'custprofile',
                  'table-name' = 'ads_custprofile_user_label_nature'
                );
                -- doris sink doris相关表及连接信息
                drop table if exists doris_from_tidb_user_label_nature;
                CREATE TABLE doris_from_tidb_user_label_nature (
                  label_id varchar(1000),
                  label_name varchar(1000),
                  label_value varchar(1000)
                )
                WITH (
                  'connector' = 'doris',
                  'fenodes' = '30.104.226.30:8030',
                  'table.identifier' = 'analysis.ads_custprofile_user_label_nature',
                  'username' = 'root',
                  'password' = 'root#2021',
                  'sink.label-prefix' = 'doris_label',
                  'sink.properties.format' = 'json',
                  'sink.properties.read_json_by_line' = 'true'
                );
                -- 插入数据
                insert into doris_from_tidb_user_label_nature select * from tidb_cdc_user_label_nature;
                -- 源表需要指定主键 PRIMARY KEY(label_id, label_value) NOT ENFORCED, 主键可指定多列, 这里的not enforced表示flinksql不会对主键做强制的唯一性约束、非空约束，而且目前flinksql中只支持这种类型的主键
                -- tidb-cdc需要tidb版本在5.1.x以上, 目前就只能通过jdbc查询后插入的方式同步tidb到doris的数据
        8.3 注意: doris数据库不支持update更新操作, doris的更新是将之前的数据标记为删除然后插入新的数据
    9. 代码整洁之道
        9.1. 注意规范命名: 要知名达意, 代码中尽量用肯定的含义表达
        9.2. 注意规范方法: 
            1> 代码的逻辑片段合理分割, 抽取的方法尽量做到一个方法只做一件事情; 
            2> 将可能复用的逻辑尽量提取成方法调用
            3> 参数传递, 尽量不要返回 null, 也别传递 null
            4> 尽量不要让同一个对象成为既是出参也是入参
        9.3. 注意成员变量的使用深度, 尽量不要出现一个方法入参传递几层再去使用
        9.4. 代码嵌套层数尽量不要超过三层, 尽量不要出现多层嵌套的if-else和for语句
        9.5. 尽量保证代码片段结构的完整性，不要随意改变代码结构, 如果需要新增逻辑, 可以抽取成方法后嵌入原有逻辑
        9.6. 业务逻辑的实现尽量保证实现类去实现一个接口, 这样可以便于在接口中清晰阅读有哪些方法
        9.7. 重点注意, 代码一定要有必要的注释, 方法一定要表明此方法的用途, 让别人可以一眼看出用途而不是需要去一行行读代码
                        