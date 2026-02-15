package dev.cdh.affiliate;

import dev.cdh.constants.Behave;

import javax.swing.*;
import java.time.LocalDateTime;

public final class CatController {
    private final Cat cat;
    private int wanderCount = 0;
    private final int wanderInterval;

    public CatController(Cat cat) {
        this.cat = cat;
        int hour = LocalDateTime.now().getHour();
        this.wanderInterval = (hour < 18 && hour > 8) ? 600 : 3000;
    }

    public void start() {
        cat.window().setVisible(true);
        cat.changeAction(Behave.CURLED);
        new Timer(10, _ -> {
            cat.update();
            if (++wanderCount >= wanderInterval) {
                cat.tryWandering();
                wanderCount = 0;
            }
        }).start();
    }
}
