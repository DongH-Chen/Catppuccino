package dev.cdh.affiliate;

import dev.cdh.constants.Behave;

import java.awt.*;
import java.util.random.RandomGenerator;

public final class Movement {
    private Movement() {}

    public static void move(Point location, Behave action) {
        switch (action) {
            case RIGHT -> location.translate(1, 0);
            case LEFT -> location.translate(-1, 0);
            case UP -> location.translate(0, -1);
            case DOWN -> location.translate(0, 1);
            default -> {}
        }
    }

    public static void clampToScreen(Point location, Dimension screenSize, Dimension windowSize) {
        if (location.x > screenSize.width - windowSize.width) {
            location.setLocation(screenSize.width - windowSize.width, location.y);
        } else if (location.x < -10) {
            location.setLocation(-10, location.y);
        } else if (location.y > screenSize.height - windowSize.height) {
            location.setLocation(location.x, screenSize.height - windowSize.height);
        } else if (location.y < -35) {
            location.setLocation(location.x, -35);
        }
    }

    public static Point generateRandomTarget(Point currentPos, Dimension windowSize) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        RandomGenerator random = RandomGenerator.getDefault();
        Point target;
        do {
            target = new Point(
                    random.nextInt(screenSize.width - windowSize.width - 20) + 10,
                    random.nextInt(screenSize.height - windowSize.height - 20) + 10
            );
        } while (Math.abs(currentPos.y - target.y) <= 400 &&
                Math.abs(currentPos.x - target.x) <= 400);

        return target;
    }
}
