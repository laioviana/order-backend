package com.peecho.orderbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peecho.orderbackend.model.Customer;
import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.service.OrderService;
import com.peecho.orderbackend.util.CreationUtils;
import com.peecho.orderbackend.util.FileReadUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Import(OrderTestConfig.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Test
    void getAllOrdersOkTest() throws Exception {
        final Customer customer = CreationUtils.createCustomer(1, "Jonh", "Doe", "johndoe@peecho.com", "Amsterdam", "1010AB", "John Doe Straat", "The Netherlands");
        when(orderService.listAllOrders(0,10))
                .thenReturn(List.of(
                        CreationUtils.createOrder(1L, 1, "Integration test 1", customer, Order.OrderStatus.OPEN),
                        CreationUtils.createOrder(2L, 2, "Integration test 2", customer, Order.OrderStatus.OPEN)
                ));

        mockMvc.perform(get("/order?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().json(FileReadUtils.readFile("order_list_ok.json")));
    }

    @Test
    void orderHappyPath() throws Exception {
        final Customer customer = CreationUtils.createCustomer(1, "Jonh", "Doe", "johndoe@peecho.com", "Amsterdam", "1010AB", "John Doe Straat", "The Netherlands");
        Order order = CreationUtils.createOrder(1L, 1, "Integration test 1", customer, Order.OrderStatus.OPEN);
        when(orderService.createOrder(any()))
                .thenReturn(order);

        ObjectMapper objectMapper = new ObjectMapper();
        String orderRequestjson = objectMapper.writeValueAsString(CreationUtils.createOrderRequest(1, "Integration test 1", 1));
        mockMvc.perform(post("/order")
                        .content(orderRequestjson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(FileReadUtils.readFile("order_1.json")));

        order.setStatus(Order.OrderStatus.PAID);
        when(orderService.payOrder(1L)).thenReturn(Optional.of(order));
        mockMvc.perform(put("/order/pay/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(FileReadUtils.readFile("order_1_paid.json")));

        order.setStatus(Order.OrderStatus.IN_PRINT_QUEUE);
        when(orderService.sendOrderToQueue(1L)).thenReturn(Optional.of(order));
        mockMvc.perform(put("/order/sendToQueue/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(FileReadUtils.readFile("order_1_queue.json")));
    }

}