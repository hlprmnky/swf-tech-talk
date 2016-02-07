package com.hlprmnky.widgco.deciders.statemachine;

import com.amazonaws.services.simpleworkflow.model.ActivityTaskCompletedEventAttributes;
import com.amazonaws.services.simpleworkflow.model.EventType;
import com.amazonaws.services.simpleworkflow.model.HistoryEvent;
import com.amazonaws.services.simpleworkflow.model.WorkflowExecutionStartedEventAttributes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.hlprmnky.widgco.common.messages.StartWidgcoWorkflowRequest;
import com.hlprmnky.widgco.common.messages.ValidateWidgetResponse;
import com.hlprmnky.widgco.common.representations.Widget;
import com.hlprmnky.widgco.deciders.statemachine.states.Completed;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class StateTest {

    public static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private List<HistoryEvent> events;

    @Before
    public void setUp() throws Exception {
        events = buildWorkflowStartEvents();
    }

    @Test
    public void testRunStateMachineStartsWorkflow() throws Exception {
        State result = State.runStateMachine(events);
        assertThat(result.getClass().getSimpleName()).isEqualTo("ValidateWidgets");
    }

    @Test
    public void testStateMachineFindsAnError() throws Exception {
        ValidateWidgetResponse errorResponse = new ValidateWidgetResponse();
        errorResponse.setWidgetsRequested(Arrays.asList("a widget", "a \"valid\" widget"));
        errorResponse.setWidgetsFound(Arrays.asList(new Widget(3, "a widget"), new Widget(77, "another widget")));
        events.add(new HistoryEvent()
                .withEventType(EventType.ActivityTaskCompleted)
                .withActivityTaskCompletedEventAttributes(new ActivityTaskCompletedEventAttributes()
                .withResult(MAPPER.writeValueAsString(errorResponse))));
        State result = State.runStateMachine(events);
        assertThat(result.getClass().getSimpleName()).isEqualTo("Failed");

    }

    @Test
    public void testStateMachineRequestsWidgetValidation() throws Exception {
        ValidateWidgetResponse validResponse = new ValidateWidgetResponse();
        validResponse.setWidgetsRequested(Arrays.asList("A widget", "Another widget"));
        validResponse.setWidgetsFound(Arrays.asList(new Widget(1, "A widget"), new Widget(2, "Another widget")));
        events.add(new HistoryEvent()
                .withEventType(EventType.ActivityTaskCompleted)
                .withActivityTaskCompletedEventAttributes(new ActivityTaskCompletedEventAttributes()
                        .withResult(MAPPER.writeValueAsString(validResponse))));
        State result = State.runStateMachine(events);
        assertThat(result.getClass().getSimpleName()).isEqualTo("ValidateInventory");
    }

    private List<HistoryEvent> buildWorkflowStartEvents() throws JsonProcessingException {
        StartWidgcoWorkflowRequest request = new StartWidgcoWorkflowRequest(
                ImmutableMap.of("Test Widget", 2, "Other Widget", 524));
        List<HistoryEvent> events = new ArrayList<>();
        events.add(new HistoryEvent()
                .withEventType(EventType.WorkflowExecutionStarted)
                .withWorkflowExecutionStartedEventAttributes(new WorkflowExecutionStartedEventAttributes()
                        .withInput(MAPPER.writeValueAsString(request))));
        events.add(new HistoryEvent()
                .withEventType(EventType.DecisionTaskScheduled));
        events.add(new HistoryEvent()
                .withEventType(EventType.DecisionTaskStarted));
        return events;
    }

    @Test
    public void testWithEvent() throws Exception {
        HistoryEvent event = new HistoryEvent();
        event.setEventType(EventType.ActivityTaskCompleted);
        State test = new Completed().withEvent(event);
        assertEquals(EventType.ActivityTaskCompleted.toString(), test.getEvent().getEventType());
    }
}