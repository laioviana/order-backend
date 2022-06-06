package com.peecho.orderbackend.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peecho.orderbackend.mapper.OrderMapper;
import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.repository.OrderRepository;
import com.peecho.orderbackend.request.OrderRequest;
import com.peecho.orderbackend.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private ObjectMapper objectMapper;

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
    public Page<Order> listAllOrders(Integer page, Integer size) {
        return orderRepository.findAllByOrderByIdDesc(PageRequest.of(page,size));
    }

    @Override
    public Optional<Order> findOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Optional<Order> payOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getStatus() == Order.OrderStatus.OPEN)
                .flatMap(order -> updateOrderStatus(orderId, Order.OrderStatus.PAID));
    }

    @Override
    public Optional<Order> sendOrderToQueue(Long orderId) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getStatus() == Order.OrderStatus.PAID)
                .map(order -> {
                    log.info("Publishing order {} to queue.",orderId);
                    ObjectMapper mapper = new ObjectMapper();
                    publishEvent(mapper.valueToTree(order), orderId);
                    return order;
                });
    }

    @Override
    public Optional<Order> cancelOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getStatus() == Order.OrderStatus.PAID)
                .flatMap(order -> updateOrderStatus(orderId, Order.OrderStatus.CANCELED));
    }

    private Optional<Order> updateOrderStatus(Long orderId, Order.OrderStatus status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    log.info("Updating order {} with new status {}.",orderId, status);
                    order.setStatus(status);
                    return orderRepository.save(order);
                });
    }

    private void publishEvent(JsonNode message, Long orderId) {
        log.info("Generating event : {}", message);
        SendMessageRequest sendMessageRequest = null;
        try {
            sendMessageRequest = new SendMessageRequest().withQueueUrl("http://localhost:4566/000000000000/order-queue.fifo")
                    .withMessageBody(objectMapper.writeValueAsString(message))
                    .withMessageGroupId("Message")
                    .withMessageDeduplicationId(UUID.randomUUID().toString());
            amazonSQS.sendMessage(sendMessageRequest);
            log.info("Event has been published in SQS.");
            updateOrderStatus(orderId, Order.OrderStatus.IN_PRINT_QUEUE);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException e : {} and stacktrace : {}", e.getMessage(), e);
            updateOrderStatus(orderId, Order.OrderStatus.ERROR);
        } catch (Exception e) {
            log.error("Exception ocurred while pushing event to sqs : {} and stacktrace ; {}", e.getMessage(), e);
            updateOrderStatus(orderId, Order.OrderStatus.ERROR);
        }
    }

}
