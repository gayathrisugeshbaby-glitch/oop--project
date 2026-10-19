package com.spicegarden;

import com.spicegarden.model.OrderSummary;
import com.spicegarden.repository.OrderRepository;
import com.spicegarden.ui.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StaffDashboardFrame extends JFrame {
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Order #", "Customer", "Phone", "Table", "Items", "Amount", "Status", "Time"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JLabel countLabel = new JLabel("Recent orders");

    public StaffDashboardFrame() throws Exception {
        setTitle("Spice Garden | Staff Dashboard");
        setSize(1250, 650); setMinimumSize(new Dimension(950, 520)); setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.CREAM); setLayout(new BorderLayout(0, 16));
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(Theme.GREEN_DARK);
        header.setBorder(new EmptyBorder(20, 24, 20, 24));
        JLabel title = new JLabel("Order dashboard"); title.setFont(Theme.TITLE); title.setForeground(Color.WHITE);
        JButton refresh = new JButton("↻ Refresh");
        refresh.addActionListener(e -> loadOrders());
        header.add(title, BorderLayout.WEST); header.add(refresh, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(0, 12)); center.setOpaque(false);
        center.setBorder(new EmptyBorder(0, 22, 22, 22));
        countLabel.setFont(Theme.HEADING); countLabel.setForeground(Theme.INK);
        center.add(countLabel, BorderLayout.NORTH);
        table.setRowHeight(34); table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scroll = new JScrollPane(table);
        center.add(scroll, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
        loadOrders();
    }

    private void loadOrders() {
        try {
            List<OrderSummary> orders = new OrderRepository().findRecentOrders();
            model.setRowCount(0);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");
            for (OrderSummary o : orders) {
                model.addRow(new Object[]{
                        "#" + o.getId(), o.getCustomer(), o.getPhone(), o.getTableNumber(),
                        o.getItems(), "₹" + o.getTotal().toPlainString(), o.getStatus(),
                        o.getCreatedAt() == null ? "" : o.getCreatedAt().format(fmt)
                });
            }
            countLabel.setText("Recent orders  ·  " + orders.size() + " order(s)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not load orders:\n" + ex.getMessage(),
                    "Database error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
