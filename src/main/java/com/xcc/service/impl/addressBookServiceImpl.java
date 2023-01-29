package com.xcc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.xcc.common.R;
import com.xcc.dao.addressBookDao;
import com.xcc.domain.AddressBook;
import com.xcc.service.addressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author xcc
 * @version 1.0
 */

@Service
public class addressBookServiceImpl implements addressBookService {

    @Autowired
    private addressBookDao addressBookDao;

    @Autowired
    private HttpServletRequest request;

    @Override
    public R add(AddressBook addressBook) {
        Long userId = (Long) request.getSession().getAttribute("user");
        addressBook.setUserId(userId);
        addressBookDao.insert(addressBook);
        return R.success("创建地址成功");
    }

    /**
     * 获取默认地址
     *
     * @return
     */
    @Override
    public R getDefaultAddress() {
        Long userId = (Long) request.getSession().getAttribute("user");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook defaultAddress = addressBookDao.selectOne(queryWrapper);
        return R.success(defaultAddress);
    }

    @Override
    public R getList() {
        Long userId = (Long) request.getSession().getAttribute("user");

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);

        List<AddressBook> addressBooks = addressBookDao.selectList(queryWrapper);
        return R.success(addressBooks);
    }

    @Override
    public R setDefault(AddressBook addressBook) {
        Long id = addressBook.getId();
        Long userId = (Long) request.getSession().getAttribute("user");

        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getId, id);

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);

        List<AddressBook> addressBooks = addressBookDao.selectList(queryWrapper);
        for (AddressBook addressBook1 : addressBooks) {
            if (addressBook1.getId() != id) {
                addressBook1.setIsDefault(0);
                addressBook1.setUpdateUser(userId);
                addressBookDao.updateById(addressBook1);
            } else {
                addressBook1.setIsDefault(1);
                addressBook1.setUpdateUser(userId);
                addressBookDao.updateById(addressBook1);
            }
        }

        return R.success("设置成功");
    }
}
