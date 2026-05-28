package model;

import enums.ProductType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Product {
    private String id;
    private String clientId;
    private String alias;
    private String cbu;
    private ProductType type;
    private Double balance;
    private List<Card> cards;
    private Date creationDate;

    public Product() {
    }

    public Product(String clientId, String alias, String cbu, ProductType type, Double balance, List<Card> cards, Date creationDate) {
        this.clientId = clientId;
        this.alias = alias;
        this.cbu = cbu;
        this.type = type;
        this.balance = balance;
        this.cards = cards;
        this.creationDate = creationDate;
    }

    public Product(String id, String clientId, String alias, String cbu, ProductType type, Double balance, List<Card> cards, Date creationDate) {
        this.clientId = clientId;
        this.id = id;
        this.alias = alias;
        this.cbu = cbu;
        this.type = type;
        this.balance = balance;
        this.cards = cards;
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getAlias() {
        return alias;
    }

    public String getCbu() {
        return cbu;
    }

    public ProductType getType() {
        return type;
    }

    public Double getBalance() {
        return balance;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBalance(Double balance) {
        if (balance < 0d) {
            balance = 0d;
        }
        this.balance = balance;
    }

    public void addBalance(Double balance) {
        this.balance = this.balance + balance;
    }

    public void removeBalance(Double balance) {
        this.balance = this.balance - balance;
    }

    @Override
    public String toString() {
        return alias;
    }

    public static class Builder {
        private final Product product;

        public Builder() {
            this.product = new Product();
            this.product.balance = 0d;
            this.product.cards = new ArrayList<>();
            this.product.creationDate = new Date();
        }

        public Builder withClientId(String clientId) {
            this.product.clientId = clientId;
            return this;
        }

        public Builder withAlias(String alias) {
            this.product.alias = alias;
            return this;
        }

        public Builder withCbu(String cbu) {
            this.product.cbu = cbu;
            return this;
        }

        public Builder withProductType(ProductType type) {
            this.product.type = type;
            return this;
        }

        public Builder withCards(List<Card> cards) {
            this.product.cards = cards;
            return this;
        }

        public Product build() {
            return this.product;
        }
    }
}
