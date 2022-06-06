package com.peecho.orderbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peecho.orderbackend.model.Customer;
import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.service.OrderService;
import com.peecho.orderbackend.util.CreationUtils;
import com.peecho.orderbackend.util.FileReadUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@WebMvcTest(OrderController.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Test
    void getAllOrdersOkTest() throws Exception {
        final Customer customer = CreationUtils.createCustomer(1, "Jonh", "Doe", "johndoe@peecho.com", "Amsterdam", "1010AB", "John Doe Straat", "The Netherlands");
        Page<Order> page = new PageImpl<>(List.of(
                CreationUtils.createOrder(1L, Order.ProductType.ARTBOOK, "Integration test 1", customer, Order.OrderStatus.OPEN),
                CreationUtils.createOrder(2L, Order.ProductType.POSTER, "Integration test 2", customer, Order.OrderStatus.OPEN)
        ));
        when(orderService.listAllOrders(0,10))
                .thenReturn(page);

        mockMvc.perform(get("/order?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().json(FileReadUtils.readFile("order_list_ok.json")));
    }

    @Test
    void orderHappyPath() throws Exception {
        final Customer customerToBeReturned = CreationUtils.createCustomer(1, "Jonh", "Doe", "johndoe@peecho.com", "Amsterdam", "1010AB", "John Doe Straat", "The Netherlands");
        Order orderToBeReturned = CreationUtils.createOrder(1L, Order.ProductType.ARTBOOK, "Integration test 1", customerToBeReturned, Order.OrderStatus.OPEN);

        when(orderService.createOrder(any()))
                .thenReturn(orderToBeReturned);

        ObjectMapper objectMapper = new ObjectMapper();
        String orderRequestjson = objectMapper.writeValueAsString(CreationUtils.createOrderRequest(Order.ProductType.ARTBOOK, "Integration test 1", 1));
        mockMvc.perform(post("/order")
                        .content(orderRequestjson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(FileReadUtils.readFile("order_1.json")));

        orderToBeReturned.setStatus(Order.OrderStatus.PAID);
        when(orderService.payOrder(1L)).thenReturn(Optional.of(orderToBeReturned));
        mockMvc.perform(put("/order/pay/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(FileReadUtils.readFile("order_1_paid.json")));

        orderToBeReturned.setStatus(Order.OrderStatus.IN_PRINT_QUEUE);
        when(orderService.sendOrderToQueue(1L)).thenReturn(Optional.of(orderToBeReturned));
        mockMvc.perform(put("/order/sendToQueue/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(FileReadUtils.readFile("order_1_queue.json")));
    }

    @Test
    void orderCancelAfterPayPath() throws Exception {
        final Customer customerToBeReturned = CreationUtils.createCustomer(1, "Jonh", "Doe", "johndoe@peecho.com", "Amsterdam", "1010AB", "John Doe Straat", "The Netherlands");
        Order orderToBeReturned = CreationUtils.createOrder(1L, Order.ProductType.ARTBOOK, "Integration test 1", customerToBeReturned, Order.OrderStatus.OPEN);

        when(orderService.createOrder(any()))
                .thenReturn(orderToBeReturned);

        ObjectMapper objectMapper = new ObjectMapper();
        String orderRequestjson = objectMapper.writeValueAsString(CreationUtils.createOrderRequest(Order.ProductType.ARTBOOK, "Integration test 1", 1));
        mockMvc.perform(post("/order")
                        .content(orderRequestjson)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(FileReadUtils.readFile("order_1.json")));

        orderToBeReturned.setStatus(Order.OrderStatus.PAID);
        when(orderService.payOrder(1L)).thenReturn(Optional.of(orderToBeReturned));
        mockMvc.perform(put("/order/pay/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(FileReadUtils.readFile("order_1_paid.json")));

        orderToBeReturned.setStatus(Order.OrderStatus.CANCELED);
        when(orderService.cancelOrder(1L)).thenReturn(Optional.of(orderToBeReturned));
        mockMvc.perform(put("/order/cancel/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(FileReadUtils.readFile("order_1_canceled.json")));

        when(orderService.sendOrderToQueue(1L)).thenReturn(Optional.empty());
        mockMvc.perform(put("/order/sendToQueue/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
