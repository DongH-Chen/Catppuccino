package dev.cdh.affiliate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class SystemTrayManager {
    private static final String PROJECT_NAME = "Catppuccino";

    public static void initialize() {
        if (!SystemTray.isSupported()) return;

        try {
            TrayIcon trayIcon = createTrayIcon();
            SystemTray.getSystemTray().add(trayIcon);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize system tray", e);
        }
    }

    private static TrayIcon createTrayIcon() throws IOException {
        Dimension iconSize = SystemTray.getSystemTray().getTrayIconSize();

        Image image = ImageIO.read(
                Objects.requireNonNull(
                        SystemTrayManager.class.getClassLoader().getResourceAsStream(PROJECT_NAME + ".png")
                )
        ).getScaledInstance(iconSize.width, iconSize.height, Image.SCALE_SMOOTH);

        PopupMenu menu = createPopupMenu();

        return new TrayIcon(image, PROJECT_NAME, menu);
    }

    private static PopupMenu createPopupMenu() {
        PopupMenu menu = new PopupMenu();
        MenuItem exit = new MenuItem("Exit");
        exit.addActionListener(_ -> System.exit(0));
        menu.add(exit);
        return menu;
    }
}