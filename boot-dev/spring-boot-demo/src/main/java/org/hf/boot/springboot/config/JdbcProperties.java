package org.hf.boot.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p> jdbc配置项类 通过属性注入的方式引入jdbc配置项 </p>
 * 配合org.hf.boot.springboot.config.JdbcConfig#getDataSource()方法使用
 * //@ConfigurationProperties(prefix = "jdbc")  //从springboot配置文件application.properties中读取配置项信息，prefix表示配置项前缀jdbc, 需要绑定一个指定该配置项类的配置类, 该注解也可以作用在方法上（此时则不需要绑定配置类了）
 * @author hufei
 * @date 2022/8/13 11:02
*/
//@ConfigurationProperties(prefix = "jdbc")
public class JdbcProperties {

    /**
     * 配置类中的变量名称要和配置文件application.properties中前缀之后的key保持 松散绑定（相同）
     */
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    public String getDriverClassName() {            //生成get/set方法的快捷键：alt + insert
        return driverClassName;
    }
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
