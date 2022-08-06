package org.hf.boot.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:hufei
 * @CreateTime:2020-09-03
 * @Description:
 */
@RestController         //表示处理器注解 组合注解， @Controller + @ResponseBody
public class HelloController {

//    @Autowired                              //注入数据源
//    private DataSource dataSource;    //单数据源注入
//    @Resource(name = "myRoutingDataSource")
//    private DataSource myRoutingDataSource;     //多数据源注入,获取的是默认数据源

//    @Value("${hufei.url}")
//    private String url;
//
//    @Value("${spring.url}")
//    private String url2;

    @GetMapping("hello")        //@GetMapping 限定了@RequestMapping的请求方法是get方法
    public String helloSpring(Integer type) throws Exception {
//        System.out.println("DataSource = " + dataSource);
//        System.out.println("url = " + url);
//        System.out.println("url2 = " + url2);

        if (type == null) {
            type = 0;
        }
        if(type == 1) {
            throw new Exception("抛出异常");
        } else if (type == 2) {
            System.out.println(10/0);
        }

        return "test hello springboot";
    }

}
