package com.xcc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.xcc.domain.Employee;
import com.xcc.common.R;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xcc
 * @version 1.0
 */


public interface EmployeeService {

    R<Employee> loginCheck(HttpServletRequest request, Employee employee);

    R<String> loginOut(HttpServletRequest request);

    R<String> save(HttpServletRequest request,Employee employee);

     R getPage(int page, int pageSize, String name);

     R update(HttpServletRequest request,Employee employee);

    Employee getById(Long id);
}
