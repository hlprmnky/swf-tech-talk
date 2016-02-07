package com.hlprmnky.widgco.common.representations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class WidgetPartsTest {
    final static ObjectMapper mapper = Jackson.newObjectMapper();
    final static WidgetParts testWidgetParts = new WidgetParts(1, 2, 4, 27);

    @Test
    public void testThatWidgetPartsSerializesToJson() throws Exception {
        assertThat(mapper.writeValueAsString(testWidgetParts)).isEqualTo(fixture("fixtures/test-widgetparts.json"));
    }

    @Test
    public void testThatWidgetPartsDeserializesFromJson() throws Exception {
        assertThat(mapper.readValue(fixture("fixtures/test-widgetparts.json"), WidgetParts.class))
                .isEqualToComparingFieldByField(testWidgetParts);
    }
}
