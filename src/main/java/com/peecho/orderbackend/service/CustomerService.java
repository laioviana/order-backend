package com.peecho.orderbackend.service;

import com.peecho.orderbackend.model.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer createCustomer(Customer customer);

    Optional<List<Customer>> listAllCustomers();

}
