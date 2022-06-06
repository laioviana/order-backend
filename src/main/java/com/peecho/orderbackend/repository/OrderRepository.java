package com.peecho.orderbackend.repository;

import com.peecho.orderbackend.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByOrderByIdDesc(Pageable pageable);
}
