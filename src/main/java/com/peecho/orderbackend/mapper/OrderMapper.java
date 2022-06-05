package com.peecho.orderbackend.mapper;

import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.request.OrderRequest;

public class OrderMapper {

    public static Order fromDto(OrderRequest orderRequest) {
        Order order = new Order();
        order.setProductType(orderRequest.productType());
        order.setDescription(orderRequest.description());
        order.setCustomerId(orderRequest.customer());
        order.setStatus(Order.OrderStatus.OPEN);
        return order;
    }
}
