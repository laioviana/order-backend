package com.peecho.orderbackend.util;

import com.peecho.orderbackend.model.Customer;
import com.peecho.orderbackend.model.Order;
import com.peecho.orderbackend.request.OrderRequest;

import java.time.Instant;

public class CreationUtils {

    private CreationUtils(){
    }


    public static Customer createCustomer(Integer id, String fistName, String lastName, String email, String city, String zipCode, String addressLine, String country){
        Customer customer = new Customer();
        customer.setId(id);
        customer.setFirstName(fistName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setCity(city);
        customer.setZipCode(zipCode);
        customer.setAddressLine(addressLine);
        customer.setCountry(country);
        customer.setCreatedAt(Instant.parse("2022-06-04T15:30:45.123Z"));
        return customer;
    }

    public static Order createOrder(Long id, Integer productType, String description, Customer customer, Order.OrderStatus orderStatus) {
        Order order = new Order();
        order.setId(id);
        order.setStatus(orderStatus);
        order.setCustomer(customer);
        order.setDescription(description);
        order.setProductType(productType);
        order.setCreatedAt(Instant.parse("2022-06-04T15:30:45.123Z"));
        return order;
    }

    public static OrderRequest createOrderRequest(Integer productType, String description, Integer customer) {
        return new OrderRequest(productType, description, customer);
    }
}
