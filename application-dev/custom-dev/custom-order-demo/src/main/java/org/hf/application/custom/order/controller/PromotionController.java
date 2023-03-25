package org.hf.application.custom.order.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.hf.application.custom.order.mapper.ProductMapper;
import org.hf.application.custom.order.pojo.entity.Product;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p> 库存操作 </p>
 *
 * @author hufei
 * @date 2023/3/25 17:42
 */
@Api(value = "多线程库存demo")
@RestController
@RequestMapping("/promotion")
public class PromotionController {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ThreadPoolTaskExecutor customTaskExecutor;

    /**
     * 思考：不用ConcurrentHashMap可以吗？
     */
    Map<Integer, AtomicInteger> products = new HashMap<>();

    /**
     * 热加载数据: bean初始化后,立即加载库存数据
     * @return Map<Integer, AtomicInteger>
     */
    @ApiOperation(value = "热加载库存")
    @GetMapping("/load")
    @PostConstruct
    public Map<Integer, AtomicInteger> load() {
        products.clear();
        List<Product> list = productMapper.selectByExample(null);
        list.forEach(p -> products.put(p.getId(), new AtomicInteger(p.getNum())));
        return products;
    }

    @ApiOperation(value = "查库存信息")
    @GetMapping("/query")
    public Map<Integer, AtomicInteger> reload() {
        return products;
    }

    @ApiOperation(value = "统计下单信息")
    @GetMapping("/count")
    public List<HashMap<Integer, Integer>> count() {
        return productMapper.count();
    }

    @ApiOperation(value = "抢购")
    @GetMapping("/go")
    public void go(int productId) {
        // 一般采用消息队列来进行异步排队,消费端按入队顺序逐个消费,界面轮询结果, 这里采用预热后多线程下单,主要是为了验证多线程
        for (int i = 0; i < 10; i++) {
            CompletableFuture.runAsync(() -> {
                int count = 0;
                long userId = Thread.currentThread().getId();
                while (products.get(productId).getAndDecrement() > 0) {
                    count++;
                    //扔消息队列，异步处理
                    rabbitTemplate.convertAndSend("promotion.order", productId + "," + userId);
                }
                System.out.println(Thread.currentThread().getName() + "抢到:" + count);
            }, customTaskExecutor);
        }
    }

    @ApiOperation(value = "初始化db测试数据")
    @GetMapping("/init")
    public int init() {
        Product product = new Product();
        productMapper.deleteByExample(null);
        for (int i = 0; i < 20; i++) {
            product.setName("商品" + i);
            product.setNum(new Random().nextInt(100));
            product.setPrice(new Random().nextInt(1000) + 0.00f);
            productMapper.insert(product);
        }
        return 1;
    }
}
