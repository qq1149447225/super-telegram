package com.xcc.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 自动填充
 *
 * @author xcc
 * @version 1.0
 */

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Autowired
    private HttpServletRequest request;


    /**
     * 执行数据库插入时执行
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("执行插入自动填充");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        Object createUserId = request.getSession().getAttribute("employee");
        if (createUserId == null) {
            metaObject.setValue("createUser", (Long) request.getSession().getAttribute("user"));
        } else {
            metaObject.setValue("createUser", (Long) request.getSession().getAttribute("employee"));

        }

        Object updateUserId = request.getSession().getAttribute("employee");
        if (updateUserId == null) {
            metaObject.setValue("updateUser", (Long) request.getSession().getAttribute("user"));
        } else {
            metaObject.setValue("updateUser", (Long) request.getSession().getAttribute("employee"));
        }

    }


    /**
     * 执行数据库更新时执行
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("执行更新自动填充");
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long adminId = (Long) request.getSession().getAttribute("employee");
        metaObject.setValue("updateUser", adminId);
    }
}
