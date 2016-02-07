package com.hlprmnky.widgco.services.datastore.factory;

import com.hlprmnky.widgco.services.datastore.dao.PartDAO;
import com.hlprmnky.widgco.services.datastore.dao.SupplierDAO;
import com.hlprmnky.widgco.services.datastore.dao.WidgetDAO;
import com.hlprmnky.widgco.services.datastore.dao.WidgetPartsDAO;
import com.hlprmnky.widgco.services.datastore.resources.DatasourceResource;
import org.skife.jdbi.v2.DBI;

public class DatastoreResourceFactory {

    private DBI database;
    public DatastoreResourceFactory(DBI database) {
        this.database = database;
    }

    public DatasourceResource build() {
        return new DatasourceResource(
                database.onDemand(WidgetDAO.class),
                database.onDemand(SupplierDAO.class),
                database.onDemand(PartDAO.class),
                database.onDemand(WidgetPartsDAO.class));
    }
}
