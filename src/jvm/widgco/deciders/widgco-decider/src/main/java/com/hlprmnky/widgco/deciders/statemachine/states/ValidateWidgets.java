package com.hlprmnky.widgco.deciders.statemachine.states;

import com.amazonaws.services.simpleworkflow.model.EventType;
import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.amazonaws.util.json.Jackson;
import com.hlprmnky.widgco.common.messages.ValidateWidgetRequest;
import com.hlprmnky.widgco.common.messages.ValidateWidgetResponse;
import com.hlprmnky.widgco.deciders.statemachine.State;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class ValidateWidgets extends State {
    Logger logger = LoggerFactory.getLogger(ValidateWidgets.class);

    private ValidateWidgetRequest request;
    private Map<String, Integer> quantitiesByName;

    @Override
    public STATE_TYPE getType() {
        return STATE_TYPE.VALIDATE_WIDGETS;
    }

    @Override
    public State process(HistoryEvent event) {
        if (event.getEventType().equals(EventType.ActivityTaskCompleted.toString())) {
            logger.debug("Received task completion {}", event);
            String activityResult = event.getActivityTaskCompletedEventAttributes().getResult();
            logger.debug("Task result is [{}]", activityResult);
            try {
                ValidateWidgetResponse widgetResponse = Jackson.getObjectMapper().readValue(activityResult, ValidateWidgetResponse.class);
                if (widgetResponse.getWidgetsRequested().stream().allMatch(name ->
                    widgetResponse.getWidgetsFound().stream().anyMatch(widget -> widget.getName().equals(name)))) {

                    logger.debug("All requested widgets found");
                    // ValidateInventoryRequest validateInventory = new ValidateInventoryRequest();
                    // set validation with Widgets and quantities
                    // return new ValidateInventory().withRequest(validateInventory).andEvent(event);
                    return new ValidateInventory().withEvent(event);
                } else{
                    logger.error("Not all requested widgets could be found: [{}], [{}]",
                            widgetResponse.getWidgetsFound(),
                            widgetResponse.getWidgetsRequested());
                    return new Failed().withEvent(event);
                }
            } catch (IOException e) {
                logger.error("Failed to deserialize task result", e);
                return new Failed().withEvent(event);
            }
        }
        logger.error("Found unlooked-for event type {}, bouncing", event.getEventType());
        return this;
    }

    public ValidateWidgets withRequest(ValidateWidgetRequest request) {
        this.setRequest(request);
        return this;
    }

    public ValidateWidgets withQuantitiesMap(Map<String, Integer> quantitiesByName) {
        this.setQuantitiesByName(quantitiesByName);
        return this;
    }

    public ValidateWidgetRequest getRequest() {
        return request;
    }

    public void setRequest(ValidateWidgetRequest request) {
        this.request = request;
    }

    public Map<String, Integer> getQuantitiesByName() {
        return quantitiesByName;
    }

    public void setQuantitiesByName(Map<String, Integer> quantitiesByName) {
        this.quantitiesByName = quantitiesByName;
    }
}
