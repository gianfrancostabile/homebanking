package view.custom;

import constant.CommonConstant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.function.BiPredicate;

public abstract class CustomTable<T> extends JPanel {

    private static final Color HEADER_BACKGROUND = new Color(248, 249, 250);
    private static final Color HEADER_FOREGROUND = new Color(73, 80, 87);
    private static final Color SELECTION_BACKGROUND = new Color(231, 241, 255);
    private static final Color SELECTION_FOREGROUND = new Color(0, 123, 255);
    private static final Color GRID_COLOR = new Color(233, 236, 239);
    public static final Color ALTERNATE_ROW_COLOR = new Color(245, 246, 248);

    private final JTable table;
    private final DefaultTableModel tableModel;

    public CustomTable(String[] columns) {
        this(columns, (_, _) -> false);
    }

    public CustomTable(String[] columns, BiPredicate<Integer, Integer> isCellEditable) {
        this.tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return isCellEditable.test(row, column);
            }
        };
        this.table = new JTable(this.tableModel);

        this.initComponents();
    }

    protected void initComponents() {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setRowHeight(CommonConstant.ROW_HEIGHT);
        table.setGridColor(GRID_COLOR);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);
        table.setFont(new Font(CommonConstant.FONT_FAMILY, Font.PLAIN, 13));
        table.setForeground(new Color(33, 37, 41));

        table.setSelectionBackground(SELECTION_BACKGROUND);
        table.setSelectionForeground(SELECTION_FOREGROUND);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setFont(new Font(CommonConstant.FONT_FAMILY, Font.BOLD, 13));
        header.setBackground(HEADER_BACKGROUND);
        header.setForeground(HEADER_FOREGROUND);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, GRID_COLOR)
        );

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val, boolean isSel, boolean hasFoc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, isSel, hasFoc, row, col);

                if (c instanceof JLabel) {
                    ((JLabel) c).setBorder(new EmptyBorder(0, 12, 0, 12));
                }

                if (!isSel) {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(ALTERNATE_ROW_COLOR);
                    }
                    c.setForeground(new Color(33, 37, 41));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(900, 250));

        scrollPane.setBorder(BorderFactory.createLineBorder(GRID_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void append(T data) {
        if (data == null) return;
        this.tableModel.addRow(this.mapToRow(data));
    }

    public void append(List<T> data) {
        if (data != null) {
            data.forEach(this::append);
        }
    }

    public void reAppend(List<T> data) {
        this.clearTable();
        this.append(data);
    }

    public void clearTable() {
        this.tableModel.setRowCount(0);
    }

    protected JTable getTable() {
        return table;
    }

    protected abstract Object[] mapToRow(T data);
}