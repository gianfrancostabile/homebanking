package util;

import constant.FeedbackConstant;

import javax.swing.*;
import java.awt.*;

public class Dialog {

    public static void showError(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message, FeedbackConstant.ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }

    public static void showWarning(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message, FeedbackConstant.INVALID_FIELD, JOptionPane.WARNING_MESSAGE);
    }

    public static void showSuccess(Component parentComponent, String message) {
        JOptionPane.showMessageDialog(parentComponent, message, FeedbackConstant.SUCCESS_TITLE, JOptionPane.INFORMATION_MESSAGE);
    }
}
