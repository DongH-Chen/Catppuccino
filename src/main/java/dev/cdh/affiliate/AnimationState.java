package dev.cdh.affiliate;

public final class AnimationState {
    private int frameNum = 0,
            animationSteps = 0,
            bubbleFrame = 0,
            bubbleSteps = 0;

    public int frameNum() {
        return frameNum;
    }

    public void setFrameNum(int frameNum) {
        this.frameNum = frameNum;
    }

    public void resetFrame() {
        frameNum = 0;
    }

    public int animationSteps() {
        return animationSteps;
    }

    public void setAnimationSteps(int animationSteps) {
        this.animationSteps = animationSteps;
    }

    public void incrementAnimationSteps() {
        animationSteps++;
    }

    public int bubbleFrame() {
        return bubbleFrame;
    }

    public void setBubbleFrame(int bubbleFrame) {
        this.bubbleFrame = bubbleFrame;
    }

    public void resetBubbleFrame() {
        bubbleFrame = 0;
    }

    public int bubbleSteps() {
        return bubbleSteps;
    }

    public void setBubbleSteps(int bubbleSteps) {
        this.bubbleSteps = bubbleSteps;
    }

    public void incrementBubbleSteps() {
        bubbleSteps++;
    }

    public void nextFrame() {
        frameNum++;
        animationSteps = 0;
    }

    public void nextBubbleFrame() {
        bubbleFrame++;
        bubbleSteps = 0;
    }

    public void reset() {
        frameNum = 0;
        animationSteps = 0;
        bubbleFrame = 0;
        bubbleSteps = 0;
    }
}
