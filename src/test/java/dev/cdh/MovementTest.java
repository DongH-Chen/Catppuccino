package dev.cdh;

import dev.cdh.constants.Behave;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class MovementTest {

    @Test
    void moveTranslatesAccordingToAction() {
        Point p = new Point(10, 10);

        Movement.move(p, Behave.RIGHT);
        assertEquals(new Point(11, 10), p);

        Movement.move(p, Behave.LEFT);
        assertEquals(new Point(10, 10), p);

        Movement.move(p, Behave.DOWN);
        assertEquals(new Point(10, 11), p);

        Movement.move(p, Behave.UP);
        assertEquals(new Point(10, 10), p);
    }

    @Test
    void moveIgnoresNonMovingActions() {
        Point p = new Point(5, 5);
        Movement.move(p, Behave.SLEEP);
        Movement.move(p, Behave.CURLED);
        assertEquals(new Point(5, 5), p);
    }

    @Test
    void clampKeepsLocationInsideVirtualBounds() {
        Dimension window = new Dimension(100, 100);
        Rectangle bounds = Movement.VIRTUAL_BOUNDS;

        Point right = new Point(bounds.x + bounds.width + 100, bounds.y);
        Movement.clampToScreen(right, window);
        assertTrue(right.x <= bounds.x + bounds.width - window.width);

        Point bottom = new Point(bounds.x, bounds.y + bounds.height + 100);
        Movement.clampToScreen(bottom, window);
        assertTrue(bottom.y <= bounds.y + bounds.height - window.height);

        Point inside = new Point(bounds.x + 50, bounds.y + 50);
        Movement.clampToScreen(inside, window);
        assertEquals(new Point(bounds.x + 50, bounds.y + 50), inside);
    }

    @Test
    void generateRandomTargetStaysInsideVirtualBounds() {
        Dimension window = new Dimension(100, 100);
        Rectangle bounds = Movement.VIRTUAL_BOUNDS;
        Point current = new Point(bounds.x + 500, bounds.y + 500);

        for (int i = 0; i < 200; i++) {
            Point target = Movement.generateRandomTarget(current, window);
            assertTrue(target.x >= bounds.x);
            assertTrue(target.y >= bounds.y);
            assertTrue(target.x <= bounds.x + bounds.width - window.width);
            assertTrue(target.y <= bounds.y + bounds.height - window.height);
        }
    }
}
