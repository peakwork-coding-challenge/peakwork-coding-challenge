package de.slauth.peakwork.nominatim.saveservice;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.cloud.gcp.pubsub.support.converter.JacksonPubSubMessageConverter;
import org.springframework.stereotype.Component;

import static net.logstash.logback.argument.StructuredArguments.v;

@Slf4j
@Component
@RequiredArgsConstructor
public class Subscriber implements ApplicationRunner {

    private static final String SUBSCRIPTION = "nominatim-resolved-subscription";

    private final JacksonPubSubMessageConverter jacksonPubSubMessageConverter;
    private final PubSubTemplate pubSubTemplate;
    private final SaveService saveService;

    @Override
    public void run(ApplicationArguments args) {
        pubSubTemplate.subscribe(SUBSCRIPTION, message -> {
            try {
                JsonNode json = jacksonPubSubMessageConverter.fromPubSubMessage(message.getPubsubMessage(), JsonNode.class);
                log.info("Received JSON message: {}", v("json", json));
                saveService.save(json);
            } catch (Exception e) {
                log.error("Error processing message", e);
            } finally {
                message.ack();
            }
        });
    }
}
