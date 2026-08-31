package dev.cdh;

import dev.cdh.constants.Behave;

import java.awt.*;
import java.util.random.RandomGenerator;

public final class Movement {
    // The virtual desktop boundaries made up of all monitors
    // may include negative coordinates when using multiple monitors.
    public static final Rectangle VIRTUAL_BOUNDS = calculateVirtualScreenBounds();

    private static final int EDGE_OVERFLOW_X = 10,
            EDGE_OVERFLOW_Y = 35,
            MIN_TARGET_DISTANCE = 400,
            MAX_TARGET_ATTEMPTS = 100;

    private Movement() {
    }

    private static Rectangle calculateVirtualScreenBounds() {
        Rectangle virtualBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        for (GraphicsDevice screen : screens) {
            GraphicsConfiguration config = screen.getDefaultConfiguration();
            virtualBounds = virtualBounds.union(config.getBounds());
        }
        if (virtualBounds.isEmpty()) {
            virtualBounds.setSize(1920, 1080);
        }
        return virtualBounds;
    }

    public static void move(Point location, Behave action) {
        switch (action) {
            case RIGHT -> location.translate(1, 0);
            case LEFT -> location.translate(-1, 0);
            case UP -> location.translate(0, -1);
            case DOWN -> location.translate(0, 1);
            default -> {
            }
        }
    }

    public static void clampToScreen(Point location, Dimension windowSize) {
        int minX = VIRTUAL_BOUNDS.x;
        int minY = VIRTUAL_BOUNDS.y;
        int maxX = VIRTUAL_BOUNDS.x + VIRTUAL_BOUNDS.width - windowSize.width;
        int maxY = VIRTUAL_BOUNDS.y + VIRTUAL_BOUNDS.height - windowSize.height;

        if (location.x > maxX) {
            location.x = maxX;
        }
        if (location.x < minX - EDGE_OVERFLOW_X) {
            location.x = minX - EDGE_OVERFLOW_X;
        }
        if (location.y > maxY) {
            location.y = maxY;
        }
        if (location.y < minY - EDGE_OVERFLOW_Y) {
            location.y = minY - EDGE_OVERFLOW_Y;
        }
    }

    public static Point generateRandomTarget(Point currentPos, Dimension windowSize) {
        RandomGenerator random = RandomGenerator.getDefault();
        int width = Math.max(1, VIRTUAL_BOUNDS.width - windowSize.width - 20);
        int height = Math.max(1, VIRTUAL_BOUNDS.height - windowSize.height - 20);

        Point target;
        int attempts = 0;
        do {
            target = new Point(
                    VIRTUAL_BOUNDS.x + random.nextInt(width) + 10,
                    VIRTUAL_BOUNDS.y + random.nextInt(height) + 10
            );
            attempts++;
        } while (attempts < MAX_TARGET_ATTEMPTS
                && Math.abs(currentPos.y - target.y) <= MIN_TARGET_DISTANCE
                && Math.abs(currentPos.x - target.x) <= MIN_TARGET_DISTANCE);

        return target;
    }
}
