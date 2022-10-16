package org.hf.application.custom.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hf.application.custom.order.mapper.ProductMapper;
import org.hf.application.custom.order.pojo.entity.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/promotion")
@Api(value = "多线程库存demo")
public class PromotionController {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    RabbitTemplate template;

    //思考：不用ConcurrentHashMap可以吗？
    Map<Integer,AtomicInteger> products = new HashMap();

    //热加载数据
    @GetMapping("/load")
    @ApiOperation(value = "热加载库存")
    //bean初始化后，立刻热加载
    @PostConstruct
    public Map load(){
        products.clear();
        List<Product> list = productMapper.selectByExample(null);
        list.forEach(p -> {
            products.put(p.getId(),new AtomicInteger(p.getNum()));
        });
        return products;
    }

    @GetMapping("/query")
    @ApiOperation(value = "查库存信息")
    public Map reload(){
        return products;
    }
    @GetMapping("/count")
    @ApiOperation(value = "统计下单信息")
    public List count(){
        return productMapper.count();
    }

    //抢购
    @GetMapping("/go")
    @ApiOperation(value = "抢购")
    public void go(int productId){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                int count = 0;
                long userId = Thread.currentThread().getId();
                while (products.get(productId).getAndDecrement() > 0){
                    count++;
                    //扔消息队列，异步处理
                    template.convertAndSend("promotion.order",productId+","+userId);
                }
                System.out.println(Thread.currentThread().getName()+"抢到:"+count);
            }).start();
        }
    }

    //初始化数据库数据
    @GetMapping("/init")
    @ApiOperation(value = "初始化db测试数据")
    public int init(){
        Product product = new Product();
        productMapper.deleteByExample(null);
        for (int i = 0; i < 20; i++) {
            product.setName("商品"+i);
            product.setNum(new Random().nextInt(100));
            product.setPrice(new Random().nextInt(1000)+0.00f);
            productMapper.insert(product);
        }
        return 1;
    }
}
