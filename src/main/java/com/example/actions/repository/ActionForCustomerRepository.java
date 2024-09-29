package com.example.actions.repository;

import com.example.actions.entities.ActionForCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ActionForCustomerRepository extends JpaRepository<ActionForCustomer, Long> {
    List<ActionForCustomer> findByCustomerIdAndActOnBetween(String customerId, LocalDateTime startDate, LocalDateTime endDate);

    void deleteByActOnBetween(LocalDateTime startDate, LocalDateTime endDate);
}
