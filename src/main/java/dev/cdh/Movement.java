package dev.cdh;

import dev.cdh.constants.Behave;

import java.awt.*;
import java.util.random.RandomGenerator;

public final class Movement {
    public static final Dimension SCREEN_SIZE = calculateVirtualScreenBounds();

    private Movement() {
    }

    private static Dimension calculateVirtualScreenBounds() {
        Rectangle virtualBounds = new Rectangle();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] screens = ge.getScreenDevices();
        for (GraphicsDevice screen : screens) {
            GraphicsConfiguration config = screen.getDefaultConfiguration();
            virtualBounds = virtualBounds.union(config.getBounds());
        }
        Dimension result = new Dimension();
        result.setSize(virtualBounds.getWidth(), virtualBounds.getHeight());
        return result;
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

    public static void clampToScreen(Point location, Dimension screenSize, Dimension windowSize) {
        switch (location) {
            case Point p when p.x > screenSize.width - windowSize.width ->
                    location.setLocation(screenSize.width - windowSize.width, location.y);
            case Point p when p.x < -10 -> location.setLocation(-10, p.y);
            case Point p when p.y > screenSize.height - windowSize.height ->
                    location.setLocation(location.x, screenSize.height - windowSize.height);
            case Point p when p.y < -35 -> location.setLocation(location.x, -35);
            default -> {
            }
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