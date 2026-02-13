package dev.cdh.affiliate;

import dev.cdh.Utils;
import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;
import dev.cdh.constants.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static dev.cdh.affiliate.CatManager.*;

public final class Stage extends JPanel {
    private static final int BASE_X = 30, BASE_Y = 40, WINDOW_HEIGHT = 100, WINDOW_WIDTH = 100, BUBBLE_SIZE = 30;

    public Stage() {
        setOpaque(false);
    }

    private boolean needsFlipping(Behave behave, Direction direction) {
        return (behave == Behave.LAYING || behave == Behave.RISING || behave == Behave.SLEEP)
                && direction == Direction.LEFT
                || behave == Behave.CURLED
                && direction == Direction.RIGHT;
    }

    private Point calculateBubblePosition(Behave action, Direction direction) {
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
    protected void paintComponent(final Graphics g) {
        BufferedImage img = currFrames.get(frameNum);
        BufferedImage flippedImage = needsFlipping(currentAction, layingDir) ? Utils.flipImage(img) : img;
        g.drawImage(flippedImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null);
        if (bubbleState != BubbleState.NONE) {
            BufferedImage bufferedImage = currBubbleFrames.get(bubbleFrame);
            Point position = calculateBubblePosition(currentAction, layingDir);
            g.drawImage(bufferedImage, position.x, position.y, BUBBLE_SIZE, BUBBLE_SIZE, null);
        }
    }
}