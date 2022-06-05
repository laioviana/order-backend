package com.peecho.orderbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "`order`")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;

    @Column(name = "product_type", nullable = false)
    private Integer productType;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer", insertable = false, updatable = false)
    private Customer customer;

    @JsonIgnore
    @Column(name = "customer")
    private Integer customerId;

    @JsonIgnore
    @Column(name = "created_at")
    private Instant createdAt;

    public enum OrderStatus {
        OPEN,
        PAID,
        IN_PRINT_QUEUE,
        CANCELED,
        ERROR,
        COMPLETE
    }
}