package com.hlprmnky.widgco.activities.datastore.factory;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.hlprmnky.widgco.activities.datastore.dropwizard.DatastoreConfiguration;
import com.hlprmnky.widgco.activities.datastore.DatastoreSWFPoller;

import javax.ws.rs.client.Client;


public class DatastoreSWFPollerFactory {

    public DatastoreSWFPoller build(Client jerseyClient, DatastoreConfiguration configuration) {
        final AmazonSimpleWorkflowClient client = new AmazonSimpleWorkflowClient(new AWSCredentialsProviderChain(new DefaultAWSCredentialsProviderChain()));
        client.setEndpoint(configuration.getSwfEndpoint());
        return new DatastoreSWFPoller(jerseyClient, client, configuration);
    }
}
