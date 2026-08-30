package dev.cdh.affiliate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public final class SystemTrayManager {
    private static final String PROJECT_NAME = "Catppuccino";

    private SystemTrayManager() {
    }

    /** 初始化系统托盘图标；托盘不可用或初始化失败时静默降级，不影响小猫运行。 */
    public static void initialize() {
        if (!SystemTray.isSupported()) return;
        try {
            SystemTray.getSystemTray().add(createTrayIcon());
        } catch (Exception e) {
            System.err.println("Failed to initialize system tray: " + e.getMessage());
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
