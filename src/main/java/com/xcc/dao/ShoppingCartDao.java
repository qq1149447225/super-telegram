package com.xcc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xcc.domain.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xcc
 * @version 1.0
 */

@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
}
