package com.peecho.orderbackend.request;

import com.peecho.orderbackend.model.Order;
import lombok.NonNull;

public record OrderRequest(@NonNull Order.ProductType productType, @NonNull String description, @NonNull Integer customer) {
}
