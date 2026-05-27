package constant;

public enum ProductType {
    CUENTA_CORRIENTE("Cuenta Corriente", Currency.PESOS),
    CAJA_DE_AHORRO("Caja de Ahorro en Pesos", Currency.PESOS),
    CAJA_DE_AHORRO_DOLARES("Caja de Ahorro en Dolares", Currency.DOLLAR);

    private final String prettyName;
    private final Currency currency;

    ProductType(String prettyName, Currency currency) {
        this.prettyName = prettyName;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return this.prettyName;
    }

    public Currency getCurrency() {
        return currency;
    }
}
