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
    private static final int BASE_X = 30, BASE_Y = 40, BUBBLE_SIZE = 30;
    private static final Map<Behave, PositionCalculator> POSITION_CACHE = new EnumMap<>(Behave.class);
    private final Cat cat;
    private final Rectangle bubbleRect = new Rectangle();

    public Stage(Cat cat) {
        this.cat = cat;
        setOpaque(false);
        initializePositionCache();
    }

    private void initializePositionCache() {
        POSITION_CACHE.put(Behave.SLEEP, dir -> new Point(dir == Direction.LEFT ? 0 : BASE_X + 30, BASE_Y));
        POSITION_CACHE.put(Behave.LAYING, dir ->
                new Point(dir == Direction.LEFT ? 0 : BASE_X + 30, BASE_Y));
        POSITION_CACHE.put(Behave.LEFT, dir ->
                new Point(dir == Direction.LEFT ? 0 : BASE_X + 30, BASE_Y));
        POSITION_CACHE.put(Behave.RIGHT, dir ->
                new Point(dir == Direction.LEFT ? 0 : BASE_X + 30, BASE_Y));
        POSITION_CACHE.put(Behave.UP, _ -> new Point(BASE_X, BASE_Y - 25));
        POSITION_CACHE.put(Behave.LICKING, _ -> new Point(BASE_X, BASE_Y - 25));
        POSITION_CACHE.put(Behave.SITTING, _ -> new Point(BASE_X, BASE_Y - 25));
    }

    private boolean needsFlipping() {
        Behave action = cat.currentAction();
        Direction direction = cat.layingDir();
        return (action == Behave.LAYING || action == Behave.RISING || action == Behave.SLEEP)
                && direction == Direction.LEFT
                || action == Behave.CURLED
                && direction == Direction.RIGHT;
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
            String flipKey = cat.currentAction().name() + state.frameNum();

            img = ImageCache.getOrFlip(img,flipKey);
        }
        g2d.drawImage(img, 0, 0, getWidth(), getHeight(), null);
    }

    private void paintBubbleIfNeeded(Graphics2D g2d) {
        if (cat.bubbleState() == BubbleState.NONE) return;
        List<BufferedImage> frames = cat.currentBubbleFrames();
        if (frames == null || frames.isEmpty()) return;
        AnimationState state = cat.animationState();
        BufferedImage bubble = frames.get(state.bubbleFrame());
        Point pos = calculateBubblePosition();
        bubbleRect.setBounds(pos.x, pos.y, BUBBLE_SIZE, BUBBLE_SIZE);
        g2d.drawImage(bubble, bubbleRect.x, bubbleRect.y, bubbleRect.width, bubbleRect.height, null);
    }

    @FunctionalInterface
    private interface PositionCalculator {
        Point calculate(Direction direction);
    }
}