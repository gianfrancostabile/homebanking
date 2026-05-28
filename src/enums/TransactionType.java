package enums;

public enum TransactionType {
    NONE("Todos"),
    DEBIT("Debito"),
    CHARGE("Recibido"),
    TO_PAY("A pagar");

    private final String prettyName;

    TransactionType(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }

    @Override
    public String toString() {
        return prettyName;
    }
}
