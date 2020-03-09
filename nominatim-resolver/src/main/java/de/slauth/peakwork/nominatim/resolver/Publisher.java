package de.slauth.peakwork.nominatim.resolver;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.v;

@Slf4j
@Component
public class Publisher {

    private static final String TOPIC = "nominatim-resolved";

    private final PubSubTemplate pubSubTemplate;

    public Publisher(JacksonPubSubMessageConverter jacksonPubSubMessageConverter, PubSubTemplate pubSubTemplate) {
        this.pubSubTemplate = pubSubTemplate;
        this.pubSubTemplate.setMessageConverter(jacksonPubSubMessageConverter);
    }

    @Async
    public void publish(JsonNode result) {
        log.info("Publishing result to topic {}", v("topic", TOPIC), v("json", result));
        pubSubTemplate.publish(TOPIC, result);
    }
}
