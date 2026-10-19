package com.spicegarden.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class ImageLoader {
    private static final Map<String, ImageIcon> CACHE = new ConcurrentHashMap<>();
    private ImageLoader() {}

    public static ImageIcon load(String url, int width, int height) {
        String key = url + "|" + width + "|" + height;
        ImageIcon cached = CACHE.get(key);
        if (cached != null) return cached;
        try {
            BufferedImage original = ImageIO.read(new URL(url));
            if (original != null) {
                Image scaled = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaled);
                CACHE.put(key, icon);
                return icon;
            }
        } catch (Exception ignored) { }
        BufferedImage placeholder = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(new Color(235, 231, 217)); g.fillRect(0, 0, width, height);
        g.setColor(new Color(72, 95, 68)); g.setFont(new Font("SansSerif", Font.BOLD, 32));
        g.drawString("SG", Math.max(8, width / 2 - 20), height / 2 + 10);
        g.dispose();
        return new ImageIcon(placeholder);
    }
}
