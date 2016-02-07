package com.hlprmnky.widgco.common.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class StartWidgcoWorkflowRequestTest {
    final static ObjectMapper mapper = Jackson.newObjectMapper();
    final static StartWidgcoWorkflowRequest testRequest = new StartWidgcoWorkflowRequest(ImmutableMap.of(
            "Some Widget", 2,
            "A \"Valid\" Widget", 7));

    @Test
    public void testThatRequestSerializesToJson() throws Exception {
        assertThat(mapper.writeValueAsString(testRequest)).isEqualTo(fixture("fixtures/test-startwidgcoworkflowrequest.json"));
    }

    @Test
    public void testThatRequestDeserializesFromJson() throws Exception {
        assertThat(mapper.readValue(fixture("fixtures/test-startwidgcoworkflowrequest.json"), StartWidgcoWorkflowRequest.class))
                .isEqualToComparingFieldByField(testRequest);
    }
}
