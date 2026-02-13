package dev.cdh;

import dev.cdh.constants.Behave;

import javax.swing.*;
import java.time.LocalDateTime;

import static dev.cdh.affiliate.CatManager.*;

public final class Main {
    private static int wanderCount = 0;
    private static final int time = LocalDateTime.now().getHour();

    static void main() {
        SwingUtilities.invokeLater(() -> {
            Utils.initSystemTray();
            win.setVisible(true);
            changeAction(Behave.CURLED);
        });

        new Timer(10, _ -> {
            handleFrames();
            performMovement();
            updateAnimation();
            manageBubbleState();
            win.repaint();
            if (++wanderCount >= (time < 18 && time > 8 ? 600 : 3000)) {
                tryWandering();
                wanderCount = 0;
            }
        }).start();
    }
}