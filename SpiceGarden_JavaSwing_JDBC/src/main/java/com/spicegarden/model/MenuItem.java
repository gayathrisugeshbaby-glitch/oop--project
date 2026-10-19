package com.spicegarden.model;

import java.math.BigDecimal;

public class MenuItem {
    private final String name;
    private final String category;
    private final String description;
    private final BigDecimal price;
    private final String imageUrl;

    public MenuItem(String name, String category, String description, BigDecimal price, String imageUrl) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}
