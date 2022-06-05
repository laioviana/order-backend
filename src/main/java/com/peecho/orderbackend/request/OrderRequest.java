package com.peecho.orderbackend.request;

public record OrderRequest(Integer productType, String description, Integer customer) {
}
