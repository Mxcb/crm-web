package com.crm.workbench.dao.customerDao;

import com.crm.workbench.domain.customer.Customer;

import java.util.List;

public interface CustomerDao {

    Customer selectByName(String company);

    int add(Customer customer);

    List<Customer> getCustomersByLike(String name);

    int insert(Customer cus);
}
