package dev.cdh.constants;

public enum Behave implements Animate {
    UP(4, 10),
    DOWN(4, 10),
    LEFT(4, 10),
    RIGHT(4, 10),
    CURLED(2, 40),
    LAYING(4, 20),
    SITTING(4, 20),
    LICKING(4, 40),
    RISING(2, 40),
    SLEEP(1, 10);

    private final int delay;
    private final int frame;

    Behave(int frame, int delay) {
        this.delay = delay;
        this.frame = frame;
    }

    @Override
    public int getDelay() {
        return delay;
    }

    @Override
    public int getFrame() {
        return frame;
    }
}
