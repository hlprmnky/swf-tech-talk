package com.hlprmnky.widgco.deciders.statemachine.states;

import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.hlprmnky.widgco.deciders.statemachine.State;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateInventory extends State {
    Logger logger = LoggerFactory.getLogger(ValidateInventory.class);

    @Override
    public STATE_TYPE getType() {
        return STATE_TYPE.VALIDATE_INVENTORY;
    }

    @Override
    public State process(HistoryEvent event) {
        logger.debug("End of the line for now with event [{}]", ReflectionToStringBuilder.toString(event));
        return new Completed().withEvent(event);
    }
}
