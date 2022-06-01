package com.peecho.orderbackend.service;

import com.peecho.orderbackend.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {

    Order createOrder(Order order);
    Page<Order> listAllOrders(Pageable pageable);
    Optional<Order> updateOrderStatus(Long orderId, Integer status);
}
