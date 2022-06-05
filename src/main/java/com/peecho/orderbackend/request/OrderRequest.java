package com.peecho.orderbackend.request;

import com.peecho.orderbackend.model.Order;

public record OrderRequest(Order.ProductType productType, String description, Integer customer) {
}
