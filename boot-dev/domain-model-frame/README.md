# domain-model-frame 基于领域模型划分的微服务框架结构
    
## 1. 微服务包层级划分, 服务层调用关系, 应用 -> 领域 -> 基础, 不能反向调用, web层一般为配置, 不建议依赖服务层, 可根据实际应用场景定义依赖关系
    一.服务层
    1> 服务层包路径: org.hf.domain.model.frame.custom
    (1) 服务应用层
    2> 微服务应用层包路径: org.hf.domain.model.frame.custom.application
    3> 应用层controller路径: org.hf.domain.model.frame.custom.application.controller
    4> 应用层提供给hub层访问的api接口: org.hf.domain.model.frame.custom.application.controller.api
    5> 应用层提供给各微服务间访问的接口: org.hf.domain.model.frame.custom.application.controller.spi
    6> 应用层定时任务包路径: org.hf.domain.model.frame.custom.application.job
    7> 应用层定时任务包路径: org.hf.domain.model.frame.custom.application.mq
    (2) 服务领域层
    8> 微服务领域层包路径: org.hf.domain.model.frame.custom.domain
    9> 领域层常量包路径: org.hf.domain.model.frame.custom.domain.constant
    10> 领域层业务实体包路径: org.hf.domain.model.frame.custom.domain.dto
    11> 领域层业务逻辑处理包路径: org.hf.domain.model.frame.custom.domain.service
    (3) 服务基础设施层
    12> 微服务基础服务层包路径: org.hf.domain.model.frame.custom.infrastructure
    13> 基础服务层领域对象包路径: org.hf.domain.model.frame.custom.infrastructure.dao
    14> 基础服务层领域数据库表实体包路径: org.hf.domain.model.frame.custom.infrastructure.dao.entity
    15> 基础服务层领域数据访问包路径: org.hf.domain.model.frame.custom.infrastructure.dao.repository
        说明: dao接口统一在这里创建, 例如:xxxDao, 通过这里去访问对应的mapper接口
    16> 基础服务层领域数据库表对象mapper接口包路径: org.hf.domain.model.frame.custom.infrastructure.dao.repository.mapper
        说明: 数据库表对象映射的mapper接口在这里定义, 只允许dao层接口访问
    17> 基础服务层访问其他微服或者第三方接口包路径: org.hf.domain.model.frame.custom.infrastructure.facade
        说明: 微服访问其他服务的接口均通过这里的接口对接, 不能直接在业务逻辑中进行其他服务的调用
    18> 基础服务层工具类包路径: org.hf.domain.model.frame.custom.infrastructure.util
    二. 服务web配置层
    19> web层包路径: org.hf.domain.model.frame.web
    20> 微服务配置类包路径: org.hf.domain.model.frame.web.config
    21> 微服务过滤器类包路径: org.hf.domain.model.frame.web.filter
    22> 微服务拦截器类包路径: org.hf.domain.model.frame.web.interceptor