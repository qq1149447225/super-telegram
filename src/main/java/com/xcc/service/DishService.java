package com.xcc.service;

import com.xcc.common.R;
import com.xcc.domain.Category;
import com.xcc.domain.Dish;
import com.xcc.domain.Employee;
import com.xcc.dto.DishDto;

/**
 * @author xcc
 * @version 1.0
 */


public interface DishService {

    //新增菜品,同时插入菜品对应的口味数据,需要操作两张表: dish dish_flavor
    R<String> save(DishDto dishDto);

    R getPage(int page, int pageSize, String name);

    R update(DishDto dish);

    R delete(Long id);

    R getDishById(Long id);

    R getDish(Dish dish);
}
