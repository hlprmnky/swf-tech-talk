package com.hlprmnky.widgco.activities.datastore.dropwizard;

import com.hlprmnky.widgco.activities.datastore.DatastoreSWFPoller;
import com.hlprmnky.widgco.activities.datastore.factory.DatastoreSWFPollerFactory;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.ws.rs.client.Client;

public class DatastoreApplication extends Application<DatastoreConfiguration> {

    @Override
    public String getName() {
        return "Datastore SWF Activity";
    }

    @Override
    public void initialize(Bootstrap<DatastoreConfiguration> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(DatastoreConfiguration datastoreConfiguration, Environment environment) throws Exception {
        final Client client = new JerseyClientBuilder(environment).using(datastoreConfiguration.getJerseyClient())
                .build(getName());
        final DatastoreSWFPollerFactory pollerFactory = new DatastoreSWFPollerFactory();
        final DatastoreSWFPoller datastoreResource = pollerFactory.build(client, datastoreConfiguration);

        environment.lifecycle().executorService("datastore-poller").maxThreads(1).build().execute(datastoreResource);
    }

    public static void main(String... args) throws Exception {
        new DatastoreApplication().run(args);
    }


}
