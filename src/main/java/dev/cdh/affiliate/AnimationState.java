package dev.cdh.affiliate;

public final class AnimationState {
    /**
     * 1 ~ 8: frameNum
     * 9 ~ 16: animationSteps
     * 17 ~ 24: bubbleFrame
     * 25 ~ 32: bubbleSteps
     */
    private int packedValue = 0;

    public int frameNum() {
        return packedValue & 0xff;
    }

    public void setFrameNum(int frameNum) {
        packedValue = (packedValue & 0xffffff00) | (frameNum & 0xff);
    }

    public void resetFrame() {
        setFrameNum(0);
    }

    public int animationSteps() {
        return (packedValue >> 8) & 0xff;
    }

    public void setAnimationSteps(int steps) {
        packedValue = (packedValue & 0xffff00ff) | ((steps & 0xff) << 8);
    }

    public void incrementAnimationSteps() {
        setAnimationSteps(animationSteps() + 1);
    }

    public int bubbleFrame() {
        return (packedValue >> 16) & 0xff;
    }

    public void setBubbleFrame(int frame) {
        packedValue = (packedValue & 0xff00ffff) | ((frame & 0xff) << 16);
    }

    public void nextBubbleFrame() {
        setBubbleFrame(bubbleFrame() + 1);
        setBubbleSteps(0);
    }

    public void resetBubbleFrame() {
        setBubbleFrame(0);
    }

    public int bubbleSteps() {
        return (packedValue >> 24) & 0xff;
    }

    public void setBubbleSteps(int steps) {
        packedValue = (packedValue & 0x00ffffff) | ((steps & 0xff) << 24);
    }

    public void incrementBubbleSteps() {
        setBubbleSteps(bubbleSteps() + 1);
    }

    public void reset() {
        packedValue = 0;
    }

    public void nextFrame() {
        setFrameNum(frameNum() + 1);
        setAnimationSteps(0);
    }
}
