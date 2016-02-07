package com.hlprmnky.widgco.common.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ValidateWidgetRequest {

    private List<String> widgetNames;

    public ValidateWidgetRequest() {}
    public ValidateWidgetRequest(List<String> widgetNames) {
        this.widgetNames = widgetNames;
    }

    @JsonProperty
    public List<String> getWidgetNames() {
        return this.widgetNames;
    }

    @JsonProperty
    public void setWidgetNames(List<String> widgetNames) { this.widgetNames = widgetNames; }
}
