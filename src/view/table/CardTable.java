package view.table;

import constant.CommonConstant;
import constant.TableHeaderConstant;
import model.Card;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CardTable extends JPanel {

    private static final String[] COLUMNS = {
            TableHeaderConstant.BRAND,
            TableHeaderConstant.TYPE,
            TableHeaderConstant.NUMBER,
            TableHeaderConstant.SECURITY_CODE,
            TableHeaderConstant.EXPIRATION_DATE,
            TableHeaderConstant.OWNER_NAME
    };

    private static final DateFormat EXPIRATION_DATE_FORMAT = new SimpleDateFormat(CommonConstant.CARD_EXPIRATION_DATE_FORMAT);
    private static final int SECURITY_CODE_COLUMN_INDEX = 3;
    private static final int PREFERRED_SECURITY_COLUMN_WIDTH = 125;

    private final DefaultTableModel tableModel;

    public CardTable() {
        this.tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        this.initComponents();
    }

    private void initComponents() {
        JTable table = new JTable(this.tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(CommonConstant.ROW_HEIGHT);

        TableColumn securityColumn = table.getColumnModel().getColumn(SECURITY_CODE_COLUMN_INDEX);
        securityColumn.setPreferredWidth(PREFERRED_SECURITY_COLUMN_WIDTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 200));

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void appendCard(Card card) {
        if (card == null) return;

        this.tableModel.addRow(new Object[]{
                card.getBrand().name(),
                card.getType().getPrettyName(),
                card.getCardNumber(),
                card.getSecurityCode(),
                EXPIRATION_DATE_FORMAT.format(card.getExpirationDate()),
                card.getOwnerName()
        });
    }

    public void appendCards(List<Card> cards) {
        if (cards != null) {
            cards.forEach(this::appendCard);
        }
    }
}