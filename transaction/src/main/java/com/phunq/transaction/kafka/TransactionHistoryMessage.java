package com.phunq.transaction.kafka;

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
}
