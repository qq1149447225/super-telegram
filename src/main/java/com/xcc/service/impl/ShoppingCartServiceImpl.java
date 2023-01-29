package com.xcc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xcc.common.R;
import com.xcc.dao.ShoppingCartDao;
import com.xcc.domain.ShoppingCart;
import com.xcc.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xcc
 * @version 1.0
 */

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private HttpServletRequest request;


    @Override
    public R add(ShoppingCart shoppingCart) {
        //添加用户id
        Long userId = (Long) request.getSession().getAttribute("user");

        shoppingCart.setUserId(userId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
//        queryWrapper.eq(shoppingCart.getDishFlavor() != null, ShoppingCart::getDishFlavor, shoppingCart.getDishFlavor());

        ShoppingCart shoppingCart1 = shoppingCartDao.selectOne(queryWrapper);

        //判断购物车中是否有当前套餐
        if (shoppingCart1 == null) {
            // 购物车中没有 添加到购物车数据库
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartDao.insert(shoppingCart);
        } else {
            //购物车已经存在
            LambdaUpdateWrapper<ShoppingCart> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ShoppingCart::getId, shoppingCart1.getId());
            Integer number = shoppingCart1.getNumber();
            shoppingCart1.setNumber(number + 1);
            shoppingCartDao.update(shoppingCart1, updateWrapper);
        }

        return R.success("添加成功!");
    }

    @Override
    public R<List<ShoppingCart>> getList() {
        //获取用户id
        Long userId = (Long) request.getSession().getAttribute("user");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> shoppingCarts = shoppingCartDao.selectList(queryWrapper);

        return R.success(shoppingCarts);
    }

    @Override
    public R sub(ShoppingCart shoppingCart) {

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(shoppingCart.getDishId() != null, ShoppingCart::getDishId, shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId() != null, ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        ShoppingCart shoppingCart1 = shoppingCartDao.selectOne(queryWrapper);

        if (shoppingCart1.getNumber() == 1) {
            //购物车中只有一个 则删除
            shoppingCartDao.deleteById(shoppingCart1.getId());
        } else {
            //数量减少一
            shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
            shoppingCartDao.updateById(shoppingCart1);
        }

        return R.success("成功!");
    }

    @Override
    public R clean() {
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        shoppingCartDao.delete(queryWrapper);
        return R.success("清空成功");
    }
}
