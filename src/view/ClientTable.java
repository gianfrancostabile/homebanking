package view;

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

    private static final String[] COLUMNS = {CommonConstant.ID_TABLE_HEADER, CommonConstant.NAME_TABLE_HEADER, CommonConstant.LASTNAME_TABLE_HEADER, CommonConstant.ACTIONS_TABLE_HEADER};

    private final DefaultTableModel tableModel;

    public ClientTable() {
        this.tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        JTable table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(27);
        TableColumn actionColumn = table.getColumnModel().getColumn(3);
        actionColumn.setPreferredWidth(200);
        actionColumn.setCellRenderer(new SubMenuClientRenderer());
        actionColumn.setCellEditor(new SubMenuClientEditor(this::updateClient, this::deleteClient));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void appendPerson(Client client) {
        tableModel.addRow(new Object[]{client.getId(), client.getName(), client.getLastName(), client});
    }

    public void appendPeople(List<Client> people) {
        people.forEach(this::appendPerson);
    }

    public void updateClient(Client client) {
        int row = findRowById(client.getId());
        if (row == -1) {
            return;
        }
        tableModel.setValueAt(client.getId(), row, 0);
        tableModel.setValueAt(client.getName(), row, 1);
        tableModel.setValueAt(client.getLastName(), row, 2);
        tableModel.setValueAt(client, row, 3);
    }

    public void deleteClient(String clientId) {
        int row = findRowById(clientId);
        if (row == -1) {
            return;
        }
        tableModel.removeRow(row);
    }

    private int findRowById(String clientId) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).equals(clientId)) {
                return i;
            }
        }
        return -1;
    }
}
