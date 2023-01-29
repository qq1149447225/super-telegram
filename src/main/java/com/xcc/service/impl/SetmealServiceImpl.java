package com.xcc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xcc.common.R;
import com.xcc.dao.CategoryDao;
import com.xcc.dao.SetmealDao;
import com.xcc.dao.SetmealDishDao;
import com.xcc.domain.Category;
import com.xcc.domain.Setmeal;
import com.xcc.domain.SetmealDish;
import com.xcc.dto.SetmealDto;
import com.xcc.service.SetmealService;
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
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private com.xcc.dao.SetmealDao SetmealDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private SetmealDishDao setmealDishDao;

    /**
     * 操作 setmeal 和 setmealDish 表
     * 新建套餐
     *
     * @param setmealDto
     * @return
     */
    @Override
    @Transactional
    public R<String> save(SetmealDto setmealDto) {
        //操作setmeal 表
        int insert = SetmealDao.insert(setmealDto);

        //得到 setmeal 的 id
        Long setmealId = setmealDto.getId();

        //设置setmealDish的 setmeal_id
        //操作setmeal_Dish 表
        List<SetmealDish> setmealDishS = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishS) {
            setmealDish.setSetmealId(setmealId);
            setmealDishDao.insert(setmealDish);
        }

        if (insert > 0) {
            return R.success("成功");
        } else {
            return R.error("失败");
        }
    }

    @Override
    public R getPage(int page, int pageSize) {
        Page<Setmeal> setmealPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Setmeal::getId);
        Page<Setmeal> selectPage = SetmealDao.selectPage(setmealPage, queryWrapper);

        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(selectPage, setmealDtoPage, "records");

        List<Setmeal> setmealList = selectPage.getRecords();

        ArrayList<SetmealDto> setmealDtos = new ArrayList<>();

        for (Setmeal setmeal : setmealList) {
            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(setmeal, setmealDto);

            Category category = categoryDao.selectById(setmeal.getCategoryId());
            String categoryName = category.getName();

            setmealDto.setCategoryName(categoryName);

            setmealDtos.add(setmealDto);
        }

        setmealDtoPage.setRecords(setmealDtos);

        return R.success(setmealDtoPage);
    }

    @Override
    public R update(SetmealDto setmealDto) {

        //先删除相关的菜品
        //得到setmeal_id
        Long setmealId = setmealDto.getId();
        //根据setmeal_id在 setmealdish 表中查找多条数据
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealId);
        //执行删除
        setmealDishDao.delete(queryWrapper);

        //插入套餐菜品表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
            setmealDishDao.insert(setmealDish);
        }


        //修改套餐表
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Setmeal::getId, setmealDto.getId());
        int update = SetmealDao.update(setmealDto, updateWrapper);
        if (update > 0) {
            return R.success("更新成功");
        } else {
            return R.error("更新失败");
        }
    }

    /**
     * 删除套餐
     * 还要删除与之对应的套餐表中的数据
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional
    public R delete(List<Long> ids) {

        for (Long id : ids) {
            SetmealDao.deleteById(id);

            //删除与之对应的套餐表中的数据
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, id);
            setmealDishDao.delete(queryWrapper);
        }

        return R.success("删除成功");
    }

    /**
     * 根据套餐id回显套餐详情
     *
     * @param id
     * @return
     */
    @Override
    public R getSetmeals(Long id) {
        Setmeal setmeal = SetmealDao.selectById(id);
        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setmealDishDao.selectList(queryWrapper);

        setmealDto.setSetmealDishes(setmealDishList);

        return R.success(setmealDto);
    }

    @Override
    public R getBusinessPackage(SetmealDto setmealDto) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmealDto.getCategoryId() != null, Setmeal::getCategoryId, setmealDto.getCategoryId());
        queryWrapper.eq(setmealDto.getStatus() != null, Setmeal::getStatus, setmealDto.getStatus());
        List<Setmeal> selectList = SetmealDao.selectList(queryWrapper);
        return R.success(selectList);
    }
}
