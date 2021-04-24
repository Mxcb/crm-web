package com.crm.workbench.service;

import com.crm.workbench.domain.customer.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getCustomersByLike(String name);

}
