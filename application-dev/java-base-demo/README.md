# java-base-demo
java基础

## 1. 算法: org.hf.application.javabase.algorithm
## 2. 设计模式: org.hf.application.javabase.design.patterns
    1> behavioral: 行为型模式
        1-observer: 观察者模式
        2-state: 状态模式
        3-strategy: 策略模式
    2> creational: 创建型模式
        1-builder: 建造者模式
        2-factory: 工厂模式
        3-prototype: 原型模式
        4-singleton: 单例模式
    3> structural: 结构型模式
        1-adapter: 适配器模式
        2-decorator: 装饰者模式
        3-flyweight: 享元模式
        4-proxy: 代理模式
    4> 小结
        1. 策略模式和代理模式应用场景的区别: 
            策略模式: 根据不同的业务场景选择不同策略进行实现,是知道执行者的
            代理模式: 控制对原始对象的访问,不关注被代理对象,关注调用时的逻辑增强
        2. 装饰者模式和代理模式应用场景的区别:
           装饰者模式: 关注的是在一个对象原有逻辑上动态的新增方法逻辑
           代理模式: 控制对原始对象的访问,不关注被代理对象,关注调用时的逻辑增强
## 3. 压制警告 @SuppressWarnings
    类型: 
        all: 抑制所有警告
        boxing :抑制装箱、拆箱操作时候的警告
        cast: 抑制映射相关的警告
        dep-ann: 抑制启用注释的警告
        deprecation: 抑制过期方法警告
        fallthrough: 抑制在 switch 中缺失 breaks 的警告
        finally :抑制 finally 模块没有返回的警告
        hiding: 抑制相对于隐藏变量的局部变量的警告
        incomplete-switch: 忽略不完整的 switch 语句
        nls: 忽略非 nls 格式的字符
        null :忽略对 null 的操作
        rawtypes: 使用 generics 时忽略没有指定相应的类型
        restriction: 抑制禁止使用劝阻或禁止引用的警告
        serial: 忽略在 serializable 类中没有声明 serialVersionUID 变量
        static-access: 抑制不正确的静态访问方式警告
        synthetic-access: 抑制子类没有按最优方法访问内部类的警告
        unchecked: 抑制没有进行类型检查操作的警告
        unqualified-field-access : 抑制没有权限访问的域的警告
        unused: 抑制没被使用过的代码的警告
