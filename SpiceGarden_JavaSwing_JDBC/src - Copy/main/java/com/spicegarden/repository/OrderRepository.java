package com.spicegarden.repository;

import com.spicegarden.Database;
import com.spicegarden.model.BasketItem;
import com.spicegarden.model.OrderSummary;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    public long placeOrder(String customerName, String phone, int tableNumber, List<BasketItem> basket) throws SQLException {
        if (basket.isEmpty()) throw new SQLException("Your basket is empty.");
        try (Connection c = Database.getConnection()) {
            c.setAutoCommit(false);
            try {
                long customerId;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO customers(name, phone) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, customerName.trim());
                    ps.setString(2, phone == null ? "" : phone.trim());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Could not save customer details.");
                        customerId = keys.getLong(1);
                    }
                }

                BigDecimal total = BigDecimal.ZERO;
                for (BasketItem bi : basket) {
                    total = total.add(bi.getItem().getPrice().multiply(BigDecimal.valueOf(bi.getQuantity())));
                }

                long orderId;
                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO orders(customer_id, table_number, total,payment_method) VALUES (?, ?, ?,?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, customerId);
                    ps.setInt(2, tableNumber);
                    ps.setBigDecimal(3, total);
                    ps.setString(4,"cash");
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Could not save order.");
                        orderId = keys.getLong(1);
                    }
                }

                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO order_items(order_id, item_name, quantity, unit_price) VALUES (?, ?, ?, ?)")) {
                    for (BasketItem bi : basket) {
                        ps.setLong(1, orderId);
                        ps.setString(2, bi.getItem().getName());
                        ps.setInt(3, bi.getQuantity());
                        ps.setBigDecimal(4, bi.getItem().getPrice());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                c.commit();
                return orderId;
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<OrderSummary> findRecentOrders() throws SQLException {
        String sql = """
            SELECT o.id, c.name, c.phone, o.table_number, o.total, o.status, o.created_at,
                   COALESCE(GROUP_CONCAT(CONCAT(oi.item_name, ' x', oi.quantity) ORDER BY oi.id SEPARATOR ', '), '') AS items
            FROM orders o
            JOIN customers c ON c.id = o.customer_id
            LEFT JOIN order_items oi ON oi.order_id = o.id
            GROUP BY o.id, c.name, c.phone, o.table_number, o.total, o.status, o.created_at
            ORDER BY o.created_at DESC, o.id DESC
            LIMIT 200
            """;
        List<OrderSummary> result = new ArrayList<>();
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("created_at");
                result.add(new OrderSummary(rs.getLong("id"), rs.getString("name"), rs.getString("phone"),
                        rs.getInt("table_number"), rs.getBigDecimal("total"), rs.getString("status"),
                        ts == null ? null : ts.toLocalDateTime(), rs.getString("items")));
            }
        }
        return result;
    }
}
