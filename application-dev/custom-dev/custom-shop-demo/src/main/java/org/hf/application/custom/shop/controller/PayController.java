package org.hf.application.custom.shop.controller;

import org.hf.application.custom.shop.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>  </p>
 * @author hufei
 * @date 2022/7/17 19:57
*/
@RestController
@RequestMapping(value = "/pay")
public class PayController {

    @Autowired
    private PayService payService;

    /***
     * 支付
     * type:支付类型  1：微信支付  2：支付宝支付
     * id:订单ID
     */
    @GetMapping(value = "/{type}/{id}")
    public String pay(@PathVariable(value = "type")String type,
                      @PathVariable(value = "id")String id){
        //支付
        payService.pay(type,id);
        return "SUCCESS";
    }
}
