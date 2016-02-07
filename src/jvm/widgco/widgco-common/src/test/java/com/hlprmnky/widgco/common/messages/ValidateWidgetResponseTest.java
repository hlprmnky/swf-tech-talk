package com.hlprmnky.widgco.common.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.hlprmnky.widgco.common.representations.Widget;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.util.Arrays;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class ValidateWidgetResponseTest {

    static final ObjectMapper mapper = Jackson.newObjectMapper();
    static final ValidateWidgetResponse testResponse = new ValidateWidgetResponse(ImmutableList.of(
            new Widget(1, "first test widget"),
            new Widget(2, "another test widget"),
            new Widget(3, "a \"valid\" widget"),
            new Widget(4, "The Terminator")),
            Arrays.asList("first test widget", "another test widget", "a \"valid\" widget", "The Terminator"));

    @Test
    public void testThatValidateWidgetResponseSerializesToJson() throws Exception {
        assertThat(mapper.writeValueAsString(testResponse)).isEqualTo(fixture("fixtures/test-validatewidgetresponse.json"));
    }

    @Test
    public void testThatValidateWidgetResponseDeserializesFromJson() throws Exception {
        assertThat(mapper.readValue(fixture("fixtures/test-validatewidgetresponse.json"), ValidateWidgetResponse.class))
                .isEqualToComparingFieldByField(testResponse);
    }

}