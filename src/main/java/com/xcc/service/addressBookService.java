package com.xcc.service;

import com.xcc.common.R;
import com.xcc.domain.AddressBook;

/**
 * @author xcc
 * @version 1.0
 */


public interface addressBookService {
    R add(AddressBook addressBook);

    R getDefaultAddress();

    R getList();
    R setDefault(AddressBook addressBook);


}
