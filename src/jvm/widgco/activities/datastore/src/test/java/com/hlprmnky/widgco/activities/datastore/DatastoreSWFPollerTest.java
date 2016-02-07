package com.hlprmnky.widgco.activities.datastore;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.ActivityTask;
import com.amazonaws.services.simpleworkflow.model.RespondActivityTaskCompletedRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.hlprmnky.widgco.activities.datastore.dropwizard.DatastoreConfiguration;
import com.hlprmnky.widgco.common.messages.ValidateWidgetRequest;
import com.hlprmnky.widgco.common.messages.ValidateWidgetResponse;
import com.hlprmnky.widgco.common.representations.Widget;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class DatastoreSWFPollerTest {
    private DatastoreSWFPoller poller;
    final static ObjectMapper mapper = Jackson.newObjectMapper();

    @Mock
    private AmazonSimpleWorkflowClient workflowClient;

    @Captor
    ArgumentCaptor<RespondActivityTaskCompletedRequest> requestCaptor;

    @Mock
    private Client serviceClient;

    @Mock
    private DatastoreConfiguration configuration;

    @Mock
    private ActivityTask activityTask;

    @Before
    public void setUp() throws Exception {
        initMocks(DatastoreSWFPollerTest.class);
        when(configuration.getDataServiceEndpoint()).thenReturn("endpoint!");
        poller = new DatastoreSWFPoller(serviceClient, workflowClient, configuration);

        ValidateWidgetRequest request = new ValidateWidgetRequest();
        request.setWidgetNames(ImmutableList.of("First Widget"));
        when(activityTask.getInput()).thenReturn(mapper.writeValueAsString(request));

        doNothing().when(workflowClient).respondActivityTaskCompleted(requestCaptor.capture());
    }

    @Test
    public void testDoValidateWidgets() throws Exception {
        Widget widget = new Widget(1, "First Widget");
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(builder.get(Widget.class)).thenReturn(widget);
        WebTarget mockTarget = mock(WebTarget.class);
        when(mockTarget.queryParam(eq("name"), anyString())).thenReturn(mockTarget);
        when(mockTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
        when(serviceClient.target(anyString())).thenReturn(mockTarget);

        poller.doValidateWidgets(mapper, activityTask);

        ValidateWidgetResponse expected = new ValidateWidgetResponse(ImmutableList.of(new Widget(1, "First Widget")), Collections.singletonList("First Widget"));
        ValidateWidgetResponse actual = mapper.readValue(requestCaptor.getValue().getResult(), ValidateWidgetResponse.class);
        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(workflowClient, times(1)).respondActivityTaskCompleted(any(RespondActivityTaskCompletedRequest.class));

    }

    @Test
    public void testDoValidateWidgets_emptyResponseForNotFound() throws Exception {
        Invocation.Builder builder = mock(Invocation.Builder.class);
        when(builder.get(Widget.class)).thenThrow(new NotFoundException("nnnneeeewp."));
        WebTarget mockTarget = mock(WebTarget.class);
        when(mockTarget.queryParam(eq("name"), anyString())).thenReturn(mockTarget);
        when(mockTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(builder);
        when(serviceClient.target(anyString())).thenReturn(mockTarget);

        poller.doValidateWidgets(mapper, activityTask);

        ValidateWidgetResponse expected = new ValidateWidgetResponse(Collections.<Widget>emptyList(), Arrays.asList("First Widget"));
        ValidateWidgetResponse actual = mapper.readValue(requestCaptor.getValue().getResult(), ValidateWidgetResponse.class);
        assertThat(actual).isEqualToComparingFieldByField(expected);
        verify(workflowClient, times(1)).respondActivityTaskCompleted(any(RespondActivityTaskCompletedRequest.class));
    }

}