package com.peecho.orderbackend.service;

import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.request.OrderRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order createOrder(OrderRequest orderRequest);

    Page<Order> listAllOrders(Integer page, Integer size);

    Optional<Order> findOrderById(Long orderId);

    Optional<Order> payOrder(Long orderId);

    Optional<Order> sendOrderToQueue(Long orderId);

    Optional<Order> cancelOrder(Long orderId);

}
