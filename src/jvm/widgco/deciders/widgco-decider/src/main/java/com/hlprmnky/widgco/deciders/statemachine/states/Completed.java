package com.hlprmnky.widgco.deciders.statemachine.states;

import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.hlprmnky.widgco.deciders.statemachine.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Completed extends State {

    Logger logger = LoggerFactory.getLogger(Completed.class);

    @Override
    public STATE_TYPE getType() {
        return STATE_TYPE.COMPLETED;
    }

    @Override
    public State process(HistoryEvent event) {
        logger.debug("Completed state processing change from {} to {}", this.getEvent(), event);
        return this.withEvent(event);
    }
}
