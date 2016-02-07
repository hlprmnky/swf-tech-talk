package com.hlprmnky.widgco.deciders.statemachine.states;

import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.hlprmnky.widgco.deciders.statemachine.State;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Failed extends State {
    Logger logger = LoggerFactory.getLogger(Failed.class);

    @Override
    public STATE_TYPE getType() {
        return STATE_TYPE.FAILED;
    }

    @Override
    public State process(HistoryEvent event) {
        logger.debug("Processing event {}", ReflectionToStringBuilder.toString(event));
        return this.withEvent(event);
    }
}
