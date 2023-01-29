package com.xcc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.xcc.dao.EmployeeDao;
import com.xcc.domain.Employee;
import com.xcc.common.R;
import com.xcc.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author xcc
 * @version 1.0
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

    @Override
    public R<Employee> loginCheck(HttpServletRequest request, Employee employee) {
        String username = employee.getUsername();
        String password = employee.getPassword();
        //将密码md5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据用户名查一个对象
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername,username);
        Employee emp = employeeDao.selectOne(wrapper);

        //密码对比
        if (!(password.equals(emp.getPassword()))){
            return R.error("账号或者密码错误!");
        }

        //查看员工状态
        if (emp.getStatus()!=1){
            return R.error("该账号已锁定!");
        }

        //成功 存入session
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @Override
    public R<String> loginOut(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出成功!");
    }

    @Override
    public R<String> save(HttpServletRequest request,Employee employee) {
        //设置初始密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        //创建时间
//        employee.setCreateTime(LocalDateTime.now());
//        //最后更新时间
//        employee.setUpdateTime(LocalDateTime.now());
//        //创建人(ID)
//        Long userId = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(userId);
//        employee.setUpdateUser(userId);

        int insert = employeeDao.insert(employee);
        if (insert>0){
        return R.success("新增员工成功");
        }else {
            return R.error("新增员工失败");
        }
    }

    @Override
    public R getPage(int page, int pageSize, String name) {

        Page<Employee> employeePage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();

        wrapper.like(!StringUtils.isNullOrEmpty(name),Employee::getName,name);

        wrapper.orderByDesc(Employee::getUpdateTime);//排序条件

        Page<Employee> selectPage = employeeDao.selectPage(employeePage, wrapper);
        return R.success(selectPage);
    }

    @Override
    public R update(HttpServletRequest request, Employee employee) {

//        Long Id = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateUser(Id);
//        employee.setUpdateTime(LocalDateTime.now());

        LambdaUpdateWrapper<Employee> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Employee::getId,employee.getId());

        int update = employeeDao.update(employee, updateWrapper);

        if (update>0){
            return R.success("修改成功");
        }else {
        return R.error("修改失败");
        }
    }

    public Employee getById(Long id){

        LambdaUpdateWrapper<Employee> Wrapper = new LambdaUpdateWrapper<>();

        Wrapper.eq(Employee::getId,id);
        Employee employee = employeeDao.selectOne(Wrapper);

        return employee;
    }
}
