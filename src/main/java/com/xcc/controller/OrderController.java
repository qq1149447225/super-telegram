package com.xcc.controller;

import com.xcc.common.R;
import com.xcc.domain.Orders;
import com.xcc.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xcc
 * @version 1.0
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping("/submit")
    public R submit(@RequestBody Orders orders){
        return service.submit(orders);
    }

    @GetMapping("/page")
    public R getPage(int page,int pageSize){
       return service.getPage(page,pageSize);
    }
}
