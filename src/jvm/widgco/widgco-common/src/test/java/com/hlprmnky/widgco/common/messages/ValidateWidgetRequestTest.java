package com.hlprmnky.widgco.common.messages;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import java.util.Arrays;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class ValidateWidgetRequestTest {

    static final ObjectMapper mapper = Jackson.newObjectMapper();
    static final ValidateWidgetRequest testRequest =
            new ValidateWidgetRequest(Arrays.asList("A Valid Widget", "Widget 4", "Another \"Valid\" Widget"));

    @Test
    public void testThatValidateWidgetRequestSerializesToJson() throws Exception {
        assertThat(mapper.writeValueAsString(testRequest)).isEqualTo(fixture("fixtures/test-validatewidgetrequest.json"));
    }

    @Test
    public void testThatValidateWidgetRequestDeserializesFromJson() throws Exception {
        assertThat(mapper.readValue(fixture("fixtures/test-validatewidgetrequest.json"), ValidateWidgetRequest.class))
                .isEqualToComparingFieldByField(testRequest);
    }

}