package com.crm.workbench.service.impl;

import com.crm.utils.SqlSessionUtil;
import com.crm.workbench.dao.customerDao.CustomerDao;
import com.crm.workbench.domain.customer.Customer;
import com.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<Customer> getCustomersByLike(String name) {
        List<Customer> customers=customerDao.getCustomersByLike(name);
        return customers;
    }
}
