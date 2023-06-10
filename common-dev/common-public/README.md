# common-public
公共配置

# 1. spring管理的bean发生方法内该类方法调用造成AOP失效时, 可以获取当前类的代理类,然后强转成当前类再调用方法, 
    具体应用:  (当前类)AopContext.currentProxy().当前类方法; 
    
# 2. 常用工具类
### 1. base64工具类: Base64Utils
### 2. AES加解密工具类: AesUtil
### 3. 解析El表达式工具类: ElUtil
### 4. RSA加解密工具类: RsaUtil
### 5. 解析自定义表达式工具类: ExpressionResolverUtil
### 6. json工具类: JsonUtils
### 7. map工具类: MapUtils
### 8. 解析Properties文件工具类: PropertiesUtil
### 9. 请求参数处理工具类: RequestParamUtil
### 10. 响应结果封装工具类: ResponseUtil
### 11. 获取spring容器对象工具类: SpringBeanUtil
### 12. 时间计算工具类: TimeUtil
### 13. 类型转换工具类: TypeConvertUtils
### 14. MD5工具类: Md5Util
### 15. IP地址工具类: LocalIpUtils
### 16. 图片处理工具类: PictureUtil
### 17. 对象属性处理工具类: ObjectFieldUtil

# 3. 自定义业务异常: org.hf.common.publi.exception
