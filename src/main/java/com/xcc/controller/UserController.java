package com.xcc.controller;

import com.xcc.common.R;
import com.xcc.domain.User;
import com.xcc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xcc
 * @version 1.0
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R login(@RequestBody Map map, HttpServletRequest request){
        System.out.println();

        return userService.login(map,request);
    }

    @PostMapping("/sendMsg")
    public R sendMsg(@RequestBody User user,HttpServletRequest request){
        String phone = user.getPhone();
        String code = "1234";
        request.getSession().setAttribute("phone",phone);
        request.getSession().setAttribute("code",code);
        return R.success("发送验证码成功");
    }

    @PostMapping("/loginout")
    public R loginOut(HttpServletRequest request){
        return userService.loginOut(request);
    }
}
