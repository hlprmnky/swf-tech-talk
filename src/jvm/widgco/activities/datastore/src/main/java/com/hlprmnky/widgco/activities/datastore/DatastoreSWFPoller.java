package com.hlprmnky.widgco.activities.datastore;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.ActivityTask;
import com.amazonaws.services.simpleworkflow.model.PollForActivityTaskRequest;
import com.amazonaws.services.simpleworkflow.model.RespondActivityTaskCompletedRequest;
import com.amazonaws.services.simpleworkflow.model.TaskList;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;
import com.hlprmnky.widgco.activities.datastore.dropwizard.DatastoreConfiguration;
import com.hlprmnky.widgco.common.messages.ValidateWidgetRequest;
import com.hlprmnky.widgco.common.messages.ValidateWidgetResponse;
import com.hlprmnky.widgco.common.representations.Widget;
import com.hlprmnky.widgco.common.util.RunLoop;
import io.dropwizard.jackson.Jackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatastoreSWFPoller extends RunLoop {

    public static final int SLEEP_INTERVAL = 10;
    public static final String VALIDATE_WIDGETS_QUERY = "/widget";
    private static Logger logger = LoggerFactory.getLogger(DatastoreSWFPoller.class);
    private Client client;
    private AmazonSimpleWorkflowClient workflowClient;
    private DatastoreConfiguration configuration;

    public DatastoreSWFPoller(Client client, AmazonSimpleWorkflowClient workflowClient, DatastoreConfiguration configuration) {
        super(logger, SLEEP_INTERVAL);
        this.client = client;
        this.workflowClient = workflowClient;
        this.configuration = configuration;
    }

    @VisibleForTesting
    public void doValidateWidgets(ObjectMapper mapper, ActivityTask task) {
        try {
            ValidateWidgetRequest inputMessage = mapper.readValue(task.getInput(), ValidateWidgetRequest.class);
            List<Widget> widgetsFound = new ArrayList<>();
            inputMessage.getWidgetNames().forEach(name -> {
                try {
                    Widget widget = client
                            .target(configuration.getDataServiceEndpoint() + VALIDATE_WIDGETS_QUERY)
                            .queryParam("name", name)
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .get(Widget.class);
                    widgetsFound.add(widget);
                } catch (NotFoundException e) {
                    logger.error("Widget name {} not found", name, e);
                }
            });

            logger.info("validation query complete, returning results {} for input {}", widgetsFound, inputMessage);
            ValidateWidgetResponse response = new ValidateWidgetResponse(widgetsFound, inputMessage.getWidgetNames());
            RespondActivityTaskCompletedRequest completedRequest = new RespondActivityTaskCompletedRequest();
            completedRequest.setResult(mapper.writeValueAsString(response));
            completedRequest.setTaskToken(task.getTaskToken());
            workflowClient.respondActivityTaskCompleted(completedRequest);
        } catch (JsonProcessingException e) {
            logger.error("Unable to map {} to JSON object", task.getInput(), e);
        } catch (IOException e) {
            logger.error("IOException while mapping {} to JSON object", task.getInput(), e);
        }
    }

    @Override
    public void doRunLoop() {
        logger.debug("Starting poller with workflowClient {}", workflowClient);
        ObjectMapper mapper = Jackson.newObjectMapper();
        PollForActivityTaskRequest request = new PollForActivityTaskRequest();
        request.setDomain(configuration.getSwfDomain());
        request.setTaskList(new TaskList().withName(configuration.getTaskList()));
        ActivityTask task = workflowClient.pollForActivityTask(request);
        if (task.getTaskToken() != null) {
            logger.info("Got task with token {}", task.getTaskToken());
            switch (task.getActivityType().getName()) {
                case "validateWidgetsByName":
                    doValidateWidgets(mapper, task);
                    break;
                default:
                    logger.error("Received unknown activity type: {}", task.getActivityType());
                    break;
            }
        } else {
            logger.info("No tasks waiting");
        }
    }
}
