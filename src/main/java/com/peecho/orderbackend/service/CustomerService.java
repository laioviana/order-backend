package com.peecho.orderbackend.service;

import com.peecho.orderbackend.model.Customer;
import com.peecho.orderbackend.request.CustomerRequest;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer createCustomer(CustomerRequest customerRequest);

    Optional<List<Customer>> listAllCustomers();

}
