package view;

import constant.TitleConstant;
import view.overview.ClientOverview;

import javax.swing.*;

public class Window extends JFrame {

    public Window() {
        super(TitleConstant.APPLICATION_TITLE);
        this.setupFrame();
        this.initComponents();
    }

    private void setupFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        this.add(new ClientOverview());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}