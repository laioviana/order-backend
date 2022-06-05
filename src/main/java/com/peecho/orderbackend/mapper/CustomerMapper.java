package com.peecho.orderbackend.mapper;

import com.peecho.orderbackend.model.Customer;
import com.peecho.orderbackend.request.CustomerRequest;

public class CustomerMapper {

    public static Customer fromDto(CustomerRequest customerRequest) {
        Customer customer = new Customer();
        customer.setFirstName(customerRequest.firstName());
        customer.setLastName(customerRequest.lastName());
        customer.setEmail(customerRequest.email());
        customer.setZipCode(customerRequest.zipCode());
        customer.setAddressLine(customerRequest.addressLine());
        customer.setCountry(customerRequest.country());
        customer.setCity(customerRequest.city());
        return customer;
    }
}
