package com.peecho.orderbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peecho.orderbackend.model.Customer;
import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.repository.OrderRepository;
import com.peecho.orderbackend.request.OrderRequest;
import com.peecho.orderbackend.service.impl.OrderServiceImpl;
import com.peecho.orderbackend.util.CreationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    final Customer customerToBeReturned = CreationUtils.createCustomer(1, "Jonh", "Doe", "johndoe@peecho.com", "Amsterdam", "1010AB", "John Doe Straat", "The Netherlands");

    @Test
    void createOrder() {
        com.peecho.orderbackend.model.Order order = CreationUtils.createOrder(1L, 1, "Integration test 1", customerToBeReturned, Order.OrderStatus.OPEN);
        OrderRequest orderRequest = CreationUtils.createOrderRequest(1, "Integration test 1", 1);
        when(orderRepository.save(any())).thenReturn(order);
        final Order result = orderService.createOrder(orderRequest);
        assertEquals(order.getDescription(), result.getDescription());
        assertEquals(order.getStatus(), result.getStatus());
        assertEquals(order.getCustomer().getEmail(), result.getCustomer().getEmail());
    }

    @Test
    void payOrder() {
        com.peecho.orderbackend.model.Order order = CreationUtils.createOrder(1L, 1, "Integration test 1", customerToBeReturned, Order.OrderStatus.OPEN);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        com.peecho.orderbackend.model.Order orderPaid = CreationUtils.createOrder(1L, 1, "Integration test 1", customerToBeReturned, Order.OrderStatus.PAID);
        when(orderRepository.save(any())).thenReturn(orderPaid);
        final Optional<Order> result = orderService.payOrder(1L);
        assertEquals(Order.OrderStatus.PAID, result.get().getStatus());
    }

    @Test
    void cancelOrder() {
        com.peecho.orderbackend.model.Order order = CreationUtils.createOrder(1L, 1, "Integration test 1", customerToBeReturned, Order.OrderStatus.PAID);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        com.peecho.orderbackend.model.Order orderCanceled = CreationUtils.createOrder(1L, 1, "Integration test 1", customerToBeReturned, Order.OrderStatus.CANCELED);
        when(orderRepository.save(any())).thenReturn(orderCanceled);
        final Optional<Order> result = orderService.cancelOrder(1L);
        assertEquals(Order.OrderStatus.CANCELED, result.get().getStatus());
    }

    @Test
    void payOrderError() {
        com.peecho.orderbackend.model.Order order = CreationUtils.createOrder(1L, 1, "Integration test 1", customerToBeReturned, Order.OrderStatus.CANCELED);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        final Optional<Order> result = orderService.payOrder(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void sendOrderToQueueError() {
        com.peecho.orderbackend.model.Order order = CreationUtils.createOrder(1L, 1, "Integration test 1", customerToBeReturned, Order.OrderStatus.PAID);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));
        com.peecho.orderbackend.model.Order orderQueue = CreationUtils.createOrder(1L, 1, "Integration test 1", customerToBeReturned, Order.OrderStatus.IN_PRINT_QUEUE);
        when(orderRepository.save(any())).thenReturn(orderQueue);
        final Optional<Order> result = orderService.sendOrderToQueue(1L);
        assertEquals(Order.OrderStatus.ERROR, result.get().getStatus());
    }

}
