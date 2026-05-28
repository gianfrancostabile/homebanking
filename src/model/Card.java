package model;

import enums.CardBrand;
import enums.CardType;
import constant.CommonConstant;

import java.util.Date;

public class Card {
    private String id;
    private String productId;
    private CardBrand brand;
    private CardType type;
    private String cardNumber;
    private String securityCode;
    private Date expirationDate;
    private String ownerName;
    private Double availableDebtBalance;
    private Double debtBalance;

    public Card() {
    }

    public Card(String id, String productId, CardBrand brand, CardType type, String cardNumber, String securityCode, Date expirationDate, String ownerName, Double availableDebtBalance, Double debtBalance) {
        this.id = id;
        this.productId = productId;
        this.brand = brand;
        this.type = type;
        this.cardNumber = cardNumber;
        this.securityCode = securityCode;
        this.expirationDate = expirationDate;
        this.ownerName = ownerName;
        this.availableDebtBalance = availableDebtBalance;
        this.debtBalance = debtBalance;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setAvailableDebtBalance(Double availableDebtBalance) {
        this.availableDebtBalance = availableDebtBalance;
    }

    public void setDebtBalance(Double debtBalance) {
        this.debtBalance = debtBalance;
    }

    public String getId() {
        return this.id;
    }

    public String getProductId() {
        return productId;
    }

    public CardBrand getBrand() {
        return brand;
    }

    public CardType getType() {
        return type;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Double getAvailableDebtBalance() {
        return availableDebtBalance;
    }

    public Double getDebtBalance() {
        return debtBalance;
    }

    public void pay(double amount) {
        if (CardType.CREDIT.equals(type)) {
            this.availableDebtBalance = this.availableDebtBalance - amount;
            this.debtBalance = this.debtBalance + amount;
        }
    }

    @Override
    public String toString() {
        return this.brand.name() + " - " + this.type.name() + " - " + this.cardNumber;
    }

    public static class Builder {
        private final Card card;

        public Builder() {
            this.card = new Card();
        }

        public Card.Builder withProductId(String productId) {
            this.card.productId = productId;
            return this;
        }

        public Card.Builder withBrand(CardBrand brand) {
            this.card.brand = brand;
            return this;
        }

        public Card.Builder withType(CardType type) {
            this.card.type = type;
            this.card.availableDebtBalance = CardType.CREDIT.equals(type) ? CommonConstant.MAX_DEBT_BALANCE : 0d;
            this.card.debtBalance = 0d;
            return this;
        }

        public Card.Builder withCardNumber(String cardNumber) {
            this.card.cardNumber = cardNumber;
            return this;
        }

        public Card.Builder withSecurityCode(String securityCode) {
            this.card.securityCode = securityCode;
            return this;
        }

        public Card.Builder withExpirationDate(Date date) {
            this.card.expirationDate = date;
            return this;
        }

        public Card.Builder withOwnerName(String ownerName) {
            this.card.ownerName = ownerName;
            return this;
        }

        public Card build() {
            return this.card;
        }
    }
}
