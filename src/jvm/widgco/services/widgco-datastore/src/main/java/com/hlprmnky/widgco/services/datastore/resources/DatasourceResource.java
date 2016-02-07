package com.hlprmnky.widgco.services.datastore.resources;

import com.codahale.metrics.annotation.Timed;
import com.hlprmnky.widgco.common.exceptions.WidgetNotFoundException;
import com.hlprmnky.widgco.common.representations.Widget;
import com.hlprmnky.widgco.services.datastore.dao.PartDAO;
import com.hlprmnky.widgco.services.datastore.dao.SupplierDAO;
import com.hlprmnky.widgco.services.datastore.dao.WidgetDAO;
import com.hlprmnky.widgco.services.datastore.dao.WidgetPartsDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class DatasourceResource {

    private WidgetDAO widgetDAO;
    private SupplierDAO supplierDAO;
    private PartDAO partDAO;
    private WidgetPartsDAO widgetPartsDAO;

    public DatasourceResource(WidgetDAO widgetDAO, SupplierDAO supplierDAO, PartDAO partDAO, WidgetPartsDAO widgetPartsDAO) {

        this.widgetDAO = widgetDAO;
        this.supplierDAO = supplierDAO;
        this.partDAO = partDAO;
        this.widgetPartsDAO = widgetPartsDAO;
    }


    @Path("/widget")
    @Timed
    @GET
    public Widget findWidgetByName(@QueryParam("name") String name) {
        Widget maybeWidget = widgetDAO.findByName(name);
        if(maybeWidget != null) {
            return maybeWidget;
        } else {
            throw new WidgetNotFoundException("No widget found for name " + name);
        }
    }

    @Path("/widgets")
    @Timed
    @GET
    public List<Widget> listWidgets() {
        return widgetDAO.findAll();
    }

}
