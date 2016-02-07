package com.hlprmnky.widgco.deciders.dropwizard;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.hlprmnky.widgco.deciders.swf.WidgcoDecider;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class WidgcoDeciderApplication extends Application<WidgcoDeciderConfiguration> {

    @Override
    public void run(WidgcoDeciderConfiguration widgcoDeciderConfiguration, Environment environment) throws Exception {
        final AmazonSimpleWorkflowClient client = new AmazonSimpleWorkflowClient(new AWSCredentialsProviderChain(new DefaultAWSCredentialsProviderChain()));
        client.setEndpoint(widgcoDeciderConfiguration.getSwfEndpoint());

        final WidgcoDeciderResource resource = new WidgcoDeciderResource(client, widgcoDeciderConfiguration);
        final WidgcoDecider decider = new WidgcoDecider(client, widgcoDeciderConfiguration);

        environment.lifecycle().executorService("widgcoDecider").maxThreads(1).build().execute(decider);
        environment.jersey().register(resource);
    }

    public static void main(String... args) throws Exception {
        new WidgcoDeciderApplication().run(args);
    }
}
