package com.example.actions.service;

import com.example.actions.entities.ActionForCustomer;
import com.example.actions.repository.ActionForCustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActionsService {
    final private ActionForCustomerRepository repository;

    @Autowired
    public ActionsService(ActionForCustomerRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<ActionForCustomer> findActions(String customerId, LocalDateTime forDate) {
        LocalDateTime startDate = forDate.toLocalDate().atStartOfDay();
        LocalDateTime endDate = forDate.toLocalDate().atTime(23, 59, 59);
        return repository.findByCustomerIdAndActOnBetween(customerId, startDate, endDate);
    }

    @Transactional
    public void save(ActionForCustomer action) {
        repository.save(action);
    }

    @Transactional
    public void cleanUp(LocalDateTime forDate) {
        LocalDateTime startDate = forDate.toLocalDate().atStartOfDay();
        LocalDateTime endDate = forDate.toLocalDate().atTime(23, 59, 59);
        repository.deleteByActOnBetween(startDate, endDate);
    }
}
