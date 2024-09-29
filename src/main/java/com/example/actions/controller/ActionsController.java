package com.example.actions.controller;

import com.example.actions.Constants;
import com.example.actions.entities.ActionForCustomer;
import com.example.actions.service.ActionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/actions")
public class ActionsController {

    final private ActionsService service;
    final private KafkaTemplate kafkaTemplate;

    final private Logger logger = LoggerFactory.getLogger(ActionsController.class);

    @Autowired
    public ActionsController(ActionsService service, KafkaTemplate kafkaTemplate) {
        this.service = service;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void accept(@RequestBody ActionForCustomer action) {
        logger.info("Publishing action onto kafka topic");
        this.kafkaTemplate.send(Constants.ACTIONS_TOPIC, action);
        //service.save(action);
    }

    @GetMapping
    @ResponseBody
    public List<ActionForCustomer> get(@RequestParam String customerId,
               @RequestParam @DateTimeFormat(pattern = "dd-M-yyyy HH:mm") LocalDateTime forDate) {
        return service.findActions(customerId, forDate);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleArgumentMismatch(MethodArgumentTypeMismatchException e) {
        logger.error("Invalid request params: ", e.getMessage());
        return new ResponseEntity<>("Invalid request params", HttpStatus.BAD_REQUEST);
    }


}
