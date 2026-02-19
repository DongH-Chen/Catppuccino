package dev.cdh.affiliate;

import dev.cdh.Movement;
import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;
import dev.cdh.constants.Direction;
import dev.cdh.constants.State;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.random.RandomGenerator;

public final class Cat {
    private final CatWindow window;
    private final ResourcesLoader resourceLoader;
    private final RandomGenerator ran = RandomGenerator.getDefault();

    private Behave currentAction = Behave.SLEEP;
    private List<BufferedImage> currentFrames;
    private List<BufferedImage> currentBubbleFrames;
    private Direction layingDir = Direction.RIGHT;
    private State state = State.DEFAULT;
    private BubbleState bubbleState = BubbleState.NONE;
    private final Point wanderTarget = new Point(0, 0);
    private final AnimationState animationState = new AnimationState();

    public Cat(ResourcesLoader resourcesLoader) {
        this.resourceLoader = resourcesLoader;
        this.window = new CatWindow(this);
        loadFramesForAction(currentAction);
        loadBubbleFrames(bubbleState);
    }

    public void update() {
        handleFrames();
        performMovement();
        updateAnimation();
        manageBubbleState();
        window.repaint();
    }

    public boolean changeAction(Behave behave) {
        if (currentAction != behave) {
            currentAction = behave;
            loadFramesForAction(behave);
            return true;
        }
        return false;
    }

    private void loadFramesForAction(Behave behave) {
        currentFrames = resourceLoader.loadFrames(behave);
    }

    private void loadBubbleFrames(BubbleState state) {
        currentBubbleFrames = resourceLoader.loadBubbleFrames(state);
    }

    private void updateAnimation() {
        animationState.incrementAnimationSteps();

        if (animationState.animationSteps() >= currentAction.delay()) {
            if (shouldTransitionFromLaying()) {
                handleLayingTransition();
            } else if (shouldTransitionFromSitting()) {
                handleSittingTransition();
            } else {
                animationState.nextFrame();
            }
        }

        if (animationState.frameNum() >= currentAction.frame()) {
            animationState.resetFrame();
        }
    }

    private boolean shouldTransitionFromLaying() {
        return currentAction == Behave.LAYING && animationState.frameNum() == currentAction.frame() - 1;
    }

    private void handleLayingTransition() {
        if (animationState.animationSteps() - currentAction.delay() > 40) {
            animationState.reset();
            changeAction(ran.nextBoolean() ? Behave.CURLED : Behave.SLEEP);
        }
    }

    private boolean shouldTransitionFromSitting() {
        return currentAction == Behave.SITTING &&
                animationState.frameNum() == currentAction.frame() - 1;
    }

    private void handleSittingTransition() {
        changeAction(Behave.LICKING);
        animationState.reset();
    }

    private void manageBubbleState() {
        if (bubbleState != BubbleState.HEART) {
            updateBubbleStateBasedOnAction();
        }

        animationState.incrementBubbleSteps();

        if (animationState.bubbleSteps() >= bubbleState.delay()) {
            animationState.nextBubbleFrame();
        }

        if (animationState.bubbleFrame() >= bubbleState.frame()) {
            animationState.resetBubbleFrame();
            if (bubbleState == BubbleState.HEART) {
                setBubbleState(BubbleState.NONE);
            }
        }
    }

    private void updateBubbleStateBasedOnAction() {
        if (currentAction == Behave.SLEEP || currentAction == Behave.CURLED) {
            setBubbleState(BubbleState.ZZZ);
        } else if (currentAction != Behave.SITTING) {
            setBubbleState(BubbleState.NONE);
        }
    }

    private void handleFrames() {
        if (currentAction == Behave.RISING) return;

        if (state == State.WANDER) {
            handleWandering();
        }

        handleMovementActions();
    }

    private void handleWandering() {
        Point curPos = window.getLocationOnScreen();
        if (Math.abs(curPos.x - wanderTarget.x) >= 3) {
            changeAction(curPos.x > wanderTarget.x ? Behave.LEFT : Behave.RIGHT);
        } else {
            changeAction(curPos.y > wanderTarget.y ? Behave.UP : Behave.DOWN);
        }
        state = wanderTarget.distance(curPos) < 3 ? State.DEFAULT : State.WANDER;
    }

    private void handleMovementActions() {
        boolean flag = false;
        switch (currentAction) {
            case LEFT -> layingDir = Direction.LEFT;
            case RIGHT -> layingDir = Direction.RIGHT;
            case Behave behave when state != State.WANDER && (behave == Behave.UP | behave == Behave.DOWN) ->
                    flag = ran.nextInt(3) >= 1 ? changeAction(Behave.LAYING) : changeAction(Behave.SITTING);
            default -> {}
        }
        if (flag) animationState.resetFrame();
    }

    private void performMovement() {
        Point loc = window.getLocation();
        Movement.move(loc, currentAction);

        Dimension screenSize = Movement.calculateVirtualScreenBounds();
        Movement.clampToScreen(loc, screenSize, window.getSize());

        window.setLocation(loc);
    }

    public void tryWandering() {
        if (ran.nextBoolean()) return;

        state = State.WANDER;
        Point screenLoc = window.getLocationOnScreen();
        Point target = Movement.generateRandomTarget(screenLoc, window.getSize());
        wanderTarget.setLocation(target);
    }

    // Setters
    public void setBubbleState(BubbleState state) {
        if (bubbleState != state) {
            bubbleState = state;
            loadBubbleFrames(state);
            animationState.resetBubbleFrame();
        }
    }

    // Getters
    public Behave currentAction() {
        return currentAction;
    }

    public List<BufferedImage> currentFrames() {
        return currentFrames;
    }

    public List<BufferedImage> currentBubbleFrames() {
        return currentBubbleFrames;
    }

    public Direction layingDir() {
        return layingDir;
    }

    public BubbleState bubbleState() {
        return bubbleState;
    }

    public AnimationState animationState() {
        return animationState;
    }

    public CatWindow window() {
        return window;
    }
}