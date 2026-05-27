package view.table;

import constant.CommonConstant;
import model.Transaction;
import view.submenu.TransactionTypeRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionTable extends JPanel {

    private static final String[] COLUMNS = {
            CommonConstant.ID_TABLE_HEADER,
            CommonConstant.DATE_HEADER,
            CommonConstant.TRANSACTION_TYPE_HEADER,
            CommonConstant.PAYMENT_METHOD_HEADER,
            CommonConstant.TRANSACTION_AMOUNT_HEADER,
            CommonConstant.TRANSACTION_ORIGIN_HEADER,
            CommonConstant.TRANSACTION_DESTINATION_HEADER
    };
    private static final int TYPE_COLUMN_INDEX = 2;

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(CommonConstant.TRANSACTION_DATE_FORMAT);

    private final DefaultTableModel tableModel;

    public TransactionTable() {
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

        TableColumn typeColumn = table.getColumnModel().getColumn(TYPE_COLUMN_INDEX);
        typeColumn.setCellRenderer(new TransactionTypeRenderer());
        typeColumn.setPreferredWidth(110);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 250));

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void appendTransaction(Transaction transaction) {
        if (transaction == null) return;

        this.tableModel.addRow(new Object[]{
                transaction.getId(),
                DATE_FORMAT.format(transaction.getCreationDate()),
                transaction.getType(),
                transaction.getPaymentMethod().getPrettyName(),
                transaction.getCurrency().getSign() + transaction.getAmount(),
                transaction.getSourceProductId(),
                transaction.getDestinationProductId()
        });
    }

    public void appendTransactions(List<Transaction> transactions) {
        if (transactions != null) {
            transactions.forEach(this::appendTransaction);
        }
    }
}
