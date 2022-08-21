package org.hf.springcloud.consumer.service.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * <p> 用户处理器 </p>
 * //@RestController             //组合注解，指定该类为controller，并返回json格式字符串
 * //@RequestMapping("/user")    //指定该类的请求路径
 * //@Slf4j                      //采用lombok的方式引入日志管理
 * //@DefaultProperties(defaultFallback = "defaultMethodFailBack")        //指定该类方法请求错误的回调方法名称
 * @author hufei
 * @date 2022/8/21 17:13
*/
@RestController
@RequestMapping("/user")
@Slf4j
@DefaultProperties(defaultFallback = "defaultMethodFailBack")
public class UserController {
    /**
     * spring对httpClient、okHttp、JDK原生URLConnection进行了封装，RestTemplate的工具类对上述的3种http客户端工具类进行了封装
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 注入eureka服务地址获取工具类实例
     */
    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * //@RequestMapping("/{id}")            //执行方法请求路径及参数
     * //@HystrixCommand(fallbackMethod = "queryUserByIdFailBack")   //指定请求失败回调方法名称，针对这一个方法的回调方法
     * //@HystrixCommand         //开启统一处理，在类上指定错误回调方法
     * @param id 入参
     * @return String   //方法的返回类型和回调方法的返回类型要保持一致
     */
    @RequestMapping("/{id}")
    @HystrixCommand
    public String queryUserById(@PathVariable Long id){
        //服务熔断测试，服务请求失败达到一定的阀值时，会自动开启熔断器，默认请求不低于20次的情况下，失败率达到50%，休眠5秒后会处于半开状态
//        if (id == 1) throw new RuntimeException();
        /*String url = "http://localhost:8083/user/"+id;
        return restTemplate.getForObject(url, User.class);*/
        //获取eureka服务中的服务地址
//        List<ServiceInstance> serviceInstanceList = discoveryClient.getInstances("service-demo");
        //获取服务列表中的某一个服务，一般集群环境，这里需要用负载均衡的方式获取服务地址
//        ServiceInstance serviceInstance = serviceInstanceList.get(0);
//        String url = "http://" + serviceInstance.getHost() + ":" + serviceInstance.getPort() + "/user/" + id;
        //开启负载均衡后会根据服务提供应用名称：service-demo自动获取服务地址，默认轮询
        String url = "http://service-demo/user/"+id;
        //restTemplate可以对json格式字符串进行反序列化
        return restTemplate.getForObject(url, String.class);
    }

    /**
     * 指定服务降级回调方法，返回类型必须指定为String，参数也要保持一致
     * @param id 入参
     * @return String
     */
    public String queryUserByIdFailBack(Long id) {
        //记录日志，“{}”在这里是占位符，后面的参数id会替代这个占位符
        log.error("查询用户信息失败，用户id：{}", id);
        return "网络错误，请稍后重试！";
    }

    public String defaultMethodFailBack() {         //该类全局服务降级回调方法，返回类型必须指定为String
        return "默认提示：网络错误，请稍后重试！";
    }
}
