package com.hlprmnky.widgco.common.representations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;


public class PartTest {

    final static ObjectMapper mapper = Jackson.newObjectMapper();
    final static Part testPart = new Part(1, "test part", 1, 2);

    @Test
    public void testThatPartSerializesToJson() throws Exception {
        assertThat(mapper.writeValueAsString(testPart)).isEqualTo(fixture("fixtures/test-part.json"));
    }

    @Test
    public void testThatPartDeserializesFromJson() throws Exception {
        assertThat(mapper.readValue(fixture("fixtures/test-part.json"), Part.class)).isEqualToComparingFieldByField(testPart);
    }
}