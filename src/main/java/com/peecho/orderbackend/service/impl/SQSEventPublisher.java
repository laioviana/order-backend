package com.peecho.orderbackend.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Log4j2
public class SQSEventPublisher {

    @Autowired
    private AmazonSQS amazonSQS;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    public void publishEvent(JsonNode message, Long orderId) {
        log.info("Generating event : {}", message);
        SendMessageRequest sendMessageRequest = null;
        try {
            sendMessageRequest = new SendMessageRequest().withQueueUrl("http://localhost:4566/000000000000/order-queue.fifo")
                    .withMessageBody(objectMapper.writeValueAsString(message))
                    .withMessageGroupId("Message")
                    .withMessageDeduplicationId(UUID.randomUUID().toString());
            amazonSQS.sendMessage(sendMessageRequest);
            log.info("Event has been published in SQS.");
            orderService.updateOrderStatus(orderId, Order.OrderStatus.IN_PRINT_QUEUE);
        } catch (JsonProcessingException e) {
            log.error("JsonProcessingException e : {} and stacktrace : {}", e.getMessage(), e);
            orderService.updateOrderStatus(orderId, Order.OrderStatus.ERROR);
        } catch (Exception e) {
            log.error("Exception ocurred while pushing event to sqs : {} and stacktrace ; {}", e.getMessage(), e);
            orderService.updateOrderStatus(orderId, Order.OrderStatus.ERROR);
        }
    }
}