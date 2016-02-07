package com.hlprmnky.widgco.common.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hlprmnky.widgco.common.representations.Widget;

import java.util.List;

public class ValidateWidgetResponse {
    private List<Widget> widgetsFound;
    private List<String> widgetsRequested;

    public ValidateWidgetResponse() {}
    public ValidateWidgetResponse(List<Widget> widgetsFound, List<String> widgetsRequested) {
        this.widgetsFound = widgetsFound;
        this.widgetsRequested = widgetsRequested;
    }

    @JsonProperty
    public List<Widget> getWidgetsFound() {
        return this.widgetsFound;
    }

    @JsonProperty
    public void setWidgetsFound(List<Widget> widgetsFound) { this.widgetsFound = widgetsFound; }

    @JsonProperty
    public List<String> getWidgetsRequested() {
        return widgetsRequested;
    }

    @JsonProperty
    public void setWidgetsRequested(List<String> widgetsRequested) {
        this.widgetsRequested = widgetsRequested;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof ValidateWidgetResponse)) {
            return false;
        }
        ValidateWidgetResponse o = (ValidateWidgetResponse)other;
        return (o.getWidgetsFound().containsAll(this.getWidgetsFound())) &&
                this.getWidgetsFound().containsAll(o.getWidgetsFound()) &&
                o.getWidgetsRequested().containsAll(this.getWidgetsRequested()) &&
                this.getWidgetsRequested().containsAll(o.getWidgetsRequested());
    }
}
