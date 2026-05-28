package enums;

public enum CardType {
    DEBIT("Tarjeta de Debito"),
    CREDIT("Tarjeta de Credito");

    private final String prettyName;

    CardType(String prettyName) {
        this.prettyName = prettyName;
    }

    public String getPrettyName() {
        return prettyName;
    }
}
