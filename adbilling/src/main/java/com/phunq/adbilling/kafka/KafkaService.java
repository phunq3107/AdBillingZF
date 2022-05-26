package com.phunq.adbilling.kafka;

import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    private final Integer kafkaPartitionId;
    private final KafkaProducer<String, TransactionHistoryMessage> kafkaProducer;


    public KafkaService(
            @Value("${server.port}") Integer serverPort,
            KafkaProducer<String, TransactionHistoryMessage> kafkaProducer) {
        this.kafkaPartitionId = serverPort - 8100;
        this.kafkaProducer = kafkaProducer;
    }


    public void sendCreditMessage(Long userId, Long amount, Long balance) {
        TransactionHistoryMessage creditMessage = TransactionHistoryMessage.createCreditMessage(
                userId,
                amount,
                balance
        );
        ProducerRecord<String, TransactionHistoryMessage> pc = new ProducerRecord<>(
                KafkaTopicConfig.TRANSACTION_HISTORY_TOPIC,
                kafkaPartitionId,
                "",
                creditMessage
        );
        kafkaProducer.send(pc);


    }

    public void sendDebitMessage(Long fromUser, Long toUser, Long amount, Long balance) {
        TransactionHistoryMessage debitMessage = TransactionHistoryMessage.createDebitMessage(
                fromUser,
                toUser,
                amount,
                balance
        );
        ProducerRecord<String, TransactionHistoryMessage> pc = new ProducerRecord<>(
                KafkaTopicConfig.TRANSACTION_HISTORY_TOPIC,
                kafkaPartitionId,
                null,
                debitMessage
        );
        kafkaProducer.send(pc);

    }
}
