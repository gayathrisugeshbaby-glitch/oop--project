package com.spicegarden.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSummary {
    private final long id;
    private final String customer;
    private final String phone;
    private final int tableNumber;
    private final BigDecimal total;
    private final String status;
    private final LocalDateTime createdAt;
    private final String items;

    public OrderSummary(long id, String customer, String phone, int tableNumber, BigDecimal total,
                        String status, LocalDateTime createdAt, String items) {
        this.id = id; this.customer = customer; this.phone = phone; this.tableNumber = tableNumber;
        this.total = total; this.status = status; this.createdAt = createdAt; this.items = items;
    }
    public long getId() { return id; }
    public String getCustomer() { return customer; }
    public String getPhone() { return phone; }
    public int getTableNumber() { return tableNumber; }
    public BigDecimal getTotal() { return total; }
    public String getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getItems() { return items; }
}
