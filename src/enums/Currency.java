package enums;

public enum Currency {
    PESOS("$"),
    DOLLAR("U$D");

    private final String sign;

    Currency(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}
