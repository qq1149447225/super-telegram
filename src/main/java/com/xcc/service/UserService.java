package com.xcc.service;

import com.xcc.common.R;
import com.xcc.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author xcc
 * @version 1.0
 */


public interface UserService {

    R login(Map map, HttpServletRequest request);

    R<String> loginOut(HttpServletRequest request);

//    R getCode();
}
