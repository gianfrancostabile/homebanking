package enums;

public enum PaymentMethod {
    TRANSFER("Transferencia"),
    CREDIT_CARD("Tarjeta de Credito"),
    DEBIT_CARD("Tarjeta de Debito"),
    DEPOSIT("Deposito Bancario"),
    INTEREST("Interes");

    private final String prettyName;

    PaymentMethod(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }
}
