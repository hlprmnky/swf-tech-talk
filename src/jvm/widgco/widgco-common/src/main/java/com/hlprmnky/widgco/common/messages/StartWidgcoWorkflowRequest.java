package com.hlprmnky.widgco.common.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class StartWidgcoWorkflowRequest {
    private Map<String, Integer> widgetsAndQuantities;

    public StartWidgcoWorkflowRequest() {}
    public StartWidgcoWorkflowRequest(Map<String, Integer> widgetsAndQuantities) {
        this.widgetsAndQuantities = widgetsAndQuantities;
    }

    @JsonProperty
    public Map<String, Integer> getWidgetsAndQuantities() {
        return widgetsAndQuantities;
    }

    @JsonProperty
    public void setWidgetsAndQuantities(Map<String, Integer> widgetsAndQuantities) {
        this.widgetsAndQuantities = widgetsAndQuantities;
    }
}
