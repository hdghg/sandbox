package com.github.atokar.producer;

import com.github.atokar.serializer.JsonDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import static java.util.Optional.ofNullable;
import static org.junit.Assert.assertTrue;

@Testcontainers
public class AsyncStillProducerTest {

    @Container
    public static final KafkaContainer KAFKA_CONTAINER = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:5.4.3"))
            .withEmbeddedZookeeper()
            .withCreateContainerCmdModifier(cmd -> ofNullable(cmd.getHostConfig())
                    .ifPresent(o -> o.withMemory(1024 * 1024 * 1024L)));

    @Test
    void testProducer() {
        System.err.println(KAFKA_CONTAINER.getBootstrapServers());

        AsyncStillProducer asyncStillProducer = new AsyncStillProducer(KAFKA_CONTAINER.getBootstrapServers());
        CompletableFuture<Void> completableFuture = asyncStillProducer.runSequenceAsync();

        Consumer<Long, Map<String, List<String>>> consumer = createConsumer();
        consumer.subscribe(Collections.singleton(StillProducer.TOPIC));
        for (int i = 0; i < 32; i++) {
            ConsumerRecords<Long, Map<String, List<String>>> poll = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<Long, Map<String, List<String>>> longMapConsumerRecord : poll) {
                System.err.println(longMapConsumerRecord);
            }
        }
        assertTrue(true);
        completableFuture.cancel(true);
    }

    public static Consumer<Long, Map<String, List<String>>> createConsumer() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_CONTAINER.getBootstrapServers());
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "still-test-consumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
        return new KafkaConsumer<>(props);
    }
}
