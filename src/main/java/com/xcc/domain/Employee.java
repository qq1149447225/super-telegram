package com.xcc.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author xcc
 * @version 1.0
 */

@Data
@TableName(value = "employee")
public class Employee {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private  LocalDateTime createTime;//

    @TableField(fill = FieldFill.INSERT_UPDATE)//更新和插入时自动填充
    private LocalDateTime updateTime;//

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;
}
