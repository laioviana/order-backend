package com.peecho.orderbackend.controller;

import com.peecho.orderbackend.model.Customer;
import com.peecho.orderbackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    ResponseEntity<List<Customer>> listAllCustomers() {
        log.info("Listing all customers.");
        return customerService.listAllCustomers()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Customer createCustomer(@Valid @RequestBody Customer customer) {
        log.info("Creating customer for email {}.", customer.getEmail());
        return customerService.createCustomer(customer);
    }

}
