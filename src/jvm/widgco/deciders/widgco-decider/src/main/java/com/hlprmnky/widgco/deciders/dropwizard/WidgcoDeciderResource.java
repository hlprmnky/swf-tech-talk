package com.hlprmnky.widgco.deciders.dropwizard;

import com.amazonaws.services.simpleworkflow.AmazonSimpleWorkflowClient;
import com.amazonaws.services.simpleworkflow.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hlprmnky.widgco.common.messages.StartWidgcoWorkflowRequest;
import io.dropwizard.jackson.Jackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/workflows")
public class WidgcoDeciderResource {

    private AmazonSimpleWorkflowClient client;
    private WidgcoDeciderConfiguration configuration;
    private Logger logger = LoggerFactory.getLogger(WidgcoDeciderResource.class);

    public WidgcoDeciderResource(AmazonSimpleWorkflowClient client, WidgcoDeciderConfiguration configuration) {
        this.client = client;
        this.configuration = configuration;
    }

    @GET
    @Path("/workflows")
    public Response getWorkflows() {
        ListOpenWorkflowExecutionsRequest openWorkflowExecutionsRequest = new ListOpenWorkflowExecutionsRequest()
                .withDomain(configuration.getSwfDomain());
        WorkflowExecutionInfos openExecutions = client.listOpenWorkflowExecutions(openWorkflowExecutionsRequest);
        ListClosedWorkflowExecutionsRequest closedWorkflowExecutionsRequest = new ListClosedWorkflowExecutionsRequest()
                .withDomain(configuration.getSwfDomain());
        WorkflowExecutionInfos closedExecutions = client.listClosedWorkflowExecutions(closedWorkflowExecutionsRequest);
        return Response.ok().entity("TRY AGAIN LATER").build();
    }

    @GET
    @Path("/workflows/{workflowId}/details")
    public Response getWorkflowDetails(@PathParam("workflowId") String workflowId) {
        return Response.ok(workflowId).build();
    }

    @POST
    public Response startNewWorkflow(StartWidgcoWorkflowRequest inputRequest) {
        try {
            StartWorkflowExecutionRequest request = new StartWorkflowExecutionRequest()
                    .withDomain(configuration.getSwfDomain())
                    .withTaskList(new TaskList().withName(configuration.getTaskList()))
                    .withInput(Jackson.newObjectMapper().writeValueAsString(inputRequest))
                    .withWorkflowId(UUID.randomUUID().toString())
                    .withWorkflowType(new WorkflowType().withName("WidgcoWorkflow").withVersion("0.1.0"));
            Run run = client.startWorkflowExecution(request);
            return Response.ok(run).build();
        } catch (JsonProcessingException e) {
            logger.error("Unable to write {} as JSON string", inputRequest, e);
            return Response.serverError().entity("Unable to write [ " + inputRequest + " ] as JSON string: " + e.getMessage()).build();
        }

    }

}
