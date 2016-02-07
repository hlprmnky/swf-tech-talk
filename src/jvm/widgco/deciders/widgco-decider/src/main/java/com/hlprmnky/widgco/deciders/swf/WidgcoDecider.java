package com.hlprmnky.widgco.deciders.swf;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.hlprmnky.widgco.common.util.RunLoop;
import com.hlprmnky.widgco.deciders.dropwizard.WidgcoDeciderConfiguration;
import com.hlprmnky.widgco.deciders.statemachine.State;
import com.hlprmnky.widgco.deciders.statemachine.states.*;
import io.dropwizard.jackson.Jackson;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class WidgcoDecider extends RunLoop {

    public static final int SLEEP_INTERVAL = 1000;
    private static Logger logger = LoggerFactory.getLogger(WidgcoDecider.class);
    private AmazonSimpleWorkflowClient client;
    private WidgcoDeciderConfiguration configuration;

    public WidgcoDecider(AmazonSimpleWorkflowClient client, WidgcoDeciderConfiguration configuration) {
        super(logger, SLEEP_INTERVAL);
        this.client = client;
        this.configuration = configuration;
    }

    @Override
    public void doRunLoop() {
        ObjectMapper mapper = Jackson.newObjectMapper();
        PollForDecisionTaskRequest request = new PollForDecisionTaskRequest()
                .withDomain(configuration.getSwfDomain())
                .withTaskList(new TaskList().withName(configuration.getTaskList()));
        DecisionTask task = client.pollForDecisionTask(request);
        if (task.getTaskToken() != null) {
            State nextAction = State.runStateMachine(task.getEvents());
            logger.info("State processing complete, next state is {}", ReflectionToStringBuilder.toString(nextAction));
            processNextAction(task.getTaskToken(), mapper, nextAction);
        } else {
            logger.info("No decisions to process");
        }
    }

    @VisibleForTesting
    public void processNextAction(String taskToken, ObjectMapper mapper, State nextAction) {
        switch (nextAction.getType()) {
            case FAILED:
                Failed failed = (Failed) nextAction;
                logger.error("Workflow has failed with error state {}", failed);
                Decision die = new Decision()
                        .withDecisionType(DecisionType.FailWorkflowExecution)
                        .withFailWorkflowExecutionDecisionAttributes(new FailWorkflowExecutionDecisionAttributes()
                                .withReason("Workflow execution failed"));
                client.respondDecisionTaskCompleted(new RespondDecisionTaskCompletedRequest()
                        .withTaskToken(taskToken)
                        .withDecisions(die));
                break;
            case COMPLETED:
                Completed completed = (Completed) nextAction;
                logger.info("Completing workflow with completion state {}", completed);
                Decision victory = new Decision()
                        .withDecisionType(DecisionType.CompleteWorkflowExecution)
                        .withCompleteWorkflowExecutionDecisionAttributes(new CompleteWorkflowExecutionDecisionAttributes()
                                .withResult("Great success!"));
                client.respondDecisionTaskCompleted(new RespondDecisionTaskCompletedRequest()
                        .withTaskToken(taskToken)
                        .withDecisions(victory));
                break;
            case VALIDATE_WIDGETS:
                ValidateWidgets validateWidgets = (ValidateWidgets) nextAction;
                logger.info("Received request to validate widgets {}", validateWidgets);
                try {
                    Decision getWidgets = new Decision()
                            .withDecisionType(DecisionType.ScheduleActivityTask)
                            .withScheduleActivityTaskDecisionAttributes(new ScheduleActivityTaskDecisionAttributes()
                                    .withActivityType(new ActivityType()
                                            .withName("validateWidgetsByName")
                                            .withVersion("0.1.0"))
                                    .withActivityId(UUID.randomUUID().toString())
                                    .withHeartbeatTimeout("2000")
                                    .withInput(mapper.writeValueAsString(validateWidgets.getRequest())));
                    client.respondDecisionTaskCompleted(new RespondDecisionTaskCompletedRequest()
                            .withTaskToken(taskToken)
                            .withDecisions(getWidgets));
                } catch (JsonProcessingException e) {
                    logger.error("Unable to deserialize {}, ", validateWidgets.getRequest(), e);
                }
                break;
            case VALIDATE_INVENTORY:
                ValidateInventory validateInventory = (ValidateInventory)nextAction;
                logger.info("Received validate inventory request {}", validateInventory);
                break;
            case START:
                WorkflowStart workflowStart = (WorkflowStart)nextAction;
                logger.info("Received start workflow request {}", workflowStart);
                client.respondDecisionTaskCompleted(new RespondDecisionTaskCompletedRequest()
                        .withTaskToken(taskToken));
                break;
            default:
                logger.error("Unhandled action type {}", nextAction.getType());
        }
    }
}
