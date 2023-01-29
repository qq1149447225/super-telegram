package com.xcc.controller;

import com.xcc.common.R;
import com.xcc.domain.AddressBook;
import com.xcc.service.addressBookService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xcc
 * @version 1.0
 */

@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private addressBookService service;

    @PostMapping
    public R addressBook(@RequestBody AddressBook addressBook) {
        return service.add(addressBook);
    }

    @GetMapping("/default")
    public R getDefaultAddress(){
        return service.getDefaultAddress();
    }

    @GetMapping("/list")
    public R getList(){
        return service.getList();
    }

    @PutMapping("/default")
     public R setDefault(@RequestBody AddressBook addressBook){
        return service.setDefault(addressBook);
    }
}
