package com.peecho.orderbackend.controller;

import com.peecho.orderbackend.service.OrderService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class OrderTestConfig {

    @Bean
    OrderService orderService() {
        return mock(OrderService.class);
    }

}
