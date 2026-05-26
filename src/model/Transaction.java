package model;

import constant.TransactionType;

import java.util.Date;

public class Transaction {
    private String id;
    private Date creationDate;
    private TransactionType type;
    private Double amount;
    private String sourceProductId;
    private String destinationProductId;

    public Transaction(Date creationDate, TransactionType type, Double amount, String sourceProductId, String destinationProductId) {
        this.creationDate = creationDate;
        this.type = type;
        this.amount = amount;
        this.sourceProductId = sourceProductId;
        this.destinationProductId = destinationProductId;
    }

    public Transaction(String id, Date creationDate, TransactionType type, Double amount, String sourceProductId, String destinationProductId) {
        this(creationDate, type, amount, sourceProductId, destinationProductId);
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getSourceProductId() {
        return sourceProductId;
    }

    public void setSourceProductId(String sourceProductId) {
        this.sourceProductId = sourceProductId;
    }

    public String getDestinationProductId() {
        return destinationProductId;
    }

    public void setDestinationProductId(String destinationProductId) {
        this.destinationProductId = destinationProductId;
    }
}
