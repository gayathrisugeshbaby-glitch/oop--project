package com.spicegarden.model;

public class BasketItem {
    private final MenuItem item;
    private int quantity;

    public BasketItem(MenuItem item) { this.item = item; this.quantity = 1; }
    public MenuItem getItem() { return item; }
    public int getQuantity() { return quantity; }
    public void increase() { quantity++; }
    public void decrease() { if (quantity > 1) quantity--; }
}
