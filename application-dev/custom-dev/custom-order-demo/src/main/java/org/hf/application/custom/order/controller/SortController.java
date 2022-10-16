package org.hf.application.custom.order.controller;

import io.swagger.annotations.Api;
import org.hf.application.custom.order.component.SortTask;
import org.hf.application.custom.order.mapper.ProductMapper;
import org.hf.application.custom.order.pojo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/sort")
@Api(value = "多线程排序测试demo")
public class SortController {
    @Autowired
    ProductMapper mapper;

    @GetMapping("/list")
    List<Product> sort() throws ExecutionException, InterruptedException {
        //查商品列表
        List<Product> list = mapper.selectByExample(null);
        //线程池
        ForkJoinPool pool = new ForkJoinPool(2);
        //开始运算，拆分与合并
        Future<List<Product>> future = pool.submit(new SortTask(list));
        return future.get();
    }
}
