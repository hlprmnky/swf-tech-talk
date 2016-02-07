package com.hlprmnky.widgco.deciders.dropwizard;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.Decision;
import com.amazonaws.services.simpleworkflow.model.DecisionType;
import com.amazonaws.services.simpleworkflow.model.RespondDecisionTaskCompletedRequest;
import com.hlprmnky.widgco.common.messages.ValidateWidgetRequest;
import com.hlprmnky.widgco.deciders.statemachine.State;
import com.hlprmnky.widgco.deciders.statemachine.states.Completed;
import com.hlprmnky.widgco.deciders.statemachine.states.Failed;
import com.hlprmnky.widgco.deciders.statemachine.states.ValidateWidgets;
import com.hlprmnky.widgco.deciders.swf.WidgcoDecider;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class WidgcoDeciderResourceTest {

    private WidgcoDecider decider;

    @Mock
    private AmazonSimpleWorkflowClient client;

    @Mock
    private WidgcoDeciderConfiguration configuration;

    @Captor
    private ArgumentCaptor<RespondDecisionTaskCompletedRequest> decisionRequestCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(WidgcoDeciderResourceTest.class);
        doNothing().when(client).respondDecisionTaskCompleted(decisionRequestCaptor.capture());
        decider = new WidgcoDecider(client, configuration);
    }

    @Test
    public void testFailsErroredWorkflow() throws Exception {
        State failed = new Failed();
        decider.processNextAction("token", Jackson.newObjectMapper(), failed);
        assertThat(decisionRequestCaptor.getValue().getTaskToken()).isEqualTo("token");
        assertThat(decisionRequestCaptor.getValue().getDecisions().size()).isEqualTo(1);
        Decision sent = decisionRequestCaptor.getValue().getDecisions().get(0);
        assertThat(sent.getDecisionType()).isEqualTo(DecisionType.FailWorkflowExecution.toString());
        assertThat(sent.getFailWorkflowExecutionDecisionAttributes().getReason()).isEqualTo("Workflow execution failed");
        verify(client, times(1)).respondDecisionTaskCompleted(any(RespondDecisionTaskCompletedRequest.class));
    }

    @Test
    public void testCompletesFinishedWorkflow() throws Exception {
        State completed = new Completed();
        decider.processNextAction("token", Jackson.newObjectMapper(), completed);
        assertThat(decisionRequestCaptor.getValue().getTaskToken()).isEqualTo("token");
        assertThat(decisionRequestCaptor.getValue().getDecisions().size()).isEqualTo(1);
        Decision sent = decisionRequestCaptor.getValue().getDecisions().get(0);
        assertThat(sent.getDecisionType()).isEqualTo(DecisionType.CompleteWorkflowExecution.toString());
        assertThat(sent.getCompleteWorkflowExecutionDecisionAttributes().getResult()).isEqualTo("Great success!");
        verify(client, times(1)).respondDecisionTaskCompleted(any(RespondDecisionTaskCompletedRequest.class));
    }

    @Test
    public void testRequestsWidgetValidation() throws Exception {
        ValidateWidgetRequest widgetRequest = new ValidateWidgetRequest(Arrays.asList("a widget", "a \"valid\" widget"));
        State validateWidgets = new ValidateWidgets()
                .withRequest(widgetRequest);
        decider.processNextAction("token", Jackson.newObjectMapper(), validateWidgets);
        assertThat(decisionRequestCaptor.getValue().getTaskToken()).isEqualTo("token");
        assertThat(decisionRequestCaptor.getValue().getDecisions().size()).isEqualTo(1);
        Decision sent = decisionRequestCaptor.getValue().getDecisions().get(0);
        assertThat(sent.getDecisionType()).isEqualTo(DecisionType.ScheduleActivityTask.toString());
        assertThat(sent.getScheduleActivityTaskDecisionAttributes().getInput())
                .isEqualTo(Jackson.newObjectMapper().writeValueAsString(widgetRequest));
        verify(client, times(1)).respondDecisionTaskCompleted(any(RespondDecisionTaskCompletedRequest.class));
    }
}