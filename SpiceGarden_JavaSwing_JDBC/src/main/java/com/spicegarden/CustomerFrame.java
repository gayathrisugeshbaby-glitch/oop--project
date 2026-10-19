package com.spicegarden;

import com.spicegarden.model.BasketItem;
import com.spicegarden.model.MenuItem;
import com.spicegarden.repository.MenuRepository;
import com.spicegarden.repository.OrderRepository;
import com.spicegarden.ui.ImageLoader;
import com.spicegarden.ui.Theme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerFrame extends JFrame {
    private final List<MenuItem> menu = new MenuRepository().findAll();
    private final LinkedHashMap<String, BasketItem> basket = new LinkedHashMap<>();
    private final JPanel menuGrid = new JPanel(new GridLayout(0, 3, 16, 16));
    private final JPanel basketRows = new JPanel();
    private final JLabel totalLabel = new JLabel();
    private final JLabel basketCount = new JLabel("0 items");
    private String activeCategory = "Starters";
    private final NumberFormat money = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));

    public CustomerFrame() {
        setTitle("Spice Garden | Restaurant Ordering");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setSize(1360, 850);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.CREAM);
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildMain(), BorderLayout.CENTER);
        renderMenu();
        refreshBasket();
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(Theme.GREEN_DARK);
        header.setBorder(new EmptyBorder(18, 28, 18, 28));
        JPanel brand = new JPanel(new GridLayout(2, 1));
        brand.setOpaque(false);
        JLabel title = new JLabel("SPICE GARDEN");
        title.setForeground(Color.WHITE); title.setFont(new Font("Serif", Font.BOLD, 27));
        JLabel sub = new JLabel("FRESH FLAVOURS · MADE WITH LOVE");
        sub.setForeground(new Color(221, 226, 211)); sub.setFont(new Font("SansSerif", Font.PLAIN, 10));
        brand.add(title); brand.add(sub);
        JButton staff = new JButton("Staff Portal  ↗");
        styleButton(staff, Theme.GOLD, Theme.INK);
        staff.addActionListener(e -> new StaffLoginDialog(this).setVisible(true));
        header.add(brand, BorderLayout.WEST);
        header.add(staff, BorderLayout.EAST);
        return header;
    }

    private JComponent buildMain() {
        JPanel main = new JPanel(new BorderLayout(18, 0));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(22, 24, 22, 24));

        JPanel left = new JPanel(new BorderLayout(0, 16));
        left.setOpaque(false);
        JPanel intro = new JPanel(new BorderLayout());
        intro.setOpaque(false);
        JLabel title = new JLabel("What are you craving today?");
        title.setFont(Theme.TITLE); title.setForeground(Theme.INK);
        JLabel sub = new JLabel("Explore our kitchen, pick your favourites, and build your perfect meal.");
        sub.setForeground(Theme.MUTED);
        JPanel introText = new JPanel(new GridLayout(2, 1, 0, 5)); introText.setOpaque(false);
        introText.add(title); introText.add(sub);
        intro.add(introText, BorderLayout.CENTER);
        left.add(intro, BorderLayout.NORTH);

        JPanel menuArea = new JPanel(new BorderLayout(0, 15)); menuArea.setOpaque(false);
        JPanel categories = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0)); categories.setOpaque(false);
        String[] cats = {"Starters", "Main Course", "Desserts", "Juices", "Refreshments"};
        for (String cat : cats) {
            JButton b = new JButton(cat);
            styleButton(b, cat.equals(activeCategory) ? Theme.GREEN : Color.WHITE,
                    cat.equals(activeCategory) ? Color.WHITE : Theme.INK);
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(225, 224, 214)), new EmptyBorder(9, 14, 9, 14)));
            b.addActionListener(e -> { activeCategory = cat; updateCategoryButtons(categories, cats); renderMenu(); });
            categories.add(b);
        }
        menuArea.add(categories, BorderLayout.NORTH);
        menuGrid.setOpaque(false);
        menuGrid.setBorder(new EmptyBorder(2, 2, 2, 2));
        JScrollPane scroll = new JScrollPane(menuGrid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setOpaque(false); scroll.setOpaque(false);
        menuArea.add(scroll, BorderLayout.CENTER);
        left.add(menuArea, BorderLayout.CENTER);

        JPanel right = buildBasketPanel();
        right.setPreferredSize(new Dimension(350, 10));
        main.add(left, BorderLayout.CENTER);
        main.add(right, BorderLayout.EAST);
        return main;
    }

    private void updateCategoryButtons(JPanel categories, String[] cats) {
        categories.removeAll();
        for (String cat : cats) {
            JButton b = new JButton(cat);
            styleButton(b, cat.equals(activeCategory) ? Theme.GREEN : Color.WHITE,
                    cat.equals(activeCategory) ? Color.WHITE : Theme.INK);
            b.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(225, 224, 214)), new EmptyBorder(9, 14, 9, 14)));
            b.addActionListener(e -> { activeCategory = cat; updateCategoryButtons(categories, cats); renderMenu(); });
            categories.add(b);
        }
        categories.revalidate(); categories.repaint();
    }

    private JPanel buildBasketPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 228, 218)),
                new EmptyBorder(18, 18, 18, 18)));
        JPanel top = new JPanel(new BorderLayout()); top.setOpaque(false);
        JLabel title = new JLabel("Your basket"); title.setFont(Theme.HEADING); title.setForeground(Theme.INK);
        basketCount.setForeground(Theme.MUTED);
        top.add(title, BorderLayout.WEST); top.add(basketCount, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        basketRows.setLayout(new BoxLayout(basketRows, BoxLayout.Y_AXIS));
        basketRows.setBackground(Color.WHITE);
        JScrollPane rowsScroll = new JScrollPane(basketRows);
        rowsScroll.setBorder(BorderFactory.createEmptyBorder());
        rowsScroll.getViewport().setBackground(Color.WHITE);
        panel.add(rowsScroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(0, 12)); bottom.setOpaque(false);
        JPanel totalRow = new JPanel(new BorderLayout()); totalRow.setOpaque(false);
        JLabel totalText = new JLabel("Total amount"); totalText.setForeground(Theme.MUTED);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 24)); totalLabel.setForeground(Theme.GREEN);
        totalRow.add(totalText, BorderLayout.WEST); totalRow.add(totalLabel, BorderLayout.EAST);
        JButton checkout = new JButton("Continue to checkout  →");
        styleButton(checkout, Theme.GREEN, Color.WHITE);
        checkout.setPreferredSize(new Dimension(280, 46));
        checkout.addActionListener(e -> checkout());
        bottom.add(totalRow, BorderLayout.NORTH); bottom.add(checkout, BorderLayout.SOUTH);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void renderMenu() {
        menuGrid.removeAll();
        List<MenuItem> filtered = menu.stream().filter(m -> m.getCategory().equals(activeCategory)).collect(Collectors.toList());
        for (MenuItem item : filtered) menuGrid.add(createFoodCard(item));
        menuGrid.revalidate(); menuGrid.repaint();
    }

    private JPanel createFoodCard(MenuItem item) {
        JPanel card = new JPanel(new BorderLayout(0, 0));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(230, 228, 218)));
        JLabel photo = new JLabel(ImageLoader.load(item.getImageUrl(), 230, 132));
        photo.setPreferredSize(new Dimension(230, 132));
        card.add(photo, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(0, 8)); body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(12, 13, 13, 13));
        JLabel name = new JLabel(item.getName()); name.setFont(new Font("SansSerif", Font.BOLD, 15)); name.setForeground(Theme.INK);
        JTextArea desc = new JTextArea(item.getDescription()); desc.setLineWrap(true); desc.setWrapStyleWord(true);
        desc.setEditable(false); desc.setFocusable(false); desc.setOpaque(false); desc.setForeground(Theme.MUTED);
        desc.setFont(new Font("SansSerif", Font.PLAIN, 12)); desc.setRows(2);
        JLabel price = new JLabel(money.format(item.getPrice())); price.setFont(new Font("SansSerif", Font.BOLD, 16)); price.setForeground(Theme.GREEN);
        JButton add = new JButton("+ Add");
        styleButton(add, Theme.GREEN, Color.WHITE);
        add.addActionListener(e -> {
            BasketItem existing = basket.get(item.getName());
            if (existing == null) basket.put(item.getName(), new BasketItem(item)); else existing.increase();
            refreshBasket();
        });
        JPanel foot = new JPanel(new BorderLayout()); foot.setOpaque(false);
        foot.add(price, BorderLayout.WEST); foot.add(add, BorderLayout.EAST);
        body.add(name, BorderLayout.NORTH); body.add(desc, BorderLayout.CENTER); body.add(foot, BorderLayout.SOUTH);
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    private void refreshBasket() {
        basketRows.removeAll();
        int count = basket.values().stream().mapToInt(BasketItem::getQuantity).sum();
        basketCount.setText(count + (count == 1 ? " item" : " items"));
        BigDecimal total = BigDecimal.ZERO;
        for (BasketItem bi : basket.values()) {
            total = total.add(bi.getItem().getPrice().multiply(BigDecimal.valueOf(bi.getQuantity())));
            basketRows.add(basketRow(bi));
            basketRows.add(Box.createVerticalStrut(10));
        }
        if (basket.isEmpty()) {
            JLabel empty = new JLabel("<html><div style='text-align:center;width:230px;padding:28px'>Your basket is waiting.<br><br>Add something delicious from the menu.</div></html>");
            empty.setForeground(Theme.MUTED); empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            basketRows.add(Box.createVerticalGlue()); basketRows.add(empty); basketRows.add(Box.createVerticalGlue());
        }
        totalLabel.setText(money.format(total));
        basketRows.revalidate(); basketRows.repaint();
    }

    private JPanel basketRow(BasketItem bi) {
        JPanel row = new JPanel(new BorderLayout(8, 6)); row.setMaximumSize(new Dimension(400, 78)); row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(239, 237, 229)));
        JPanel info = new JPanel(new GridLayout(2, 1)); info.setOpaque(false);
        JLabel name = new JLabel(bi.getItem().getName()); name.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel price = new JLabel(money.format(bi.getItem().getPrice().multiply(BigDecimal.valueOf(bi.getQuantity()))));
        price.setForeground(Theme.GREEN); info.add(name); info.add(price);
        JPanel qty = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 12)); qty.setOpaque(false);
        JButton minus = new JButton("−"), plus = new JButton("+"), remove = new JButton("×");
        for (JButton b : new JButton[]{minus, plus, remove}) { b.setMargin(new Insets(2, 7, 2, 7)); }
        minus.addActionListener(e -> { bi.decrease(); refreshBasket(); });
        plus.addActionListener(e -> { bi.increase(); refreshBasket(); });
        remove.addActionListener(e -> { basket.remove(bi.getItem().getName()); refreshBasket(); });
        qty.add(minus); qty.add(new JLabel(String.valueOf(bi.getQuantity()))); qty.add(plus); qty.add(remove);
        row.add(info, BorderLayout.CENTER); row.add(qty, BorderLayout.EAST);
        return row;
    }

    private void checkout() {
        if (basket.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one item to your basket first.", "Basket is empty", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JSpinner table = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.add(new JLabel("Customer name *")); form.add(name);
        form.add(new JLabel("Phone number (optional)")); form.add(phone);
        form.add(new JLabel("Table number *")); form.add(table);
        int answer = JOptionPane.showConfirmDialog(this, form, "Checkout — customer details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (answer != JOptionPane.OK_OPTION) return;
        if (name.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the customer name."); return;
        }
        try {
            long orderId = new OrderRepository().placeOrder(name.getText(), phone.getText(),
                    (Integer) table.getValue(), new ArrayList<>(basket.values()));
            BigDecimal total = basket.values().stream().map(b -> b.getItem().getPrice().multiply(BigDecimal.valueOf(b.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            JOptionPane.showMessageDialog(this, "Order placed successfully!\nOrder number: #" + orderId
                    + "\nTotal: " + money.format(total) + "\nThank you for choosing Spice Garden.",
                    "Order confirmed", JOptionPane.INFORMATION_MESSAGE);
            basket.clear(); refreshBasket();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not place order:\n" + ex.getMessage(),
                    "Order error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background); button.setForeground(foreground);
        button.setFocusPainted(false); button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setBorder(new EmptyBorder(9, 13, 9, 13)); button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
