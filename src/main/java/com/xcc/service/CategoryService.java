package com.xcc.service;

import com.xcc.common.R;
import com.xcc.domain.Category;
import com.xcc.domain.Employee;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xcc
 * @version 1.0
 */


public interface CategoryService {

    R<String> save(Category category);

    R getPage(int page, int pageSize);

    R update(Category category);

    R delete(Long id);

    R getList(Category category);

}
