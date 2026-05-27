package constant;

public enum TransactionType {
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
}
