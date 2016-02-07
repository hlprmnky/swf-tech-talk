package com.hlprmnky.widgco.services.datastore.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hlprmnky.widgco.common.representations.Widget;
import com.hlprmnky.widgco.services.datastore.dao.PartDAO;
import com.hlprmnky.widgco.services.datastore.dao.SupplierDAO;
import com.hlprmnky.widgco.services.datastore.dao.WidgetDAO;
import com.hlprmnky.widgco.services.datastore.dao.WidgetPartsDAO;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatasourceEndpointTest {
    static ObjectMapper mapper = Jackson.newObjectMapper();

    private final WidgetDAO widgetDAO = mock(WidgetDAO.class);
    private final SupplierDAO supplierDAO = mock(SupplierDAO.class);
    private final PartDAO partDAO = mock(PartDAO.class);
    private final WidgetPartsDAO widgetPartsDAO = mock(WidgetPartsDAO.class);


    @Before
    public void setUp() throws Exception {
        when(widgetDAO.findByName(eq("test"))).thenReturn(new Widget(1, "test"));
    }

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new DatasourceResource(widgetDAO, supplierDAO, partDAO, widgetPartsDAO))
            .build();

    @Test
    public void testFindWidgetByName() throws Exception {
        Widget response = resources.client()
                .target("/widget")
                .queryParam("name", "test")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Widget.class);
        assertThat(response).isEqualTo(new Widget(1, "test"));
    }

    @Test
    public void testWidgetNotFound() throws Exception {
        try {
            resources.client()
                    .target("/widget")
                    .queryParam("name", "not found")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Widget.class);
        } catch(NotFoundException expected) {
            assertThat(expected.getMessage()).isEqualTo("HTTP 404 Not Found");
            return;
        }
        fail("Expected exception not thrown");
    }
}
