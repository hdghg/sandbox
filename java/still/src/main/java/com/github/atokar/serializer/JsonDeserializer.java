package com.github.atokar.serializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JsonDeserializer implements Deserializer<Map<String, Object>> {

    private static final Logger log = LoggerFactory.getLogger(JsonDeserializer.class);

    @Override
    public Map<String, Object> deserialize(String topic, byte[] data) {
        try {
            return new ObjectMapper().readValue(data, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            // implementations are recommended to handle null by returning a value or null rather than throwing an exception.
            log.warn("Could not deserialize json", e);
            return null;
        }
    }
}
