package com.xcc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.xcc.common.R;
import com.xcc.dao.CategoryDao;
import com.xcc.dao.DishDao;
import com.xcc.dao.DishFlavorDao;
import com.xcc.domain.Category;
import com.xcc.domain.Dish;
import com.xcc.domain.DishFlavor;
import com.xcc.domain.Employee;
import com.xcc.dto.DishDto;
import com.xcc.service.CategoryService;
import com.xcc.service.DishService;
import org.apache.coyote.OutputBuffer;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xcc
 * @version 1.0
 */

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishDao DishDao;

    @Autowired
    private DishFlavorDao dishFlavorDao;

    @Autowired
    private CategoryDao categoryDao;

    @Override
    @Transactional
    public R<String> save(DishDto dishDto) {

        int insert = DishDao.insert(dishDto);

        Long dishId = dishDto.getId();

        List<DishFlavor> flavors = dishDto.getFlavors();

        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishId);
            dishFlavorDao.insert(flavor);
        }

        if (insert > 0) {
            return R.success("成功");
        } else {
            return R.error("失败");
        }
    }

    @Override
    public R getPage(int page, int pageSize, String name) {
//        Page<Dish> dishPage = new Page<>(page,pageSize);
//
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//
//        queryWrapper.like(!StringUtils.isNullOrEmpty(name),Dish::getName,name);
//
//        queryWrapper.orderByDesc(Dish::getSort);
//
//        return R.success(DishDao.selectPage(dishPage,queryWrapper));

        Page<Dish> dishPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.like(!StringUtils.isNullOrEmpty(name), Dish::getName, name);

        queryWrapper.orderByDesc(Dish::getSort);

        Page<Dish> selectPage = DishDao.selectPage(dishPage, queryWrapper);

        Page<DishDto> dishDtoPage = new Page<>();
        // dishPage 克隆 到 dishDtoPage
        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        List<Dish> dishList = selectPage.getRecords();

        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish dish : dishList) {

            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(dish, dishDto);

            //根据id查 categoryName
            Category category = categoryDao.selectById(dish.getCategoryId());
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);

            dishDtoList.add(dishDto);
        }

        dishDtoPage.setRecords(dishDtoList);

        return R.success(dishDtoPage);
    }

    @Override
    @Transactional
    public R update(DishDto dishDto) {

        //执行更新之前删除口味相关信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        List<DishFlavor> dishFlavors = dishFlavorDao.selectList(queryWrapper);

        for (DishFlavor dishFlavor : dishFlavors) {
            if (dishFlavor != null) {
                dishFlavorDao.deleteById(dishFlavor.getId());
            }
        }

        //增加 dishFlavor
        List<DishFlavor> flavors = dishDto.getFlavors();

        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDto.getId());
            dishFlavorDao.insert(flavor);
        }

        //增加 dish
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Dish::getId, dishDto.getId());
        int update = DishDao.update(dishDto, updateWrapper);
        if (update > 0) {
            return R.success("更新成功");
        } else {
            return R.error("更新失败");
        }
    }


    @Override
    public R delete(Long id) {
        int i = DishDao.deleteById(id);
        if (i > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    /**
     * 数据回显
     *
     * @param id
     * @return
     */
    @Override
    public R<DishDto> getDishById(Long id) {

        Dish dish = DishDao.selectById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = dishFlavorDao.selectList(queryWrapper);

        dishDto.setFlavors(dishFlavors);

        return R.success(dishDto);
    }

    /**
     * 查询当前分类下的所有菜品
     * 口味
     * @param dish
     * @return
     */
    @Override
    public R<List<DishDto>> getDish(Dish dish) {

        //查询当前分类下的所有菜品集合
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> dishList = DishDao.selectList(queryWrapper);

        //根据菜品查询口味
        ArrayList<DishDto> dishDtos = new ArrayList<>();

        for (Dish dish1 : dishList) {
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,dish1.getId());
            List<DishFlavor> dishFlavors = dishFlavorDao.selectList(queryWrapper1);

            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(dish1,dishDto);
            dishDto.setFlavors(dishFlavors);

            dishDtos.add(dishDto);
        }

        return R.success(dishDtos);
    }

}
