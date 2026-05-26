package view;

import constant.CommonConstant;
import model.Card;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CardTable extends JPanel {

    private static final String[] COLUMNS = {
            CommonConstant.CARD_BRAND_HEADER,
            CommonConstant.CARD_TYPE_HEADER,
            CommonConstant.CARD_NUMBER_HEADER,
            CommonConstant.CARD_SECURITY_CODE_HEADER,
            CommonConstant.CARD_EXPIRATION_DATE_HEADER,
            CommonConstant.CARD_OWNER_NAME_HEADER
    };
    private static final DateFormat EXPIRATION_DATE_FORMAT = new SimpleDateFormat("MM/yy");

    private final DefaultTableModel tableModel;

    public CardTable() {
        this.tableModel = new DefaultTableModel(COLUMNS, 0);

        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(27);
        TableColumn actionColumn = table.getColumnModel().getColumn(3);
        actionColumn.setPreferredWidth(125);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 200));
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void appendCard(Card card) {
        tableModel.addRow(new Object[]{
            card.getBrand().name(),
            card.getType().name(),
            card.getCardNumber(),
            card.getSecurityCode(),
            EXPIRATION_DATE_FORMAT.format(card.getExpirationDate()),
            card.getOwnerName()
        });
    }

    public void appendCards(List<Card> cards) {
        cards.forEach(this::appendCard);
    }
}
