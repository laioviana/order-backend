package com.peecho.orderbackend.controller;

import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.request.OrderRequest;
import com.peecho.orderbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> listAllOrders(@RequestParam(name = "page", defaultValue = "1") int page,
                                     @RequestParam(name = "size", defaultValue = "10") int size) {
        log.info("Listing all orders.");
        return orderService.listAllOrders(page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @PutMapping("/pay/{orderId}")
    public ResponseEntity<Order> payOrder(@PathVariable Long orderId) {
        return orderService.payOrder(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/sendToQueue/{orderId}")
    public ResponseEntity<Order> sendOrderToQueue(@PathVariable Long orderId) {
        return orderService.sendOrderToQueue(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
