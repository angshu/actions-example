package com.example.actions.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "actions_for_customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionForCustomer {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "act_on")
    @JsonFormat(pattern = "dd-M-yyyy HH:mm")
    private LocalDateTime actOn;
}
