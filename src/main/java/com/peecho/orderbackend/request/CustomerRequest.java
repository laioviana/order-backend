package com.peecho.orderbackend.request;

import lombok.NonNull;

public record CustomerRequest(@NonNull String firstName, @NonNull String lastName, @NonNull String email, @NonNull String country, @NonNull String zipCode, @NonNull String city, @NonNull String addressLine) {
}
