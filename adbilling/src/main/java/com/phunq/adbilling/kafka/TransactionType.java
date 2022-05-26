package com.phunq.adbilling.kafka;

public enum TransactionType {
    CREDIT("CREDIT"), DEBIT("DEBIT");

    private final  String description;

    TransactionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
