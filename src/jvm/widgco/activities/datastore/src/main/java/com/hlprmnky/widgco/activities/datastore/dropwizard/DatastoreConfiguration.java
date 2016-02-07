package com.hlprmnky.widgco.activities.datastore.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class DatastoreConfiguration extends Configuration {

    @Valid
    @NotNull
    private JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();

    @NotNull
    private String swfEndpoint;

    @NotNull
    private String taskList;

    @NotNull
    private String swfDomain;

    @NotNull
    private String dataServiceEndpoint;


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

    @JsonProperty
    public String getSwfDomain() {
        return swfDomain;
    }

    @JsonProperty
    public void setSwfDomain(String swfDomain) {
        this.swfDomain = swfDomain;
    }

    @JsonProperty
    public JerseyClientConfiguration getJerseyClient() { return jerseyClient; }

    @JsonProperty
    public void setJerseyClient(JerseyClientConfiguration jerseyClient) { this.jerseyClient = jerseyClient; }

    @JsonProperty
    public String getDataServiceEndpoint() { return dataServiceEndpoint; }

    @JsonProperty
    public void setDataServiceEndpoint(String dataServiceEndpoint) { this.dataServiceEndpoint = dataServiceEndpoint; }
}
