package view.table;

import constant.CommonConstant;
import model.Client;
import view.submenu.SubMenuClientEditor;
import view.submenu.SubMenuClientRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class ClientTable extends JPanel {

    private static final String[] COLUMNS = {
            CommonConstant.ID_TABLE_HEADER,
            CommonConstant.NAME_TABLE_HEADER,
            CommonConstant.LASTNAME_TABLE_HEADER,
            CommonConstant.ACTIONS_TABLE_HEADER
    };

    private static final int ID_COLUMN_INDEX = 0;
    private static final int ACTIONS_COLUMN_INDEX = 3;
    private static final int ACTIONS_COLUMN_PREFERRED_WIDTH = 200;
    private static final int NOT_FOUND_ROW = -1;

    private final DefaultTableModel tableModel;

    public ClientTable() {
        this.tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == ACTIONS_COLUMN_INDEX;
            }
        };

        this.initComponents();
    }

    private void initComponents() {
        JTable table = new JTable(this.tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(CommonConstant.ROW_HEIGHT);

        TableColumn actionColumn = table.getColumnModel().getColumn(ACTIONS_COLUMN_INDEX);
        actionColumn.setPreferredWidth(ACTIONS_COLUMN_PREFERRED_WIDTH);
        actionColumn.setCellRenderer(new SubMenuClientRenderer());
        actionColumn.setCellEditor(new SubMenuClientEditor(this::updateClient, this::deleteClient));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 200));

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void appendPerson(Client client) {
        if (client == null) return;

        this.tableModel.addRow(new Object[]{
                client.getId(),
                client.getName(),
                client.getLastName(),
                client
        });
    }

    public void appendPeople(List<Client> people) {
        if (people != null) {
            people.forEach(this::appendPerson);
        }
    }

    public void updateClient(Client client) {
        if (client == null) return;

        int row = this.findRowById(client.getId());
        if (row == NOT_FOUND_ROW) {
            return;
        }

        this.tableModel.setValueAt(client.getId(), row, 0);
        this.tableModel.setValueAt(client.getName(), row, 1);
        this.tableModel.setValueAt(client.getLastName(), row, 2);
        this.tableModel.setValueAt(client, row, ACTIONS_COLUMN_INDEX);
    }

    public void deleteClient(String clientId) {
        if (clientId == null) return;

        int row = this.findRowById(clientId);
        if (row == NOT_FOUND_ROW) {
            return;
        }

        this.tableModel.removeRow(row);
    }

    private int findRowById(String clientId) {
        for (int i = 0; i < this.tableModel.getRowCount(); i++) {
            Object value = this.tableModel.getValueAt(i, ID_COLUMN_INDEX);
            if (value != null && value.equals(clientId)) {
                return i;
            }
        }
        return NOT_FOUND_ROW;
    }
}