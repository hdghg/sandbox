## Compact message producer for kafka (minimal docker image)
Requires: docker, maven, java8

### Kafka producer required properties

    BOOTSTRAP_SERVERS_CONFIG        bootstrap.servers   url to zookeeper, i.e. PLAINTEXT://localhost:32826
    CLIENT_ID_CONFIG                client.id           unique identifier of the client
    KEY_SERIALIZER_CLASS_CONFIG     key.serializer      obvious
    VALUE_SERIALIZER_CLASS_CONFIG   value.serializer    obvious

### Kafka consumer required properties

    BOOTSTRAP_SERVERS_CONFIG            bootstrap.servers   url to zookeeper, i.e. PLAINTEXT://localhost:32826
    CLIENT_ID_CONFIG                    client.id           unique identifier of the client
    KEY_DESERIALIZER_CLASS_CONFIG       key.deserializer    obvious
    VALUE_DESERIALIZER_CLASS_CONFIG     value.deserializer  obvious
    GROUP_ID_CONFIG                     group.id            to be able to call subscribe(topic)
