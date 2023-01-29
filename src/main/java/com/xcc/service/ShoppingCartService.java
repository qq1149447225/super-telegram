package com.xcc.service;

import com.xcc.common.R;
import com.xcc.domain.ShoppingCart;

import java.util.List;

/**
 * @author xcc
 * @version 1.0
 */


public interface ShoppingCartService {

    R add(ShoppingCart shoppingCart);

    R<List<ShoppingCart>> getList();

    R sub(ShoppingCart shoppingCart);

    R clean();
}
