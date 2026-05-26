package view;

import constant.TitleConstant;
import model.Card;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CardsOverview extends JFrame {

    public CardsOverview(List<Card> cards) {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(TitleConstant.PRODUCT_CARDS_FORM), BorderLayout.NORTH);
        panel.add(this.buildCenter(cards), BorderLayout.CENTER);

        this.add(panel);
        this.pack();
        this.setVisible(true);
    }

    private JPanel buildCenter(List<Card> cards) {
        JPanel panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);

        CardTable table = new CardTable();
        table.appendCards(cards);
        panel.add(table);
        return panel;
    }
}

