# common-config
公共配置类工程
自定义注解实现
自定义实体Bean注入
统一切面配置

# 注意: 引入common-config依赖的时候, 检查web工程的启动类路径是否是此工程类路径的父级或者同级
# 1. 封装elasticJob定时任务, 采用注解方式（注解：@ElasticSimpleJob）实现定时任务处理并兼容xml方式
    使用方法:
        1> 引入common-config依赖
        2> 创建作业类, 添加@Component、@ElasticSimpleJob注解, 实现SimpleJob接口
        3> 在application.properties文件中配置ej相关配置项
        
# 2. 自定义注解@JavaEnum枚举注解, 可以根据枚举类group获取所有枚举项
    使用方法:
        1> 引入common-config依赖
        2> 创建枚举类, 添加@JavaEnum("枚举group")注解, 实现NameValueEnum或其子接口
        3> 普通使用, NameValueEnum.getEnum(枚举类.class, value)方式获取获取项对应枚举项, 其他使用方法见NameValueEnum
        
# 3. 自定义@EventListenerAnnotation事件注解, 可同步进行事件的发布和监听
    使用方法:
        1> 引入common-config依赖
        2> 创建事件实体, 继承EventBaseAbstract或者实现EventBase
        3> 创建事件监听器处理类添加@EventListenerAnnotation, 继承EventListenerAbstract<第二步创建的事件实体>或者实现EventListener<第二步创建的事件实体>
        4> 在需要的bean中注入EventPublisher对象, 进行事件发布

# 4. 自定义注解@ServicePagination, 可以直接采用PageHelper分页
    使用方法: 
        1> 引入common-config依赖
        2> 在service层方法上添加@ServicePagination

# 5. 自定义注解@CustomBeanReplace实现spring容器中的bean替换
    使用方法:
        1> 引入common-config依赖
        2> 创建需要替换接口的实现类, 添加@CustomBeanReplace(value="被替换bean的名称")
        3> 在application.properties文件中配置replaceBean.scannerPackages替换扫描包配置项, 支持org.hf.*.config的方式配置

# 6. 自定义事务注解实现手动提交事务
    使用方法:
        在对应的方法上添加@CustomTransactional(rollbackFor = Exception.class), 如果回滚异常类型是Exception.class则可以不传,此为默认值

# 7. 比较两个对象是否相等
    使用方法:
        创建org.hf.common.config.comparator.ObjectComparator的任意一个实现类org.hf.common.config.comparator.FieldBaseObjectComparator(推荐)
        或者org.hf.common.config.comparator.GetterBaseObjectComparator对象, 然后调用方法即可

