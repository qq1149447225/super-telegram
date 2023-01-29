package com.xcc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xcc.common.R;
import com.xcc.dao.CategoryDao;
import com.xcc.dao.DishDao;
import com.xcc.dao.SetmealDao;
import com.xcc.domain.Category;
import com.xcc.domain.Dish;
import com.xcc.domain.Employee;
import com.xcc.domain.Setmeal;
import com.xcc.service.CategoryService;
import com.xcc.service.DishService;
import com.xcc.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xcc
 * @version 1.0
 */

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private DishDao dishDao;

    @Autowired
    private SetmealDao setmealDao;

    @Override
    public R<String> save(Category category) {
        int insert = categoryDao.insert(category);

        if (insert > 0) {
            return R.success("成功");
        } else {
            return R.error("失败");
        }
    }

    @Override
    public R getPage(int page, int pageSize) {
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);
        return R.success(categoryDao.selectPage(categoryPage, queryWrapper));
    }

    @Override
    public R update(Category category) {
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(category != null, Category::getId, category.getId());
        int update = categoryDao.update(category, updateWrapper);
        if (update > 0) {
            return R.success("更新成功");
        } else {
            return R.error("更新失败");
        }
    }


    @Override
    public R delete(Long id) {
        //判断此分类下是否有菜品或者套餐
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        Long dishCount = dishDao.selectCount(queryWrapper);
        if (dishCount != 0) {
            return R.error("此分类关联有菜品,无法删除");
        }

        LambdaQueryWrapper<Setmeal> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(Setmeal::getCategoryId, id);
        Long selectCount = setmealDao.selectCount(Wrapper);
        if (selectCount != 0) {
            return R.error("此分类关联有套餐,无法删除");
        }

        int i = categoryDao.deleteById(id);

        if (i > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败");
        }
    }

    @Override
    public R getList(Category category) {
        if (category.getType() != null) {
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
            queryWrapper.eq(category.getId() != null, Category::getId, category.getId());
            List<Category> categories = categoryDao.selectList(queryWrapper);
            return R.success(categories);
        } else {
            List<Category> categories = categoryDao.selectList(null);
            return R.success(categories);
        }
    }


}
