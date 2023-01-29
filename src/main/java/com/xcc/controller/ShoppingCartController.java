package com.xcc.controller;

import com.xcc.common.R;
import com.xcc.domain.ShoppingCart;
import com.xcc.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xcc
 * @version 1.0
 */

@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService service;

    /**
     * 添加购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        return service.add(shoppingCart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> getList(){
        return service.getList();
    }

    @PostMapping("/sub")
   public R sub(@RequestBody ShoppingCart shoppingCart){
        return service.sub(shoppingCart);
    }

    @DeleteMapping("/clean")
    public R clean(){
        return service.clean();
    }
}
