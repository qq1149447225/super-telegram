package com.xcc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.xcc.domain.Employee;
import com.xcc.common.R;
import com.xcc.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xcc
 * @version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @PostMapping("/login")
    public R<Employee> check(HttpServletRequest request, @RequestBody Employee employee) {
        return service.loginCheck(request, employee);
    }

    @PostMapping("/logout")
    public R<String> check(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return service.loginOut(request);
    }

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        return service.save(request, employee);
    }

    @GetMapping("/page")
    public R getPage(int page, int pageSize, String name) {
        return service.getPage(page, pageSize, name);
    }

    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        return service.update(request,employee);
    }

    @GetMapping("/{id}")
    public R<Employee> update(@PathVariable Long id){
        Employee employee = service.getById(id);
        return R.success(employee);
    }
}
