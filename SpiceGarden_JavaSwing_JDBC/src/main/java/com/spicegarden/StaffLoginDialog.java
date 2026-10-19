package com.spicegarden;

import com.spicegarden.ui.Theme;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class StaffLoginDialog extends JDialog {
    public StaffLoginDialog(JFrame owner) {
        super(owner, "Staff Portal", true);
        setSize(390, 300);
        setLocationRelativeTo(owner);
        JPanel root = new JPanel(new BorderLayout(0, 16));
        root.setBackground(Theme.CREAM); root.setBorder(new EmptyBorder(24, 26, 24, 26));
        JLabel title = new JLabel("Welcome back, team");
        title.setFont(Theme.TITLE); title.setForeground(Theme.INK);
        JLabel subtitle = new JLabel("Sign in to view restaurant orders.");
        subtitle.setForeground(Theme.MUTED);
        JPanel top = new JPanel(new GridLayout(2, 1, 0, 5)); top.setOpaque(false); top.add(title); top.add(subtitle);
        JTextField user = new JTextField("staff");
        JPasswordField pass = new JPasswordField();
        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5)); form.setOpaque(false);
        form.add(new JLabel("Username")); form.add(user); form.add(new JLabel("Password")); form.add(pass);
        JButton login = new JButton("Sign in");
        login.setBackground(Theme.GREEN); login.setForeground(Color.WHITE); login.setFocusPainted(false);
        login.addActionListener(e -> {
            String password = new String(pass.getPassword());
            if ("staff".equals(user.getText().trim()) && "garden123".equals(password)) {
                dispose();
                try { new StaffDashboardFrame().setVisible(true); }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(owner, "Could not load staff orders:\n" + ex.getMessage(),
                            "Staff dashboard error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect username or password.", "Login failed", JOptionPane.WARNING_MESSAGE);
            }
        });
        root.add(top, BorderLayout.NORTH); root.add(form, BorderLayout.CENTER); root.add(login, BorderLayout.SOUTH);
        setContentPane(root);
    }
}
