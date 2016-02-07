package com.hlprmnky.widgco.common.representations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;


public class WidgetTest {
    static final ObjectMapper mapper = Jackson.newObjectMapper();
    static final Widget testWidget = new Widget(1, "test widget");

    @Test
    public void testThatWidgetSerializesToJson() throws Exception {
        assertThat(mapper.writeValueAsString(testWidget)).isEqualTo(fixture("fixtures/test-widget.json"));
    }

    @Test
    public void testThatWidgetDeserializesFromJson() throws Exception {
        assertThat(mapper.readValue(fixture("fixtures/test-widget.json"), Widget.class)).isEqualToComparingFieldByField(testWidget);
    }
}
