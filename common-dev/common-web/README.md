# common-web
全局web配置

# 1. 出入参拦截处理
    入参拦截处理：org.hf.common.web.aop.RequestParameterAspect
    出参统一响应处理：org.hf.common.web.utils.ResponseUtil
# 2. 异常统一拦截处理
    相关实现的包：org.hf.common.web.exception
    org.hf.common.web.exception.handler.GlobalExceptionResolver
# 3. 数据脱敏
    相关实现的包：org.hf.common.web.desensitize
    使用: 在返回的实体对应属性上加@Sensitive(type = SensitiveTypeEnum.CHINESE_NAME)注解
    SensitiveTypeEnum为脱敏类型枚举
# 4. 自定义注解@ControllerPagination, 可以直接采用PageHelper分页
    相关实现的包：org.hf.common.web.mybatis.pagehelper
    使用方法: 
        1> 引入common-web依赖
        2> 在Controller层方法上添加@ControllerPagination
# 5. 自定义三种限流注解
    相关实现的包：org.hf.common.web.currentlimiting
    使用方法: 
        1> 基于guava的令牌桶限流注解, 在类方法上添加org.hf.common.web.currentlimiting.guava.TokenBucketLimiter注解
        2> 基于java自带的Semaphore限流注解, 在类方法上添加org.hf.common.web.currentlimiting.semaphore.ShLimiter注解
        3> 基于sentinel的限流注解, 在类方法上添加org.hf.common.web.currentlimiting.sentinel.SentinelLimiter
    示例参考: org.hf.springboot.web.controller.CurrentLimitController
# 6. 相关工具类：org.hf.common.web.utils