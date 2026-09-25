package dev.cdh.affiliate

import java.awt.*
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.system.exitProcess

object SystemTrayManager

private const val PROJECT_NAME = "Catppuccino"

fun initialize() {
    if (!SystemTray.isSupported()) return
    try {
        SystemTray.getSystemTray().add(createTrayIcon())
    } catch (e: Exception) {
        System.err.println("Failed to initialize system tray: " + e.message)
    }
}

@Throws(IOException::class)
private fun createTrayIcon(): TrayIcon {
    val iconSize = SystemTray.getSystemTray().trayIconSize

    val image = ImageIO.read(
        SystemTrayManager::class.java.classLoader.getResourceAsStream("$PROJECT_NAME.png")
    ).getScaledInstance(iconSize.width, iconSize.height, Image.SCALE_SMOOTH)

    val menu = createPopupMenu()

    return TrayIcon(image, PROJECT_NAME, menu)
}

private fun createPopupMenu(): PopupMenu {
    val menu = PopupMenu()
    val exit = MenuItem("Exit")
    exit.addActionListener { exitProcess(0) }
    menu.add(exit)
    return menu
}