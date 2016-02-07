package com.hlprmnky.widgco.common.representations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class SupplierTest {
    final static ObjectMapper mapper = Jackson.newObjectMapper();
    final static Supplier testSupplier = new Supplier(1, "test supplier", "A complex address string, USA, Earth 12345");

    @Test
    public void testThatSupplierSerializesToJson() throws Exception {
        assertThat(mapper.writeValueAsString(testSupplier)).isEqualTo(fixture("fixtures/test-supplier.json"));
    }

    @Test
    public void testThatSupplierDeserializesFromJson() throws Exception {
        assertThat(mapper.readValue(fixture("fixtures/test-supplier.json"), Supplier.class)).isEqualToComparingFieldByField(testSupplier);
    }

}
