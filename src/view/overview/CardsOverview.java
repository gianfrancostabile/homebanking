package view.overview;

import constant.TitleConstant;
import model.Card;
import view.table.CardTable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CardsOverview extends JFrame {

    public CardsOverview(List<Card> cards) {
        this.setupFrame();
        this.initComponents(cards);
    }

    private void setupFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setTitle(TitleConstant.PRODUCT_CARDS_FORM);
    }

    private void initComponents(List<Card> cards) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        panel.add(new JLabel(TitleConstant.PRODUCT_CARDS_FORM, SwingConstants.CENTER), BorderLayout.NORTH);
        panel.add(this.buildCenter(cards), BorderLayout.CENTER);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JPanel buildCenter(List<Card> cards) {
        JPanel centerPanel = new JPanel(new BorderLayout());

        CardTable table = new CardTable();
        table.append(cards);

        centerPanel.add(table, BorderLayout.CENTER);
        return centerPanel;
    }
}