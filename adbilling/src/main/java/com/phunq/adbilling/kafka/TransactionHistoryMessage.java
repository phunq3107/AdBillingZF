package com.phunq.adbilling.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryMessage implements Serializable {
    private Long uid;
    private Long amount;
    private Long balance;
    private TransactionType type;
    private LocalDateTime timestamp;
    private Long toUser;
    private Long itemId;
    private Long quantity;
    private Long price;
    private String note;

    public static TransactionHistoryMessage createCreditMessage(Long userId, Long amount, Long balance) {

        return TransactionHistoryMessage.builder()
                .uid(userId)
                .amount(amount)
                .balance(balance)
                .type(TransactionType.CREDIT)
                .timestamp(LocalDateTime.now())
                .itemId(null)
                .quantity(null)
                .price(null)
                .note("")
                .build();
    }

    public static TransactionHistoryMessage createDebitMessage(Long userId, Long toUser, Long amount, Long balance) {
        return TransactionHistoryMessage.builder()
                .uid(userId)
                .amount(amount)
                .balance(balance)
                .toUser(toUser)
                .type(TransactionType.DEBIT)
                .timestamp(LocalDateTime.now())
                //todo
                .itemId(null)
                .quantity(null)
                .price(null)
                .note("")
                .build();

    }
}
