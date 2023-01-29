package com.xcc.controller;

import com.xcc.common.R;
import com.xcc.domain.Category;
import com.xcc.domain.Dish;
import com.xcc.dto.DishDto;
import com.xcc.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xcc
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService Service;

    @GetMapping("/page")
    public R getPage(int page, int pageSize, String name) {
        return Service.getPage(page, pageSize, name);
    }

    @PostMapping
    public R save(@RequestBody DishDto dish) {
        return Service.save(dish);
    }

    @DeleteMapping
    public R delete(Long ids) {
        return Service.delete(ids);
    }

    @PutMapping
    public R update(@RequestBody DishDto dishDto) {
        return Service.update(dishDto);
    }

    /**
     * 数据回显
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public R update(@PathVariable Long id) {
        System.out.println(id);
        return Service.getDishById(id);
    }

    /**
     * 查询当前分类下的所有菜品
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R getDish(Dish dish) {
        return Service.getDish(dish);
    }
}
