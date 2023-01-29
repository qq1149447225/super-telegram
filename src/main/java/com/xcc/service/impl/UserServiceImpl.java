package com.xcc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xcc.common.R;
import com.xcc.dao.UserDao;
import com.xcc.domain.User;
import com.xcc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xcc
 * @version 1.0
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 用户验证码登录
     *
     * @param map
     * @return
     */
    @Override
    public R login(Map map, HttpServletRequest request) {
        String phone = (String) map.get("phone");
        Object code = map.get("code");

        String phone1 = (String) request.getSession().getAttribute("phone");
        String code1 = (String) request.getSession().getAttribute("code");

        if (phone.equals(phone1) && code.equals(code1)) {
            //判断当前手机号是否注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(phone != null, User::getPhone, phone);
            User user1 = userDao.selectOne(queryWrapper);
            if (user1 != null) {
                request.getSession().setAttribute("user", user1.getId());
                return R.success("成功!");
            } else {
                //没有注册 则注册
                User user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userDao.insert(user);
                request.getSession().setAttribute("user", user.getId());
                return R.success("成功!");
            }
        } else {
            return R.error("验证码错误");
        }

    }

    @Override
    public R<String> loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return R.success("退出登录成功!");
    }

}
