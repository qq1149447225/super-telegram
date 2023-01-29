package com.xcc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xcc.common.R;
import com.xcc.domain.Category;
import com.xcc.domain.Employee;
import com.xcc.domain.Setmeal;
import com.xcc.dto.SetmealDto;
import com.xcc.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author xcc
 * @version 1.0
 */

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService Service;

    @GetMapping("/page")
    public R getPage(int page, int pageSize) {
        return Service.getPage(page, pageSize);
    }

    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto) {
        return Service.save(setmealDto);
    }

    /**
     * 删除套餐
     * 还要删除与之对应的套餐表中的数据
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R delete(@RequestParam List<Long> ids) {
        return Service.delete(ids);
    }

    @PutMapping
    public R update(@RequestBody SetmealDto setmealDto) {
        return Service.update(setmealDto);
    }


    /**
     * 根据套餐id回显套餐详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R getSetmeals(@PathVariable Long id) {
        return Service.getSetmeals(id);
    }

    @GetMapping("/list")
    public R getBusinessPackage(SetmealDto setmealDto) {
        return Service.getBusinessPackage(setmealDto);

    }
}
