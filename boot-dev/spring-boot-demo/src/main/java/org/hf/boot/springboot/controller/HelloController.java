package org.hf.boot.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p> 测试controller </p>
 * //@RestController    //表示处理器注解 组合注解， @Controller + @ResponseBody
 * @author hufei
 * @date 2022/8/13 8:50
*/
@RestController
public class HelloController {

    /**
     * //@GetMapping    // 限定了@RequestMapping的请求方法是get方法
     */
    @GetMapping("/hello")
    public String helloSpring(Integer type) throws Exception {
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
