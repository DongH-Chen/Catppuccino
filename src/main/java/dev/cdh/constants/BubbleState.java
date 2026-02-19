package dev.cdh.constants;

public enum BubbleState implements Animate {
    ZZZ(4, 30),
    HEART(4, 50),
    NONE(-1, -1);
    private final int delay;
    private final int frame;

    BubbleState(int frame, int delay) {
        this.delay = delay;
        this.frame = frame;
    }

    @Override
    public int delay() {
        return delay;
    }

    @Override
    public int frame() {
        return frame;
    }
}