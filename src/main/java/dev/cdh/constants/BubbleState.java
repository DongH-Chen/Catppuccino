package dev.cdh.constants;

/**
 * 气泡状态。delay 为帧间隔的步数（每步约 33ms）：
 * ZZZ 18 步 ≈ 600ms/帧，HEART 30 步 ≈ 1s/帧。
 */
public enum BubbleState implements Animate {
    ZZZ(4, 18),
    HEART(4, 30),
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
