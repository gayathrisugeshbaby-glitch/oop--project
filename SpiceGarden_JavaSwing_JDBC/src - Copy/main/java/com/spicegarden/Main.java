package com.spicegarden;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }
            try {
                Database.initialize();
                new CustomerFrame().setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null,
                        "Could not connect to MySQL.\n\n" + ex.getMessage()
                        + "\n\nCheck Database.java (URL, username, password) and make sure MySQL is running.",
                        "Database connection problem", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
