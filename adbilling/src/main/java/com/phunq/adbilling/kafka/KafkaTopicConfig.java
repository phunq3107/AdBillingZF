package com.phunq.adbilling.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String TRANSACTION_HISTORY_TOPIC = "TRANSACTION_HISTORY_TOPIC";

    @Bean
    public NewTopic createTransactionHistoryTopic() {
        return TopicBuilder.name(TRANSACTION_HISTORY_TOPIC).partitions(3).build();
    }
}
