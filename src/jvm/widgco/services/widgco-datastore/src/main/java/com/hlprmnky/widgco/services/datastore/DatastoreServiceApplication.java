package com.hlprmnky.widgco.services.datastore;

import com.hlprmnky.widgco.services.datastore.factory.DatastoreResourceFactory;
import com.hlprmnky.widgco.services.datastore.resources.DatasourceResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.bundles.DBIExceptionsBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class DatastoreServiceApplication extends Application<DatastoreServiceConfiguration> {

    public static void main(String... args) throws Exception {
        new DatastoreServiceApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<DatastoreServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new DBIExceptionsBundle());
        bootstrap.addBundle(new MigrationsBundle<DatastoreServiceConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(DatastoreServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(DatastoreServiceConfiguration configuration, Environment environment) throws Exception {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final DatastoreResourceFactory datastoreResourceFactory = new DatastoreResourceFactory(jdbi);
        final DatasourceResource datastoreResource = datastoreResourceFactory.build();

        environment.jersey().register(datastoreResource);
    }
}
