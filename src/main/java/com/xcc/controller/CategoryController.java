package com.xcc.controller;

import com.xcc.common.R;
import com.xcc.domain.Category;
import com.xcc.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.PushBuilder;

/**
 * @author xcc
 * @version 1.0
 */

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService Service;

    @GetMapping("/page")
    public R getPage(int page,int pageSize){
        return Service.getPage(page, pageSize);
    }

    @PostMapping
    public R save(@RequestBody Category category){
       return Service.save(category);
    }

    @DeleteMapping
    public R delete( Long ids){
        return Service.delete(ids);
    }

    @PutMapping
    public R update(@RequestBody Category category){
        return Service.update(category);
    }

    @GetMapping("/list")
    public R getList(Category category){
        return Service.getList(category);
    }

}
