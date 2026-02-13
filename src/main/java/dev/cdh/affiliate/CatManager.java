package dev.cdh.affiliate;

import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;
import dev.cdh.constants.Direction;
import dev.cdh.constants.State;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.random.RandomGenerator;

import static java.lang.Math.abs;

public final class CatManager {
    public static final CatWindow win = new CatWindow();
    public static final Map<String, List<BufferedImage>> frames = ResourcesLoader.loadAllFrames(Behave.class);
    public static final Map<String, List<BufferedImage>> bubbleFrames = ResourcesLoader.loadAllFrames(BubbleState.class);
    public static Behave currentAction = Behave.SLEEP;
    public static List<BufferedImage> currFrames;
    public static List<BufferedImage> currBubbleFrames;
    public static Direction layingDir = Direction.RIGHT;
    public static State state = State.DEFAULT;
    public static final Point wanderLoc = new Point(0, 0);
    public static BubbleState bubbleState = BubbleState.NONE;
    public static int bubbleFrame = 0, bubbleSteps = 0, animationSteps = 0, frameNum = 0;
    private static final RandomGenerator ran = RandomGenerator.getDefault();

    public static boolean changeAction(Behave behave) {
        if (currentAction != behave) {
            currentAction = behave;
            currFrames = frames.get(currentAction.name());
            return true;
        } else {
            return false;
        }
    }

    public static void updateAnimation() {
        animationSteps++;
        if (animationSteps >= currentAction.getDelay()) {
            if (currentAction == Behave.LAYING && frameNum == currentAction.getFrame() - 1) {
                if ((animationSteps - currentAction.getDelay()) > 40) {
                    animationSteps = 0;
                    frameNum = 0;
                    changeAction(ran.nextBoolean() ? Behave.CURLED : Behave.SLEEP);
                }
            } else if (currentAction == Behave.SITTING && frameNum == currentAction.getFrame() - 1) {
                changeAction(Behave.LICKING);
                animationSteps = 0;
                frameNum = 0;
            } else {
                frameNum++;
                animationSteps = 0;
            }
        }
        if (frameNum >= currentAction.getFrame()) frameNum = 0;
    }

    public static void manageBubbleState() {
        if (bubbleState != BubbleState.HEART) {
            if (currentAction == Behave.SLEEP || currentAction == Behave.CURLED) bubbleState = BubbleState.ZZZ;
            else if (currentAction != Behave.SITTING) bubbleState = BubbleState.NONE;
        }
        bubbleSteps++;
        currBubbleFrames = bubbleFrames.getOrDefault(bubbleState.name(), bubbleFrames.get(BubbleState.HEART.name()));
        if (bubbleSteps >= bubbleState.getDelay()) {
            bubbleFrame++;
            bubbleSteps = 0;
        }
        if (bubbleFrame >= bubbleState.getFrame()) {
            bubbleFrame = 0;
            if (bubbleState == BubbleState.HEART) bubbleState = BubbleState.NONE;
        }
    }

    public static void handleFrames() {
        if (currentAction != Behave.RISING) {
            if (state == State.WANDER) {
                var curPos = win.getLocationOnScreen();
                if (abs(curPos.x - wanderLoc.x) >= 3) {
                    changeAction(curPos.x > wanderLoc.x ? Behave.LEFT : Behave.RIGHT);
                } else {
                    changeAction(curPos.y > wanderLoc.y ? Behave.UP : Behave.DOWN);
                }
                state = wanderLoc.distance(curPos) < 3 ? State.DEFAULT : State.WANDER;
            }
            var flag = false;
            if (currentAction == Behave.LEFT) {
                layingDir = Direction.LEFT;
            } else if (currentAction == Behave.RIGHT) {
                layingDir = Direction.RIGHT;
            } else if (state != State.WANDER && (currentAction == Behave.UP || currentAction == Behave.DOWN)) {
                flag = ran.nextInt(3) >= 1 ? changeAction(Behave.LAYING) : changeAction(Behave.SITTING);
            }
            if (flag) frameNum = 0;
        }
    }

    public static void performMovement() {
        Point loc = win.getLocation();
        switch (currentAction) {
            case RIGHT -> loc.translate(1, 0);
            case LEFT -> loc.translate(-1, 0);
            case UP -> loc.translate(0, -1);
            case DOWN -> loc.translate(0, 1);
            default -> {
            }
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (loc.x > screenSize.width - win.getWidth()) {
            loc.setLocation(screenSize.width - win.getWidth(), loc.y);
        } else if (loc.x < -10) {
            loc.setLocation(-10, loc.y);
        } else if (loc.y > screenSize.height - win.getHeight()) {
            loc.setLocation(loc.x, screenSize.height - win.getHeight());
        } else if (loc.y < -35) {
            loc.setLocation(loc.x, -35);
        }
        win.setLocation(loc);
    }

    public static void tryWandering() {
        if (ran.nextBoolean()) return;
        state = State.WANDER;
        Point screenLoc = win.getLocationOnScreen();
        Point loc;
        do {
            var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            loc = new Point(
                    ran.nextInt(screenSize.width - win.getWidth() - 20) + 10,
                    ran.nextInt(screenSize.height - win.getHeight() - 20) + 10
            );
        } while (abs(screenLoc.y - loc.y) <= 400 && abs(screenLoc.x - loc.x) <= 400);
        wanderLoc.setLocation(loc.x, loc.y);
    }
}