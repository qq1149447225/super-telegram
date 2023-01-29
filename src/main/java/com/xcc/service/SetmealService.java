package com.xcc.service;

import com.xcc.common.R;
import com.xcc.domain.Category;
import com.xcc.domain.Employee;
import com.xcc.domain.Setmeal;
import com.xcc.dto.SetmealDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author xcc
 * @version 1.0
 */


public interface SetmealService {

    R<String> save(SetmealDto setmealDto);

    R getPage(int page, int pageSize);
    R update(SetmealDto setmealDto);

    R delete(List<Long> ids);

//    R delete(Long ids);

    R getSetmeals( Long id);

    R getBusinessPackage(SetmealDto setmealDto);
}
