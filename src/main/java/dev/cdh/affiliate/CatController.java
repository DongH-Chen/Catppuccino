package dev.cdh.affiliate;

import dev.cdh.constants.Behave;

import javax.swing.*;
import java.time.LocalDateTime;

public final class CatController {
    /** Animation timer interval (milliseconds), about 30 FPS. */
    private static final int TICK_MS = 33;

    private final Cat cat;
    private int wanderCount = 0;
    private final int wanderInterval;

    public CatController(Cat cat) {
        this.cat = cat;
        int hour = LocalDateTime.now().getHour();
        // During the day (9 AM - 5 PM), tries to wander about every 12 seconds;
        // at night, about every 60 seconds
        this.wanderInterval = (hour < 18 && hour > 8) ? 360 : 1800;
    }

    public void start() {
        cat.window().setVisible(true);
        cat.changeAction(Behave.CURLED);
        new Timer(TICK_MS, _ -> {
            cat.update();
            if (++wanderCount >= wanderInterval) {
                cat.tryWandering();
                wanderCount = 0;
            }
        }).start();
    }
}
