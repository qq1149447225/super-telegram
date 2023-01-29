package com.xcc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xcc.common.R;
import com.xcc.dao.*;
import com.xcc.domain.*;
import com.xcc.service.OrderDetailService;
import com.xcc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.compiler.ast.Variable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xcc
 * @version 1.0
 */

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private com.xcc.dao.addressBookDao addressBookDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Autowired
    private HttpServletRequest request;

    @Override
    @Transactional
    public R submit(Orders orders) {
//        addressBookId: "1417414926166769667"
//        payMethod: 1
//        remark: ""

        //根据地址id找到地址信息
        AddressBook addressBook = addressBookDao.selectById(orders.getAddressBookId());

        //获取用户id 得到一个用户
        Long userId = (Long) request.getSession().getAttribute("user");
        User user = userDao.selectById(userId);

        //获取购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartDao.selectList(queryWrapper);

        //生成一个订单号
        long orderId = IdWorker.getId();

        //总金额
        AtomicInteger amount = new AtomicInteger(0);

        //遍历购物车数据加入到订单中
        for (ShoppingCart shoppingCart : shoppingCarts) {

            //订单详细////////////////////////////////////////////////////////
            OrderDetail orderDetail = new OrderDetail();
            //主键 id 自动生成

            //name
            orderDetail.setName(user.getName());

            //图片

            //订单id
            orderDetail.setOrderId(orderId);

            //菜品id
            orderDetail.setDishId(shoppingCart.getDishId());

            //套餐Id
            orderDetail.setSetmealId(shoppingCart.getSetmealId());

            //口味
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());

            //数量
            orderDetail.setNumber(shoppingCart.getNumber());

            //金额
            orderDetail.setAmount(shoppingCart.getAmount());

            //计算总金额 原子性
            amount.addAndGet(shoppingCart.getAmount().multiply(new BigDecimal(shoppingCart.getNumber())).intValue());


            //订单/////////////////////////////////////////////////////////
            //主键id 用雪花算法自动生成

            //订单号
            orders.setNumber(String.valueOf(orderId));

            //订单状态 1待付款，2待派送，3已派送，4已完成，5已取消 默认 1
            orders.setStatus(2);

            //下单用户
            orders.setUserId(userId);

            //地址id
            orders.setAddressBookId(addressBook.getId());

            //下单时间
            orders.setOrderTime(LocalDateTime.now());

            //结帐时间
            orders.setCheckoutTime(LocalDateTime.now());

            //支付方式 已封装

            //备注 已封装

            //phone
            orders.setPhone(addressBook.getPhone());

            //userName
            orders.setUserName(user.getName());

            //consignee
            orders.setConsignee(addressBook.getConsignee());

            //地址
            String address = (addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                    + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                    + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                    + (addressBook.getDetail() == null ? "" : addressBook.getDetail());

            orders.setAddress(address);

            orderDetailDao.insert(orderDetail);
        }
        //总金额
        orders.setAmount(new BigDecimal(amount.get()));

        //
        orderDao.insert(orders);

        //清空当前用户的购物车
        shoppingCartDao.delete(queryWrapper);

        log.info("用户{}下单成功，金额{}",userId,amount.get());
        return R.success("下单成功！");
    }

    @Override
    public R getPage(int page, int pageSize) {
        Page<Orders> ordersPage = new Page<>(page,pageSize);
        Page<Orders> selectPage = orderDao.selectPage(ordersPage, null);
        return R.success(selectPage);
    }
}
