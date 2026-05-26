package view;

import constant.TitleConstant;

import javax.swing.*;

public class Window extends JFrame {

    public Window() {
        super(TitleConstant.APPLICATION_TITLE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.add(new ClientOverview());

        this.pack();
        this.setVisible(true);
    }
}
