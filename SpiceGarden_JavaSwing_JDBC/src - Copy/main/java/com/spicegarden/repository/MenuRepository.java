package com.spicegarden.repository;

import com.spicegarden.model.MenuItem;
import java.math.BigDecimal;
import java.util.List;

public class MenuRepository {
    private static final String PHOTO = "https://images.unsplash.com/";
    private static MenuItem item(String name, String category, String desc, String price, String photo) {
        return new MenuItem(name, category, desc, new BigDecimal(price), PHOTO + photo);
    }

    public List<MenuItem> findAll() {
        return List.of(
            item("Crispy Paneer Bites","Starters","Golden, spiced paneer with mint dip","189","photo-1601050690597-df0568f70950?w=700&q=85"),
            item("Garden Spring Rolls","Starters","Crunchy rolls with fresh vegetables","159","photo-1544025162-d76694265947?w=700&q=85"),
            item("Masala Fries","Starters","Crispy fries tossed with house masala","129","photo-1573080496219-bb080dd4f877?w=700&q=85"),
            item("Butter Paneer Masala","Main Course","Creamy tomato gravy and soft paneer","289","photo-1631452180519-c014fe946bc7?w=700&q=85"),
            item("Veg Dum Biryani","Main Course","Fragrant rice, herbs and garden vegetables","249","photo-1563379091339-03246963d96c?w=700&q=85"),
            item("Garden Special Thali","Main Course","A hearty platter of Indian favourites","329","photo-1546833999-b9f581a1996d?w=700&q=85"),
            item("Chocolate Brownie","Desserts","Warm chocolate brownie delight","149","photo-1606313564200-e75d5e30476c?w=700&q=85"),
            item("Gulab Jamun","Desserts","Soft milk dumplings in cardamom syrup","99","photo-1666190094763-1d7f8d5e7c9f?w=700&q=85"),
            item("Mango Smoothie","Juices","Cool mango blended to creamy perfection","139","photo-1622597467836-f3285f2131b8?w=700&q=85"),
            item("Fresh Lime Cooler","Juices","Refreshing lime with a mint finish","89","photo-1513558161293-cdad8edfa0c5?w=700&q=85"),
            item("Iced Coffee","Refreshments","Chilled coffee topped with a silky foam","129","photo-1461023058943-07fcbe16d735?w=700&q=85"),
            item("Mint Lemonade","Refreshments","Fresh mint and lemon, served cold","99","photo-1513558161293-cdad8edfa0c5?w=700&q=85")
        );
    }
}
