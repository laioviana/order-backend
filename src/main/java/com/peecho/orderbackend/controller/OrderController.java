package com.peecho.orderbackend.controller;

import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.request.OrderRequest;
import com.peecho.orderbackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
        log.info("Creating order with product {} for userId {}.", orderRequest.productType(), orderRequest.customer());
        return orderService.createOrder(orderRequest);
    }

    @PutMapping("/pay/{orderId}")
    public ResponseEntity payOrder(@PathVariable Long orderId) {
        log.info("Paying order {}.", orderId);
        return orderService.payOrder(orderId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity("Order can't be paid with this status", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity cancelOrder(@PathVariable Long orderId) {
        log.info("Canceling order {}.", orderId);
        return orderService.cancelOrder(orderId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity("Order can't be canceled with this status", HttpStatus.BAD_REQUEST));
    }

    @PutMapping("/sendToQueue/{orderId}")
    public ResponseEntity sendOrderToQueue(@PathVariable Long orderId) {
        log.info("Sending order {} to queue.", orderId);
        return orderService.sendOrderToQueue(orderId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity("Order can't be sent to queue with this status", HttpStatus.BAD_REQUEST));
    }
}
