package com.peecho.orderbackend.service.impl;

import com.peecho.orderbackend.mapper.CustomerMapper;
import com.peecho.orderbackend.model.Customer;
import com.peecho.orderbackend.repository.CustomerRepository;
import com.peecho.orderbackend.request.CustomerRequest;
import com.peecho.orderbackend.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(CustomerRequest customerRequest) {
        Customer newCustomer = CustomerMapper.fromDto(customerRequest);
        return customerRepository.save(newCustomer);
    }

    @Override
    public Optional<List<Customer>> listAllCustomers() {
        return Optional.of(customerRepository.findAll());
    }
}
