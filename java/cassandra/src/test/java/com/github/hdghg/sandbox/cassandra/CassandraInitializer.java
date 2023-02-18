package com.github.hdghg.sandbox.cassandra;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

public class CassandraInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final CassandraContainer CASSANDRA_CONTAINER = (CassandraContainer)
            new CassandraContainer(DockerImageName.parse("cassandra").withTag("4.0.4"))
                    .withEnv("HEAP_NEWSIZE", "256M")
                    .withEnv("MAX_HEAP_SIZE", "1024M")
                    .withEnv("JVM_OPTS", "-Dcassandra.skip_wait_for_gossip_to_settle=0 -Dcassandra.initial_token=0")
                    .waitingFor(
                            new LogMessageWaitStrategy()
                                    .withRegEx(".*Startup complete.*\\s")
                                    .withStartupTimeout(Duration.ofSeconds(70))
                    );

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        CASSANDRA_CONTAINER.start();
        TestPropertyValues.of(
                "spring.data.cassandra.contact-points=" + CASSANDRA_CONTAINER.getHost(),
                "spring.data.cassandra.port=" + CASSANDRA_CONTAINER.getFirstMappedPort(),
                "spring.data.cassandra.local-datacenter=datacenter1"
        ).applyTo(applicationContext);
    }
}
