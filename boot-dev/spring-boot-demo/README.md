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
16. spring缓存配置: org.hf.boot.springboot.config.CustomCacheConfig
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
20. org.hf.boot.springboot.config.CustomTransactional 自定义事务注解
21. org.hf.boot.springboot.annotations.CustomRedisLock 自定义redis锁实现,支持锁续期
22. org.hf.boot.springboot.annotations.CustomPrefixRedisLock 自定义redis锁实现,支持spel表达式
23. org.hf.boot.springboot.retry.CustomRetryException 自定义异步记录需要重试的异常记录
    使用方法:
        1> 在可能需要异常重试的方法上添加@CustomRetryException注解
        2> 调用方法进行异步方法重试: org.hf.boot.springboot.service.RetryExceptionRecordService.retryExceptionRecord
24. spring重试
    使用方法见：org.hf.boot.springboot.service.impl.RetryExceptionRecordServiceImpl.springExceptionRetry注释内容


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
