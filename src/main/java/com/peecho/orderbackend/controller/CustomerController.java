package com.peecho.orderbackend.controller;

import com.peecho.orderbackend.model.Customer;
import com.peecho.orderbackend.request.CustomerRequest;
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
@CrossOrigin
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
    ResponseEntity createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        log.info("Creating customer for email {}.", customerRequest.email());
        return customerService.createCustomer(customerRequest)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity("E-mail "+customerRequest.email()+ " already in use!", HttpStatus.BAD_REQUEST));
    }

}
