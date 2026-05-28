package model;

import enums.Currency;
import enums.PaymentMethod;
import enums.TransactionType;

import java.util.Date;

public class Transaction {
    private String id;
    private Date creationDate;
    private TransactionType type;
    private PaymentMethod paymentMethod;
    private Currency currency;
    private Double amount;
    private String sourceProductId;
    private String destinationProductId;
    private String cardId;

    public Transaction(Date creationDate, TransactionType type, PaymentMethod paymentMethod, Currency currency, Double amount, String sourceProductId, String destinationProductId, String cardId) {
        this.creationDate = creationDate;
        this.type = type;
        this.paymentMethod = paymentMethod;
        this.currency = currency;
        this.amount = amount;
        this.sourceProductId = sourceProductId;
        this.destinationProductId = destinationProductId;
        this.cardId = cardId;
    }

    public Transaction(String id, Date creationDate, TransactionType type, PaymentMethod paymentMethod, Currency currency, Double amount, String sourceProductId, String destinationProductId, String cardId) {
        this(creationDate, type, paymentMethod, currency, amount, sourceProductId, destinationProductId, cardId);
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
