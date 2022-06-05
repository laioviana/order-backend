package com.peecho.orderbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peecho.orderbackend.model.Customer;
import com.peecho.orderbackend.service.CustomerService;
import com.peecho.orderbackend.util.CreationUtils;
import com.peecho.orderbackend.util.FileReadUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;

    @Test
    void getAllCustomersOkTest() throws Exception {
        final Customer customer = CreationUtils.createCustomer(1, "Jonh", "Doe", "johndoe@peecho.com", "Amsterdam", "1010AB", "John Doe Straat", "The Netherlands");
        final Customer customer2 = CreationUtils.createCustomer(2, "Mary", "Doe", "marydoe@peecho.com", "Rotterdam", "3030AB", "Mary Doe Straat", "The Netherlands");
        when(customerService.listAllCustomers())
                .thenReturn(Optional.of(List.of(customer,customer2)));

        mockMvc.perform(get("/customer"))
                .andExpect(status().isOk())
                .andExpect(content().json(FileReadUtils.readFile("customer_list_ok.json")));
    }

    @Test
    void getAllCustomerNoContentTest() throws Exception {
        when(customerService.listAllCustomers())
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/customer"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer() throws Exception {
        final Customer customerToBeReturned = CreationUtils.createCustomer(1, "Jonh", "Doe", "johndoe@peecho.com", "Amsterdam", "1010AB", "John Doe Straat", "The Netherlands");
        when(customerService.createCustomer(any()))
                .thenReturn(customerToBeReturned);

        ObjectMapper objectMapper = new ObjectMapper();
        String orderRequestjson = objectMapper.writeValueAsString(CreationUtils.createCustomerRequest("Jonh", "Doe", "johndoe@peecho.com", "The Netherlands", "1010AB", "Amsterdam", "John Doe Straat"));
        mockMvc.perform(post("/customer")
                        .content(orderRequestjson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(FileReadUtils.readFile("customer_1.json")));
    }


}
