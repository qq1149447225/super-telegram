package com.xcc.service;

import com.xcc.common.R;
import com.xcc.domain.Orders;

/**
 * @author xcc
 * @version 1.0
 */


public interface OrderService {

    R submit(Orders orders);

    R getPage(int page,int pageSize);
}
