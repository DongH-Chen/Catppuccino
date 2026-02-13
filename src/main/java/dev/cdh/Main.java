package dev.cdh;

import dev.cdh.constants.Behave;

import javax.swing.*;
import java.time.LocalDateTime;

import static dev.cdh.affiliate.CatManager.*;

public final class Main {
    private static final int ANIMATION_UPDATE_DELAY = 10,
            DAYTIME_WANDER_INTERVAL = 6000,
            NIGHTTIME_WANDER_INTERVAL = 30000;

    static void main() {
        SwingUtilities.invokeLater(() -> {
            Utils.initSystemTray();
            win.setVisible(true);
            changeAction(Behave.CURLED);
        });
        Timer eventLoop = new Timer(ANIMATION_UPDATE_DELAY, _ -> {
            handleFrames();
            performMovement();
            updateAnimation();
            manageBubbleState();
            win.repaint();
        });
        int time = LocalDateTime.now().getHour();
        int DELAY = time < 18 && time > 8 ? DAYTIME_WANDER_INTERVAL : NIGHTTIME_WANDER_INTERVAL;
        Timer wanderingLoop = new Timer(DELAY, _ -> tryWandering());
        eventLoop.start();
        wanderingLoop.start();
    }
}