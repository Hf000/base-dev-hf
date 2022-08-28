# common-web
全局web配置

# 1. 入参统一处理
# 2. 接口响应统一处理
# 3. 配置全局统一过滤器
# 4. 配置全局统一拦截器
# 5. 异常统一拦截处理
    org.hf.common.web.exception.handler.GlobalExceptionResolver
# 6. 数据脱敏
    使用: 在返回的实体对应属性上加@Sensitive(type = SensitiveTypeEnum.CHINESE_NAME)注解
    SensitiveTypeEnum为脱敏类型枚举
# 7. 自定义注解@ControllerPagination, 可以直接采用PageHelper分页
    使用方法: 
        1> 引入common-web依赖
        2> 在Controller层方法上添加@ControllerPagination
