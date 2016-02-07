package com.hlprmnky.widgco.deciders.statemachine.states;

import com.amazonaws.services.simpleworkflow.model.EventType;
import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.hlprmnky.widgco.common.messages.StartWidgcoWorkflowRequest;
import com.hlprmnky.widgco.common.messages.ValidateWidgetRequest;
import com.hlprmnky.widgco.deciders.statemachine.State;
import io.dropwizard.jackson.Jackson;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

public class WorkflowStart extends State {
    Logger logger = LoggerFactory.getLogger(WorkflowStart.class);

    @Override
    public STATE_TYPE getType() {
        return STATE_TYPE.START;
    }

    @Override
    public State process(HistoryEvent event) {
        if(event.getEventType().equals(EventType.WorkflowExecutionStarted.toString())) {
            String context = event.getWorkflowExecutionStartedEventAttributes().getInput();
            logger.info("Starting workflow processing for message {}", context);
            try {
                StartWidgcoWorkflowRequest request = Jackson.newObjectMapper().readValue(context, StartWidgcoWorkflowRequest.class);
                ValidateWidgetRequest widgetRequest = new ValidateWidgetRequest(new ArrayList<>());
                request.getWidgetsAndQuantities().keySet().forEach(name -> widgetRequest.getWidgetNames().add(name));
                return new ValidateWidgets()
                        .withQuantitiesMap(request.getWidgetsAndQuantities())
                        .withRequest(widgetRequest)
                        .withEvent(event);
            } catch(IOException e) {
                logger.error("Unable to deserialize event input [{}]", context, e);
                return new Failed().withEvent(event);
            }
        } else {
            logger.info("Received unlooked-for event of type {}", ReflectionToStringBuilder.toString(event));
            return this;
        }
    }
}
