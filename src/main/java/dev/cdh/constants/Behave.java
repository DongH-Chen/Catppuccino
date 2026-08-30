package dev.cdh.constants;

/**
 * 猫的行为状态。delay 为帧间隔的步数（每步约 33ms，对应 30 FPS 定时器）：
 * 6 步 ≈ 200ms，12 步 ≈ 400ms，24 步 ≈ 800ms。
 */
public enum Behave implements Animate {
    UP(4, 6),
    DOWN(4, 6),
    LEFT(4, 6),
    RIGHT(4, 6),
    CURLED(2, 24),
    LAYING(4, 12),
    SITTING(4, 12),
    LICKING(4, 24),
    RISING(2, 24),
    SLEEP(1, 6);

    private final int delay;
    private final int frame;

    Behave(int frame, int delay) {
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
