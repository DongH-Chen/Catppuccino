package dev.cdh;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public final class Utils {
    public static final String PROJECT_NAME = "Catppuccino";
//    private static final Map<String, BufferedImage> FLIP_CACHE = new HashMap<>();
//
//    public static BufferedImage flipImageDirect(BufferedImage img, String cacheKey) {
//        if (cacheKey == null) return flipImageDirect(img);
//        return FLIP_CACHE.computeIfAbsent(cacheKey, _ -> flipImageDirect(img));
//    }

    public static BufferedImage flipImageDirect(BufferedImage img) {
        BufferedImage bImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bImage.createGraphics();
        g2d.drawImage(img, img.getWidth(), 0, -img.getWidth(), img.getHeight(), null);
        g2d.dispose();
        return bImage;
    }

    public static void initSystemTray() {
        if (!SystemTray.isSupported()) return;
        Dimension iconSize = SystemTray.getSystemTray().getTrayIconSize();
        try {
            TrayIcon trayIcon = new TrayIcon(ImageIO.read(Objects.requireNonNull(Utils.class.getClassLoader().getResourceAsStream(PROJECT_NAME + ".png"))).getScaledInstance(iconSize.width, iconSize.height, Image.SCALE_SMOOTH), PROJECT_NAME);
            PopupMenu menu = new PopupMenu();
            MenuItem exit = new MenuItem("Exit");
            exit.addActionListener(_ -> System.exit(0));
            menu.add(exit);
            trayIcon.setPopupMenu(menu);
            SystemTray.getSystemTray().add(trayIcon);
        } catch (IOException | AWTException e) {
            throw new RuntimeException(e);
        }
    }
}