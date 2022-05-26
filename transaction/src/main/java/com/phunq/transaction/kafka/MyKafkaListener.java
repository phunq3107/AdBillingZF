package com.phunq.transaction.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
public class MyKafkaListener {

    //    @KafkaListener(topics = "TRANSACTION_HISTORY_TOPIC", groupId = "group-1")
//    @KafkaListener(topics = "TRANSACTION_HISTORY_TOPIC", groupId = "group-1", containerFactory = "transactionListener")
    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = "TRANSACTION_HISTORY_TOPIC", partitions = {"0", "1", "2"})
    }, groupId = "zf-g01", containerFactory = "transactionListener")
    void listenerPartition0(TransactionHistoryMessage data) {
        System.out.println("0: " + data);
    }
//
//    @KafkaListener(topicPartitions = {
//            @TopicPartition(topic = "TRANSACTION_HISTORY_TOPIC", partitions = "1")
//    }, groupId = "zf-g01", containerFactory = "transactionListener")
//    void listenerPartition1(TransactionHistoryMessage data) {
//        System.out.println("1: " + data);
//    }
//
//    @KafkaListener(topicPartitions = {
//            @TopicPartition(topic = "TRANSACTION_HISTORY_TOPIC", partitions = "2")
//    }, groupId = "zf-g01", containerFactory = "transactionListener")
//    void listenerPartition2(TransactionHistoryMessage data) {
//        System.out.println("2: " + data);
//    }
}
