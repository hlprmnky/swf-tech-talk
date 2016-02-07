package com.hlprmnky.widgco.services.datastore.resources;

import com.google.common.collect.ImmutableList;
import com.hlprmnky.widgco.common.exceptions.WidgetNotFoundException;
import com.hlprmnky.widgco.common.representations.Widget;
import com.hlprmnky.widgco.services.datastore.dao.PartDAO;
import com.hlprmnky.widgco.services.datastore.dao.SupplierDAO;
import com.hlprmnky.widgco.services.datastore.dao.WidgetDAO;
import com.hlprmnky.widgco.services.datastore.dao.WidgetPartsDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class DatasourceResourceTest {

    @Mock
    private WidgetDAO widgetDAO;
    @Mock
    private SupplierDAO supplierDAO;
    @Mock
    private PartDAO partDAO;
    @Mock
    private WidgetPartsDAO widgetPartsDAO;

    private DatasourceResource resource;

    @Before
    public void setUp() throws Exception {
        initMocks(DatasourceResourceTest.class);
        resource = new DatasourceResource(widgetDAO, supplierDAO, partDAO, widgetPartsDAO);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFindWidgetByName() throws Exception {
        Widget expected = new Widget(2, "test");
        when(widgetDAO.findByName("test")).thenReturn(expected);
        assertThat(resource.findWidgetByName("test")).isEqualToComparingFieldByField(expected);

        URI path = UriBuilder.fromResource(DatasourceResource.class).path("/widget").queryParam("name", "test").build();

        when(widgetDAO.findByName(anyString())).thenThrow(new WidgetNotFoundException("Indeed nay."));
        try {
            resource.findWidgetByName("anyString");
        } catch (WidgetNotFoundException lookedFor) {
            assertThat(lookedFor.getMessage()).isEqualTo("Indeed nay.");
            return;
        }
        fail("Expected exception not thrown");
    }

    @Test
    public void testListWidgets() throws Exception {
        List<Widget> expected = ImmutableList.of(
                new Widget(1, "an widget"),
                new Widget(2, "another widget"),
                new Widget(3, "The Terminator"));
        when(widgetDAO.findAll()).thenReturn(expected);
        assertThat(resource.listWidgets()).isEqualTo(expected);
    }
}