# Spice Garden — Java Swing + JDBC + MySQL

A desktop restaurant ordering demo built with Java 17, Swing, Maven, JDBC, and MySQL.

## Features
- Separate menu categories: Starters, Main Course, Desserts, Juices, Refreshments.
- Food cards with photos, item names, descriptions, prices, and Add buttons.
- Basket with quantity increase/decrease, item removal, and live total.
- Customer name, phone, and table number at checkout.
- Places the order and stores customer/order/order-item details in MySQL.
- Staff login and dashboard showing recent orders, customer details, table number, ordered items, and amount.
- Object-oriented structure: separate model, database, repository, UI, and utility classes.
- Database tables are created automatically on first successful connection.

## Requirements
- JDK 17+
- IntelliJ IDEA (Community edition is fine)
- MySQL Server 8+
- Internet connection the first time Maven downloads the JDBC driver and when food photos load.

## 1. Create the database
Open MySQL Command Line Client and run:

```sql
CREATE DATABASE IF NOT EXISTS spice_garden;
```

Then exit MySQL with `exit;`.

## 2. Configure the database password
Open `src/main/java/com/spicegarden/Database.java`.

Default settings:
- URL: `jdbc:mysql://localhost:3306/spice_garden?serverTimezone=UTC`
- Username: `root`
- Password: blank

Change `DB_PASSWORD` to your own local MySQL password. Do not share your password. For a safer setup, you can instead set environment variables `SPICE_DB_URL`, `SPICE_DB_USER`, and `SPICE_DB_PASSWORD`.

## 3. Open in IntelliJ
1. Extract this ZIP to a normal folder.
2. IntelliJ → **Open** → choose the extracted `SpiceGarden_JavaSwing_JDBC` folder (the one containing `pom.xml`).
3. If asked, choose **Trust Project** and **Load Maven Project**.
4. Set Project SDK to JDK 17: File → Project Structure → Project SDK.
5. Wait for Maven to finish downloading dependencies.
6. Open `src/main/java/com/spicegarden/Main.java`, then click the green Run triangle.

## 4. Staff login
Demo staff credentials:
- Username: `staff`
- Password: `garden123`

Change these demo credentials in `StaffLoginDialog.java` before using this for any real business. This is a learning/demo project, not a production payment system.

## Food photos
Food photos are loaded from Unsplash URLs at runtime, so the app needs internet access to display them. If a photo cannot load, a styled placeholder is shown. You can change each photo URL in `MenuRepository.java`.

## Notes
- This project stores orders only; it does not process real payments.
- Customer phone number is optional.
- The first run creates tables `customers`, `orders`, and `order_items`.
