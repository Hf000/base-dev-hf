# spring-boot-demo 简单使用 (springboot没有父子容器的概念了)
    
## 1. SpringBoot项目搭建，注意：启动类位置应该高于扫描包中类的位置级别     
## 2. 整合SpringMVC拦截器    
## 3. 整合事务和连接池（hikari（默认）、druid）    
## 4. 整合mybatis、通用Mapper    
    1> 这里自定义实现了org.hf.boot.springboot.mapper.BaseMapper接口,可以进行特定的逻辑处理,注意事项见该类注释
## 5. 整合Junit   
## 6. 整合redis   
    1> 配置文件application.yml文件中新增了redis相关的自定义配置开关,是为了控制没启动redis时的依赖注入报错
    2> redis使用避坑
        1.将一个集合元素为object类型的数据放到Set集合, 使用members()方法无法获取出所有的值, 原因:如果当前对象进行了继承, 可能redis获取所有元素后进行比较时导致equals方法都相等, 所以需要重写一下equals和hashCode方法;
        2.使用redis的set集合的add方法时, 放多个元素需要将集合转换成数组, 否则会当成一个元素放入;
## 7. (已删除)整合elastic-job 
    1> 注意guava和curator-client的版本兼容问题, 这里guava的版本号是18.0, curator-client的版本号是2.10.0
## 8. 整合swagger3.0.0
    1> 注意SpringBoot版本是2.6.0以上的需要使用swagger3.0.0版本, swagger本地文档地址: http://localhost:8088/springboot/swagger-ui/index.html
    2> swagger整合配置: org.hf.boot.springboot.config.SwaggerConfiguration
## 9. org.hf.boot.springboot.annotations包下新增依赖redis的相关功能注解
    1> CustomAvoidRepeat防重复提交, org.hf.boot.springboot.annotations.CustomAvoidRepeat
    2> CustomPrefixRedisLock自定义锁前缀及名称(支持SpEL)redis锁, org.hf.boot.springboot.annotations.CustomPrefixRedisLock
    3> CustomRedisLock自定义redis锁(支持锁续期), org.hf.boot.springboot.annotations.CustomRedisLock
## 10. org.hf.boot.springboot.controller.RequestController request请求接收参数示例
## 11. 默认集成logback日志框架, 如果需要使用别的日志框架需要先排除logback相关依赖;
## 12. SpringBoot中使用AopContext.currentProxy()需要在启动类开启@EnableAspectJAutoProxy(exposeProxy = true);
    1> 获取spring中代理类对象基础类型工具类: org.hf.boot.springboot.utils.SpringAopTargetUtils
## 13. SpringBoot中集成线程池 org.hf.boot.springboot.config.AsyncExecutorConfig
    1 启动类上添加@EnableAsync注解, 开启异步支持, 然后直接在对应的@Service方法上添加@Async, 进行异步调用(此时使用的是默认线程池SimpleAsyncTaskExecutor);
    2 在@Configuration配置类上添加@EnableAsync注解, 自定义线程池对象上添加@Bean(name = "多个线程池对象需要根据name区分"), 
        然后直接在对应的@Service方法上添加@Async("线程池bean对象name")进行调用;
    3 SpringBoot中集成线程池失效
        1> 异步方法使用static修饰;
        2> 异步方法类没有使用@Service注解（或其他注解）导致spring无法扫描到异步类
        3> controller中需要使用@Autowired或@Resource等注解自动注入service类，不能自己手动new对象
    4 通过TaskDecorator实现自定义任务装饰器, 进行Spring异步线程池的线程上下文传递
        1> 实现: org.hf.boot.springboot.config.CustomThreadPoolTaskDecorator
        2> 使用案例:org.hf.boot.springboot.controller.UserController.testAysnc
    5 通过实现AsyncConfigurer接口, 实现springboot默认线程池的替换和异步线程执行异常捕获
        1> 重写方法实现线程池替换: org.hf.boot.springboot.config.AsyncExecutorConfig.getAsyncExecutor
        2> 重写方法实现线程池异步任务异常捕获: org.hf.boot.springboot.config.AsyncExecutorConfig.getAsyncUncaughtExceptionHandler
    6 自定义可调度执行任务线程池示例: org.hf.boot.springboot.service.impl.UserServiceImpl.addUserInfo
## 14. spring整合redis缓存配置: org.hf.boot.springboot.config.CustomRedisCacheConfig
## 15. 全局异常统一处理
    1> 实现类: org.hf.boot.springboot.error.handler.GlobalExceptionHandler 
    2> 集成对响应返回的处理(继承ResponseBodyAdvice,org.hf.boot.springboot.error.handler.GlobalExceptionHandler#beforeBodyWrite自定义响应结果处理)
        注意: 这里自定义响应结果统一处理时,不能对返回类型进行统一的封装处理,需要根据已识别的指定返回类型做定制化封装处理,如果统一封装容易出现问题,例如:会导致swagger返回对象无法识别而不可使用
## 16. org.hf.boot.springboot.proxy包下为代理转发相关实现
    1.整体流程:
        ProxyServletHandlerMapping通过urlPath匹配ProxyServletController -> ProxyServletController调用AbstractProxyServlet的service方法 -> 
        获取urlPath并记录目标地址 -> 解析request请求体,转化为json对象 -> 执行继承AbstractProxyServlet的handler方法(处理目标服务器的请求响应封装) -> 
        获取handler的处理结果并写入response -> ProxyServletController组装ModelAndView -> 执行DispatcherServlet后续处理
    2.使用方法: 
        1> org.hf.boot.springboot.proxy.impl.servlet.CustomServiceProxyServlet 自定义实现servlet请求转发
        2> org.hf.boot.springboot.proxy.impl.controller.CustomServiceProxyServletController 自定义实现controller进行请求映射注册
        3> org.hf.boot.springboot.proxy.impl.config.ProxyServletConfiguration 自定义配置类进行自定义代理controller的bean初始化和自定义代理处理映射器注册
        4> src/main/resources/proxy/application-proxy-conf.yml 代理转发路径映射配置, 支持多个server的配置
        5> src/main/resources/application.yml 中新增请求代理相关配置
        6> 直接请求path路径,会转发到target-path
    3. org.hf.boot.springboot.proxy包下的类,除以上的类,其他均为公共实现
## 17. org.hf.boot.springboot.config.AbstractEventSubscriber 拓展spring事件分发和订阅
## 18. org.hf.boot.springboot.annotations.CustomTransactional 自定义事务注解
## 19. org.hf.boot.springboot.retry.CustomRetryException 自定义异步记录需要重试的异常记录, 需要手动调接口实现异常重试
    使用方法:
        1> 在可能需要异常重试的方法上添加@CustomRetryException注解
        2> 调用方法进行异步方法重试: org.hf.boot.springboot.service.RetryExceptionRecordService.retryExceptionRecord
## 20. 自动重试
    1> spring重试注解@Retryable使用方法见: org.hf.boot.springboot.service.impl.RetryExceptionRecordServiceImpl.springExceptionRetry注释内容
    2> 重试工具类: org.hf.boot.springboot.utils.RetryUtil
## 21. 基于redis+lua脚本实现限流注解@RedisLimitAnnotation **(待验证)**
    使用方法: 在对应的类方法上添加org.hf.boot.springboot.currentlimit.redis.RedisLimitAnnotation注解
## 22. javax.validation.Valid的@Valid和org.springframework.validation.annotation.Validated的@Validated注解区别:
    1> 加载方式差异: @Valid是SpringMVC在进行参数加载的时候, 对验证器接口通过SPI的方式加载的hibernate参数验证器实现; @Validated是Spring通过自动配置类去加载的;
    2> 实现方式: 在controller层, @Valid和@Validated是SpringMVC处理器生成参数对象的时候进行参数验证生效的; 在service层, @Validated是通过Spring AOP的方式进行切面拦截@Valid生效的;
    3> 使用方式: 在controller层, 都可以直接在方法入参前加上@Valid或者@Validated注解; 在service层要使用方法的@Valid生效, 需要在类上加上@Validated注解;
    4> 使用区别: @Valid注解可以实现对参数对象的嵌套校验; @Validated注解可以实现对参数对象的分组校验;
    5> 需要引入的依赖: spring-boot-starter-validation
    6> 使用实例: org.hf.boot.springboot.controller.ValidatedController
    7> 扩展: 自定义校验注解, 需要在自定义注解上指定校验器实现类@Constraint(validatedBy = 类名.class), 然后实现类实现接口ConstraintValidator<注解类型, 参数类型>
## 23. 根据自定义枚举注解@CustomEnum获取具体枚举的枚举项列表; 
    1> 核心实现: org.hf.boot.springboot.service.impl.EnumFactory
    2> 参考实例: org.hf.boot.springboot.controller.EnumController.getEnumList
## 24. 自定义实现枚举的转换处理; 相关功能实现类都放在org.hf.boot.springboot.enumerate包下
    1> 核心实现:
        MVC: {org.hf.boot.springboot.enumerate.mvc.CommonEnumRegistry, 
            org.hf.boot.springboot.enumerate.mvc.converter.CommonEnumJsonParamConverter, 
            org.hf.boot.springboot.enumerate.mvc.converter.CommonEnumRequestParamConverter}
        DAO: {org.hf.boot.springboot.enumerate.repository.mybatis.CommonEnumTypeHandler或者
            org.hf.boot.springboot.enumerate.repository.jpa.CommonEnumAttributeConverter}
    2> 参考实例: org.hf.boot.springboot.enumerate.mvc.controller.EnumsController
## 25. 获取SpringMVC注册url资源
    1> 获取所有的接口资源并导出成excel文件(注意:依赖swagger的@Api和@ApiOperation注解); 示例: org.hf.boot.springboot.controller.UrlResController#scanUrlRes **(待验证)**
    2> 比较两个分支新增的controller接口(依赖SpringMVC的@PostMapping和@GetMapping注解); 工具类: org.hf.boot.springboot.utils.FileCompareUtil
    3> 通过springMVC映射器, 获取所有的接口资源并递归解析出入参是否包含指定字符或注解的属性; 示例: org.hf.boot.springboot.controller.UrlResResolveController
## 26. springboot自定义多数据源切换 **(事务支持待验证)**
    1> 实现思想: 通过配置文件配置多数据源信息,或者数据库配置的数据源信息加载多个数据实例进行数据源的配置,根据重写获取数据源路由key方法获取当前线程
    数据源, 并采用threadlocal保存当前线程数据源信息
    2> 核心类实现的包路径: org.hf.boot.springboot.dynamic.datasource
    3> 测试参考实例: org.hf.boot.springboot.controller.DynamicDataSourceController 
## 27. SpringBoot中测试类被@SpringBootTest注解标识, 那么测试方法上加@Transactional注解, 就表示此为测试用例方法, 事务不用提交;
## 28. 循环依赖
    一. 可能出现的问题
        1> spring是通过三级缓存解决了循环依赖的, 但是springboot自2.6.0及以上版本默认禁止循环依赖, 如果开启需配置spring.main.allow-circular-references=true
        2> 开启循环依赖也可能会导致所持有的依赖对像的引用不同而报错, 因为循环依赖过程中先实例化的对象如果使用了@Async注解(本质也是通过BPP后置处理器实现)或由
        BeanPostProcessor后置处理器返回代理对象时会报错:BeanCurrentlyInCreationException, 原因是因为当循环依赖中先实例化的对象在进行实例化后,属性赋值之前会先将
        ObjectFactory添加至三级缓存中, 从而使得循环依赖中后实例化的对象在实例化时可以从三级缓存中拿到先实例化对象的引用完成属性赋值, 而先实例化的对象完成赋值后进行初
        始化时如果存在@Async注解, 此时会进行其对应的后置处理器AsyncAnnotationBeanPostProcessor的处理, 在postProcessAfterInitialization方法中将返回代理对象，
        此代理对象与B中持有的A对象引用不同从而报错
    二. 解决方法
        1> 尽可能从设计层面避免循环依赖, 这里可以采用接口隔离的方式
        2> 使用@Lazy注解进行延迟加载
        3> 使用@DependsOn注解指定依赖项的加载顺序
        4> 使用@Autowired注解, 配置允许循环依赖, 但是需要注意先实例化的对象不要出现使用@Async注解或由BeanPostProcessor后置处理器返回代理对象的场景
        5> 使用org.hf.boot.springboot.utils.SpringContextUtil.getBean(java.lang.Class<T>)的方式获取spring容器bean实例对象后进行方法调用
        6> 使用@PostConstruct注解, 因为@PostConstruct注解告诉Spring在创建Bean后立即执行指定的初始化方法, 此时在初始化方法中去获取容器中指定的bean
        对象, 使得对象的创建和依赖的解析分开进行, 避免循环依赖的出现
        7> 通过构造函数注入, 需要将循环依赖的对象通过
    三. 注意
        1> 使用lambda的@RequiredArgsConstructor注解进行构造注入时, 需要避免循环依赖, 所以要慎用, 也可以使用
        @RequiredArgsConstructor(onConstructor_ = {@Lazy})方式实现
        2> 通过构造函数注入, 在此工程依赖的springboot版本进行实现时, 还是会报循环依赖问题, 需要@Lazy注解辅助实现, 采用构造注入需要避免循环依赖场景
        3> 通过Setter方法注入依赖对象实例, 也可能会出现BeanCurrentlyInCreationException报错问题(需要@Lazy注解辅助实现), 还有可能出现实例没有完成
        注入就被其他bean使用而导致空指针问题
## 29. 采用redis缓存和redisson分布式实现接口防重
    1> redis缓存实现: org.hf.boot.springboot.custom.lock.redisson.RedisRequestLockAspect
    2> redisson分布式锁实现: org.hf.boot.springboot.custom.lock.redisson.RedissonRequestLockAspect
## 30. spring中事务前后置处理 **(待验证)**
    1> 事务提交后的逻辑处理: org.hf.boot.springboot.service.CustomTransactionService.transactionSubmitAfterLogic
    2> 异步线程事务失效解决方案: org.hf.boot.springboot.service.CustomTransactionService.asyncTransactionLoseEfficacy
## 31. 接口签名验证及出入参加解密:
    1> 签名验证: org.hf.boot.springboot.security.sign
    2> 出入参加解密: org.hf.boot.springboot.security.encryption
## 32. 业务逻辑执行耗时统计
    1> 工具类: org.hf.boot.springboot.utils.PerfTrackerUtil
    2> 使用实例: org.hf.boot.springboot.utils.PerfTrackerUtil.main
## 33. Spring中的设计模式
    1> 工厂模式: Spring IOC(Bean通过工厂进行创建)
    2> 单例模式: Spring Bean(Bean以默认方式创建后的实例)
    3> 适配器模式: Spring MVC(处理请求时, 先查找对应的处理器链, 然后通过适配器进行处理器链的执行)
    4> 装饰者模式: Spring Bean(实例化Bean后, 会将其包装成BeanWrapper, 用来统一Bean的属性操作功能, 也就是提供一套标准的查询和设置属性的方法)
    5> 代理模式: Spring AOP(通过Jdk动态代理生成Bean的代理对象)
    6> 策略模式: Spring容器启动时, 加载Resource资源文件, 根据url, fileSystem, classPath, InputStream进行不同策略的加载
    7> 观察者模式: Spring Event(当Spring容器刷新完成时会触发ContextRefreshedEvent事件,此时Spring容器的Bean已经初始化完成)
    8> 模板方法模式: 场景一:Spring初始化容器时执行模板方法org.springframework.context.support.AbstractApplicationContext.refresh; 
                   场景二:数据源的连接操作, 根据不同的模板JdbcTemplate, HibernateTemplate进行数据库的连接
    9> 责任链模式: Spring AOP(Spring默认按照顺序定义了AOP的切面拦截执行链, 在Bean创建时初始化操作时, 通过BPP后置处理器按规则进行切面匹配, 匹配到对应拦截方法后进行前后置增强)
## 34. Spring中实现Bean注册到Spring容器的几种方式
    1> 通过 @Configuration + @ComponentScan(指定类路径) 开启组件扫描
    2> 通过@Configuration + @Bean 自定义Bean的创建
    3> 通过@Configuration + @Import({class}) 自定义指定Bean的加载
    4> 通过@Configuration + @Import(其他Configuration) 自定义指定配置类的加载,@EnableWebMvc通过此方式实现注册SpringWebMVC
        所需要的各种特殊类型的bean到Spring容器中，以便在DispatcherServlet初始化及处理请求的过程中生效！
    5> 通过@Configuration + @Import(ImportSelector) 自定义指定类路径的Bean的加载,@EnableAutoConfiguration通过此方式实现
        扫描所有与依赖JAR中指定位置的META-INF/spring.factories文件中的配置,加载各种自动配置类.这些自动配置类会根据当前项目的依
        赖和配置,自动配置相应的Bean注册到BeanDefinition。
    6> 通过@Configuration + @ImportResources({classPath:applicationProperties.xml}) 加载Spring的XML文件配置
## 35. Spring常用加载入口
    1> 通过加载指定XML文件:org.springframework.context.support.ClassPathXmlApplicationContext.ClassPathXmlApplicationContext(java.lang.String)
    2> 通过加载指定包路径:org.springframework.context.annotation.AnnotationConfigApplicationContext
    3> Spring MVC通过配置Spring上下文监听器ContextLoaderListener执行initWebApplicationContext()方法调用
        configureAndRefreshWebApplicationContext()方法进行Spring容器初始化流程
    4> Spring Boot通过应用程序入口进行run()方法调用(调用链路应用入口run() -> org.springframework.boot.SpringApplication.run() -> 
        org.springframework.boot.SpringApplication.refreshContext() -> org.springframework.boot.SpringApplication.refresh()),
        如果当前是WebApplicationType.SERVLET类型,则通过org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh()
        方法进行Spring容器初始化流程
    5> Mybatis加载到Spring的入口,通过org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration自动加载,
        创建SqlSessionFactory进行SqlSession对象获取,创建SqlSessionTemplate进行Mapper相关方法调用
## 36. Springboot定时任务实现
    1> @Scheduled注解实现: org.hf.boot.springboot.job.ScheduleTaskDemo
## 37. 通过Mybatis框架实现动态的statement配置, 并实现动态sql的查询
    1> 相关实现包路径: org.hf.boot.springboot.dynamic.statement
    2> 应用示例: org.hf.boot.springboot.controller.DynamicStatementDataApiController#postJson



# 扩展: 
## 1. immutables
    1.1 生成不可变对象；
    1.2 没有提供对应的setter和getter方法，不能进行对象属性的修改，如果想要修改当前immutables对象的某个属性的值，只能重新生成一个对象；
    1.3 DAO框架mybatis做值的映射的时候是根据setter和getter方法做赋值，所以采用immutables对象可能无法直接做值得映射;
    1.4 可以进行对象的序列化。
## 2. Linux
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
    2.12 如何看查占用cpu最多的进程？方法一, 指令：ps H -eo pid,pcpu | sort -nk2 | tail; 方法二, 指令：top
    2.13 找到了最耗CPU的进程ID，对应的服务名是什么呢？方法一, 指令：ps aux | fgrep pid; 方法二, 指令：ll /proc/pid
    2.14 如何查看某个端口的连接情况？方法一, 指令：netstat -lap | fgrep port; 方法二, 指令：lsof -i :port
## 3. PGSQL
    3.1 忽略传值的大小写模糊查询关键字: ilike, 例: select * from corehr_emp_info where um ilike '%fuGU%';
## 4. MAVEN
    4.1 如果依赖坐标相同: 遵循依赖路劲最短原则;
    4.2 不同依赖中的同包同类: JVM的类加载器的随机加载;
    4.3 原则上不要出现重复依赖引用;
    4.4 maven不同版本依赖引入方式
        1> 如果引入时层级相同, 则按照依赖顺序引入, 也就是哪个在前面则引入哪个依赖
        2> 如果引入时层级不同, 则按照最短路径原则引入, 也就是哪个依赖的引入层级最短则引入哪个依赖
    4.5 在<packaging>pom</packaging>类型的工程下不要在test -> java的源文件夹(Test Sources Root)中写单元测试类,不然编译时会生成三个文件夹,一个arget文件夹,其下子文件夹generated-test-sources,再其下子文件夹test-annotations
    4.6 maven版本3.8.1起阻塞http请求解决办法:
        <mirror>
            <id>maven-default-http-blocker</id>
            <mirrorOf>external:dummy:*</mirrorOf>
            <name>Pseudo repository to mirror external repositories initially using HTTP.</name>
            <url>http://0.0.0.0/</url>
            <blocked>true</blocked>
        </mirror>
     4.7 maven资料链接: juejin.cn/post/7266293217054163000
## 5. idea使用
    5.1 工程名称和文件路径不一致：
        5.1.1 方法一：直接将该模块路径文件夹名称修改的跟模块名称一致；
        5.1.2 方法二：先移除掉该模块，然后重新将此模块加入到工程中；
    5.2 idea项目文件报红, 点击进去又不报红了问题
        5.2.1 原因: 可能是项目文件.iml文件导致的;
        5.2.2 解决办法: 打开idea的Terminal 终端, cd到对应的module模块路径下, 然后执行命令: mvn idea:module 即可.
    5.3 idea撤销远程分支提交记录
        5.3.1 使用idea绑定git的远程分支, check out检出指定分支代码
        5.3.2 在idea的git提交记录中找到需要回滚的提交记录版本号, 鼠标右键, 点击Reset Current Branch To Here..., 然后选择Hard模式方式回滚, 点击Reset
        5.3.3 然后强制推送到远程分支, 即可回滚到指定的提交版本号, 并删除此版本号之后的提交记录
        5.3.4 参考文档: https://blog.csdn.net/weixin_44328926/article/details/144292932
## 6. Git使用
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
    6.4 撤销commit
        -- 不删除空间代码改动,撤销commit,不撤销add
        git reset --soft HEAD^
        -- 删除空间代码改动,撤销commit,撤销add, 将本地需要push的记录清除掉, 并获取最新的远程分支指定版本号的记录
        git reset --hard 远程分支的版本号
    6.5 将本地指定版本号的commit记录提交到远程
        git cherry-pick 提交版本号1 提交版本号2 提交版本号3
## 7. SQL相关
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
                i. index_merge: 索引合并, 表示查询一张表时使用了多个索引的情况
                j. 查询效率从高到底的取值为; 所有的type字段取值: NULL > system > const > eq_ref > ref > fulltext > ref_or_null > index_merge > unique_subquery > index_subquery > range > index > ALL; 一般情况下type字段取值: system > const > eq_ref > ref > range > index > ALL
            6> 第六列possible_keys字段表示: 指出MySQL能使用哪个索引在表中找到记录, 查询涉及到的字段上若存在索引, 则该索引将被列出, 但不一定被查询使用
            7> 第七列key字段表示: 显示MySQL在查询中实际使用的索引, 若没有使用索引, 显示为NULL
            8> 第八列key_len字段表示: 索引中使用的字节数, 可通过该列计算查询中使用的索引的长度(key_len显示的值为索引字段的最大可能长度, 并非实际使用长度, 即key_len是根据表定义计算而得, 不是通过表内检索出的)
            9> 第九列ref字段表示: 上述表的连接匹配条件, 哪些列或常量被用于查找索引列上的值, 即某表的某个字段引用到了本表的索引字段
            10> 第十列rows字段表示: MySQL根据表统计信息及索引选用情况，估算的找到所需的记录所需要读取的行数
            11> 第十一列filtered字段表示: 返回结果与实际结果的差值占总记录数的百分比
            12> 第十二列Extra字段表示: 包含不适合在其他列中显示但十分重要的额外信息
                a. Using index: 1.该值表示相应的select操作中使用了覆盖索引(Covering Index: MySQL可以利用索引返回select列表中的字段, 
                                    而不必根据索引再次读取数据文件, 包含所有满足查询需要的数据的索引称为覆盖索引), 注意: 如果要使用覆盖索引, 
                                    一定要注意select列表中只取出需要的列, 不可select *, 因为如果将所有字段一起做索引会导致索引文件过大, 查询性能下降
                                2.此查询只使用索引中的信息，不需要访问数据行。这是一种高效的访问方式，通常不需要进一步优化
                b. Using where: 1.表示mysql服务器将在存储引擎检索行后再进行过滤, 即使用where条件包含了索引字段进行数据过滤, 
                                    而查询的字段通过索引无法全部查出需要回表进行查询. 如果where条件里涉及索引中的列, 当读取索引时就能被存储引擎检索, 
                                    但不是所有带where的查询都会显示"Using where". 有时"Using where"的出现就是一个暗示: 查询用到了不同
                                    的索引但需要回表进行其他数据查询. 注意: 索引设置不合理可能导致全表扫描, 例如删除状态或者性别字段设置索引会导致全表扫描
                                2.在存储引擎检索行之后，MySQL服务器将在更高层次进行额外的过滤操作。尽可能在索引中包含用于过滤条件的列，
                                    这样数据检索时可以更精确，减少MySQL服务层的过滤工作。
                c. Using temporary: 1.表示MySQL需要使用临时表来存储结果集, 常见于排序和分组查询
                                    2.MySQL在对查询结果排序或执行某些分组操作时使用了临时表。尝试改变查询结构，减少需要排序或分组的数据量，
                                        或者考虑增加能够支持排序和分组操作的索引。
                d. Using filesort: 1.MySQL中无法利用索引完成的排序操作称为"文件排序"
                                   2.MySQL需要进行额外的扫描来找到正确的行顺序，通常出现在对非索引列的排序查询中。增加合适的索引来直接支持
                                        排序操作，确保ORDER BY的列在索引中。
                e. Using join buffer: 1.该值强调了在获取连接条件时没有使用索引, 并且需要连接缓冲区来存储中间结果. 如果出现了这个值, 那应该注意, 
                                        根据查询的具体情况可能需要添加索引来改进能.
                                      2.用于JOIN操作时，涉及的表没有使用索引。为参与JOIN的列添加索引，以避免全表扫描和提高连接的效率。
                f. Impossible where: 这个值强调了where语句会导致没有符合条件的行.
                g. Select tables optimized away: 这个值意味着仅通过使用索引, 优化器可能仅从聚合函数结果中返回一行.
                h. Index merges: 当MySQL决定要在一个给定的表上使用超过一个索引的时候, 就会出现以下格式中的一个，详细说明使用的索引以及合并的类型。
                i. NULL: 没有用到额外的附加条件
                j. 常见情况的性能对比: Using index > NULL > Using where >= Using temporary > Using filesort
                k. Using intersect: 指会在查询时对使用到的索引执行同步扫描分别获取数据集，并对所获得的数据集取交集。
    7.2 MySQL索引存在在硬盘中(会在硬盘的指定文件中存储), 这样能避免服务宕机导致数据丢失, 当服务启动时会加载到内存中, 这样能提高索引查询效率
    7.3 MySQL的主键的最大值存储在表结构信息中, 同理7.2, 服务启动时会将表结构信息加载到内存中
    7.4 MySQL的varchar转int类型, 如果用字符串和数值进行比较, 此时字符串和数值都会转换成浮点数(此操作会导致该字段索引失效), 转换如下:
        1> SELECT CAST('abc123' AS SIGNED); -> 0
        2> SELECT CAST('123abc' AS SIGNED); -> 123
        3> SELECT CAST('12' AS SIGNED); -> 12
    7.5 MySQL更新时是使用表锁还是行锁? (注意这个要区分MySQL的引擎, MyISAM采用的是表锁, 只有InnoDB才有行锁)
        1> 如果是范围更新则采用的间隙锁
        2> 如果是索引字段为条件进行更新则采用行锁(如果索引失效也会采用表锁)
        3> 如果是非索引字段为条件进行更新则采用表锁
    7.6 统计MySQL数据库大表信息
        SELECT table_schema as '数据库', table_name as '表名', table_rows as '记录数', truncate(data_length/1024/1024, 2) as '数据容量(MB)', 
            truncate(index_length/1024/1024, 2) as '索引容量(MB)' FROM information_schema.tables WHERE table_rows > 1000000 ORDER BY table_rows DESC;
    7.7 MySQL流式查询(建了长连接,利用游标控制一次读取多少条数据)
        1> 在<select/>标签中新增fetchSize(每次获取的行数)和resultSetType(结果返回配置,例如:FORWARD_ONLY表示结果集的游标只能向下滚动)属性
        2> 采用注解方式在@Select标注的语句处加上@Options(设置resultSetType和fetchSize属性)和@ResultType(设置返回的结果对象类型,注意此注解标注的方法返回类型为void,结果在ResultHandler中处理)注解
## 8. Doris相关, 参考文档1: https://www.bookstack.cn/read/doris-1.2-zh/157a16c97e044495.md 参考文档2: https://doris.apache.org/zh-CN/docs/dev/lakehouse/multi-catalog/jdbc/
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
              'hostname' = 'mysql.dbstg.com.cn',
              'port' = '3116',
              'username' = 'test',
              'password' = 'password',
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
              "kafka_topic" = "topic_data_sync",
              "property.group.id" = "customer_info",
              "property.client.id" = "client_data_sync.jJL6CWtfFB",
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
    8.3 注意: doris数据库不支持update更新操作, doris的更新是将之前的数据标记为删除然后插入新的数据;在Doris Version 0.15.x+可以使用
        update命令来操作, 但是update命令只能在Unique数据模型的表中执行.
## 9. 代码整洁之道
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
