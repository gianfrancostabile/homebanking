package constant;


import java.awt.*;

public enum ButtonVariant {
    CREATE(new Color(40, 167, 69), Color.WHITE, "add.png"),
    UPDATE(new Color(255, 193, 7), new Color(33, 37, 41), "edit.png"),
    DELETE(new Color(220, 53, 69), Color.WHITE, "delete.png"),
    SEARCH(new Color(0, 153, 255, 205), Color.WHITE, "search.png"),
    DEFAULT(new Color(108, 117, 125), Color.WHITE, "default.png");

    private final Color backgroundColor;
    private final Color foregroundColor;
    private final String iconName;

    ButtonVariant(Color backgroundColor, Color foregroundColor, String iconName) {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.iconName = iconName;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public String getIconName() {
        return iconName;
    }
}
