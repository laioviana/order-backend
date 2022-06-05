package com.peecho.orderbackend.request;

public record CustomerRequest(String firstName, String lastName, String email, String country, String zipCode, String city, String addressLine) {
}
