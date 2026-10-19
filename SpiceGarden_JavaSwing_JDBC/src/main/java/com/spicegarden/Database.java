package com.spicegarden;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class Database {
    private Database() { }

    public static final String DB_URL = System.getenv().getOrDefault(
            "SPICE_DB_URL", "jdbc:mysql://localhost:3306/spice_garden?serverTimezone=UTC");
    public static final String DB_USER = System.getenv().getOrDefault("SPICE_DB_USER", "root");
    public static final String DB_PASSWORD = System.getenv().getOrDefault("SPICE_DB_PASSWORD", "gayathri@2720");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void initialize() throws SQLException {
        try (Connection connection = getConnection(); Statement st = connection.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS customers (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(120) NOT NULL,
                    phone VARCHAR(40),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
            """);
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS orders (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    customer_id BIGINT NOT NULL,
                    table_number INT NOT NULL,
                    total_amount DECIMAL(10,2) NOT NULL,
                    status VARCHAR(30) NOT NULL DEFAULT 'PLACED',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers(id)
                )
            """);
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS order_items (
                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                    order_id BIGINT NOT NULL,
                    item_name VARCHAR(120) NOT NULL,
                    quantity INT NOT NULL,
                    unit_price DECIMAL(10,2) NOT NULL,
                    CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
                )
            """);
        }
    }
}
