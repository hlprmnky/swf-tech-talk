package com.hlprmnky.widgco.deciders.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class WidgcoDeciderConfiguration extends Configuration {

    @NotNull
    private String swfDomain;

    @NotNull
    private String swfEndpoint;

    @NotNull
    private String taskList;

    @JsonProperty
    public String getSwfDomain() {
        return swfDomain;
    }

    @JsonProperty
    public void setSwfDomain(String swfDomain) {
        this.swfDomain = swfDomain;
    }

    @JsonProperty
    public String getSwfEndpoint() {
        return swfEndpoint;
    }

    @JsonProperty
    public void setSwfEndpoint(String swfEndpoint) {
        this.swfEndpoint = swfEndpoint;
    }

    @JsonProperty
    public String getTaskList() {
        return taskList;
    }

    @JsonProperty
    public void setTaskList(String taskList) {
        this.taskList = taskList;
    }
}
