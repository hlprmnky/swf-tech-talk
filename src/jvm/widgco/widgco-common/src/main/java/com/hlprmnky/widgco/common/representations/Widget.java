package com.hlprmnky.widgco.common.representations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Widget {
    private int widgetNo;
    private String name;

    public Widget() {}
    public Widget(int id, String name) {
        this.widgetNo = id;
        this.name = name;
    }

    @JsonProperty
    public int getWidgetNo() {
        return widgetNo;
    }

    @JsonProperty
    public void setWidgetNo(int widgetNo) {
        this.widgetNo = widgetNo;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof Widget)) {
            return false;
        }
        Widget o = (Widget)other;
        return(o.getName().equals(this.name) && o.getWidgetNo() == this.widgetNo);
    }
}
