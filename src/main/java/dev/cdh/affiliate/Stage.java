package dev.cdh.affiliate;

import dev.cdh.ImageUtils;
import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;
import dev.cdh.constants.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public final class Stage extends JPanel {
    private static final int BASE_X = 30, BASE_Y = 40, BUBBLE_SIZE = 30;
    private final Cat cat;

    public Stage(Cat cat) {
        this.cat = cat;
        setOpaque(false);
    }

    private boolean needsFlipping() {
        Behave action = cat.currentAction();
        Direction direction = cat.layingDir();
        return (action == Behave.LAYING || action == Behave.RISING || action == Behave.SLEEP)
                && direction == Direction.LEFT
                || action == Behave.CURLED
                && direction == Direction.RIGHT;
    }

    private void paintBubble(Graphics g) {
        List<BufferedImage> frames = cat.currentBubbleFrames();
        AnimationState state = cat.animationState();
        if (frames == null || frames.isEmpty()) return;
        BufferedImage bubble = frames.get(state.bubbleFrame());
        Point pos = calculateBubblePosition();
        g.drawImage(bubble, pos.x, pos.y, BUBBLE_SIZE, BUBBLE_SIZE, null);
    }

    private Point calculateBubblePosition() {
        var action = cat.currentAction();
        var direction = cat.layingDir();

        return switch (action) {
            case SLEEP, LAYING, LEFT, RIGHT -> {
                int x = direction == Direction.LEFT ? 0 : BASE_X + 30;
                yield new Point(x, BASE_Y);
            }
            case UP, LICKING, SITTING -> new Point(BASE_X, BASE_Y - 25);
            default -> new Point(BASE_X, BASE_Y);
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        var state = cat.animationState();
        var frames = cat.currentFrames();

        if (frames == null || frames.isEmpty()) return;

        BufferedImage img = frames.get(state.frameNum());

        if (needsFlipping()) {
            img = ImageUtils.flipHorizontally(img);
        }

        g.drawImage(img, 0, 0, getWidth(), getHeight(), null);

        if (cat.bubbleState() != BubbleState.NONE) {
            paintBubble(g);
        }
    }
}