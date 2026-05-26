package constant;

public enum ProductType {
    CUENTA_CORRIENTE("Cuenta Corriente", "$"),
    CAJA_DE_AHORRO("Caja de Ahorro en Pesos", "$"),
    CAJA_DE_AHORRO_DOLARES("Caja de Ahorro en Dolares", "U$D");

    private final String prettyName;
    private final String currency;

    ProductType(String prettyName, String currency) {
        this.prettyName = prettyName;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return this.prettyName;
    }

    public String getCurrency() {
        return currency;
    }
}
