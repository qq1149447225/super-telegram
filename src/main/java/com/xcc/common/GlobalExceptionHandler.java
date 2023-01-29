package com.xcc.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 全局异常处理
 * @author xcc
 * @version 1.0
 */

@Slf4j
@RestControllerAdvice//拦截
public class GlobalExceptionHandler {


    @ExceptionHandler(SQLException.class)//捕获全局sql异常
    public R<String> exceptionHandler(SQLException e){
        log.error(e.getMessage());
        if (e.getMessage().contains("Duplicate entry")){
            return R.error("用户名已存在");
        }
        return R.error("未知异常");
    }
}
