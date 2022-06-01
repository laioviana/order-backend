package com.peecho.orderbackend.service.impl;

import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.repository.OrderRepository;
import com.peecho.orderbackend.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Page<Order> listAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Optional<Order> updateOrderStatus(Long orderId, Integer status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(1);
                    return orderRepository.save(order);
                });
    }
}
