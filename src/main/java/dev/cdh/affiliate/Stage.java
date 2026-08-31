package dev.cdh.affiliate;

import dev.cdh.ImageCache;
import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;
import dev.cdh.constants.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class Stage extends JPanel {
    private static final int BASE_X = 30;
    private static final int BASE_Y = 40;

    private static final Map<Behave, PositionCalculator> POSITION_CACHE = createPositionCache();

    private final Cat cat;

    public Stage(Cat cat) {
        this.cat = cat;
        setDoubleBuffered(true);
        setOpaque(false);
    }

    private static Map<Behave, PositionCalculator> createPositionCache() {
        Map<Behave, PositionCalculator> cache = new EnumMap<>(Behave.class);
        cache.put(Behave.SLEEP, dir -> new Point(dir == Direction.LEFT ? 0 : BASE_X + 30, BASE_Y));
        cache.put(Behave.LAYING, dir -> new Point(dir == Direction.LEFT ? 0 : BASE_X + 30, BASE_Y));
        cache.put(Behave.LEFT, dir -> new Point(dir == Direction.LEFT ? 0 : BASE_X + 30, BASE_Y));
        cache.put(Behave.RIGHT, dir -> new Point(dir == Direction.LEFT ? 0 : BASE_X + 30, BASE_Y));
        cache.put(Behave.UP, ignored -> new Point(BASE_X, BASE_Y - 25));
        cache.put(Behave.LICKING, ignored -> new Point(BASE_X, BASE_Y - 25));
        cache.put(Behave.SITTING, ignored -> new Point(BASE_X, BASE_Y - 25));
        return cache;
    }

    private boolean needsFlipping() {
        Behave action = cat.currentAction();
        Direction direction = cat.layingDir();
        return ((action == Behave.LAYING || action == Behave.RISING || action == Behave.SLEEP)
                && direction == Direction.LEFT)
                || (action == Behave.CURLED && direction == Direction.RIGHT);
    }

    private Point calculateBubblePosition() {
        PositionCalculator calculator = POSITION_CACHE.get(cat.currentAction());
        if (calculator != null) return calculator.calculate(cat.layingDir());
        return new Point(BASE_X, BASE_Y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            paintCat(g2d);
            paintBubbleIfNeeded(g2d);
        } finally {
            g2d.dispose();
        }
    }

    private void paintCat(Graphics2D g2d) {
        AnimationState state = cat.animationState();
        List<BufferedImage> frames = cat.currentFrames();
        if (frames == null || frames.isEmpty()) return;
        BufferedImage img = frames.get(state.frameNum());
        if (needsFlipping()) {
            String flipKey = cat.catType() + ":" + cat.currentAction().name() + ":" + state.frameNum();
            img = ImageCache.getOrFlip(img, flipKey);
        }
        g2d.drawImage(img, 0, 0, null);
    }

    private void paintBubbleIfNeeded(Graphics2D g2d) {
        if (cat.bubbleState() == BubbleState.NONE) return;
        List<BufferedImage> frames = cat.currentBubbleFrames();
        if (frames == null || frames.isEmpty()) return;
        AnimationState state = cat.animationState();
        BufferedImage bubble = frames.get(state.bubbleFrame());
        Point pos = calculateBubblePosition();
        g2d.drawImage(bubble, pos.x, pos.y, null);
    }

    @FunctionalInterface
    private interface PositionCalculator {
        Point calculate(Direction direction);
    }
}
