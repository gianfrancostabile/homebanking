package constant;

public enum ProductType {
    CUENTA_CORRIENTE("Cuenta Corriente", Currency.PESOS, 1d),
    CAJA_DE_AHORRO("Caja de Ahorro en Pesos", Currency.PESOS, 1.1d),
    CAJA_DE_AHORRO_DOLARES("Caja de Ahorro en Dolares", Currency.DOLLAR, 1.01d);

    private final String prettyName;
    private final Currency currency;
    private final double interest;

    ProductType(String prettyName, Currency currency, double interest) {
        this.prettyName = prettyName;
        this.currency = currency;
        this.interest = interest;
    }

    @Override
    public String toString() {
        return this.prettyName;
    }

    public Currency getCurrency() {
        return currency;
    }

    public double getInterest() {
        return interest;
    }
}
