package com.hlprmnky.widgco.common.representations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WidgetParts {
    private int widget_parts_no;
    private int widget_no;
    private int part_no;
    private int required_qty;

    public WidgetParts() {}
    public WidgetParts(int id, int widget_no, int part_no, int required_qty) {
        this.widget_parts_no = id;
        this.widget_no = widget_no;
        this.part_no = part_no;
        this.required_qty = required_qty;
    }

    @JsonProperty
    public int getWidget_parts_no() {
        return widget_parts_no;
    }

    @JsonProperty
    public void setWidget_parts_no(int widget_parts_no) {
        this.widget_parts_no = widget_parts_no;
    }

    @JsonProperty
    public int getWidget_no() {
        return widget_no;
    }

    @JsonProperty
    public void setWidget_no(int widget_no) {
        this.widget_no = widget_no;
    }

    @JsonProperty
    public int getPart_no() {
        return part_no;
    }

    @JsonProperty
    public void setPart_no(int part_no) {
        this.part_no = part_no;
    }

    @JsonProperty
    public int getRequired_qty() {
        return required_qty;
    }

    @JsonProperty
    public void setRequired_qty(int required_qty) {
        this.required_qty = required_qty;
    }


}
