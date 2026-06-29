package dto;

import enums.TransactionType;

import java.time.LocalDateTime;

public class TransactionFilter {
    private String clientId;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private TransactionType transactionType;

    public TransactionFilter(String clientId, LocalDateTime fromDate, LocalDateTime toDate, TransactionType transactionType) {
        this.clientId = clientId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.transactionType = transactionType;
    }

    public String getClientId() {
        return clientId;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }
}
