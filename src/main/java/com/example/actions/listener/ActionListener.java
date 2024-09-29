package com.example.actions.listener;

import com.example.actions.Constants;
import com.example.actions.entities.ActionForCustomer;
import com.example.actions.service.ActionsService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class ActionListener {

    Logger logger = LoggerFactory.getLogger(ActionListener.class);
    final private ActionsService service;

    @Autowired
    public ActionListener(ActionsService service) {
        this.service = service;
    }

    @KafkaListener(topics = Constants.ACTIONS_TOPIC)
    public void listener(@Payload ActionForCustomer action, ConsumerRecord<String, ActionForCustomer> cr) {
        logger.info("Topic {}: Received message {} for {} Customer ", Constants.ACTIONS_TOPIC, action.getTitle(), action.getCustomerId());
        logger.info(cr.toString());
        service.save(action);
    }
}
