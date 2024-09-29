package com.example.actions.scheduler;

import com.example.actions.service.ActionsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ActionArchiverJob {
    public static final int ACTIONS_PURGE_DAYS_OLDER = 1;
    final private Logger logger = LoggerFactory.getLogger(ActionArchiverJob.class);
    final private ActionsService service;

    @Autowired
    public ActionArchiverJob(ActionsService service) {
        this.service = service;
    }

    //5 mins interval for test
    @Scheduled(fixedRate = 300000)
    public void cleanUpActions() {
        logger.info("Running clean up job. Will look for actions yesterday or earlier and remove");
        service.cleanUp(LocalDateTime.now().minusDays(ACTIONS_PURGE_DAYS_OLDER));
    }
}
