package constant;

public enum CardBrand {
    VISA("45935400"),
    MASTERCARD("55056850");

    private final String cardNumberPrefix;

    CardBrand(String cardNumberPrefix) {
        this.cardNumberPrefix = cardNumberPrefix;
    }

    public String getCardNumberPrefix() {
        return cardNumberPrefix;
    }
}
