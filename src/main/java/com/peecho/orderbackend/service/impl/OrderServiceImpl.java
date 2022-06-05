package com.peecho.orderbackend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peecho.orderbackend.mapper.OrderMapper;
import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.repository.OrderRepository;
import com.peecho.orderbackend.request.OrderRequest;
import com.peecho.orderbackend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private SQSEventPublisher sqsEventPublisher;

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        Order newOrder = OrderMapper.fromDto(orderRequest);
        return orderRepository.save(newOrder);
    }

    @Override
    public List<Order> listAllOrders(Integer page, Integer size) {
        return orderRepository.findAll(PageRequest.of(page,size)).stream().toList();
    }

    @Override
    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Optional<Order> sendOrderToQueue(Long orderId) {
        return orderRepository.findById(orderId)
                    .filter(order -> order.getStatus() == Order.OrderStatus.PAID)
                    .map(order -> {
                        ObjectMapper mapper = new ObjectMapper();
                        sqsEventPublisher.publishEvent(mapper.valueToTree(order), orderId);
                        return order;
                    });
    }

    @Override
    public Optional<Order> payOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getStatus() == Order.OrderStatus.OPEN)
                .flatMap(order -> updateOrderStatus(orderId, Order.OrderStatus.PAID));
    }

    @Override
    public Optional<Order> cancelOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getStatus() == Order.OrderStatus.PAID)
                .flatMap(order -> updateOrderStatus(orderId, Order.OrderStatus.CANCELED));
    }

    public Optional<Order> updateOrderStatus(Long orderId, Order.OrderStatus status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                });
    }

}
