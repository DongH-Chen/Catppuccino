package dev.cdh.affiliate

class AnimationState {
    private var frameNum = 0
    private var animationSteps = 0
    private var bubbleFrame = 0
    private var bubbleSteps = 0

    fun frameNum(): Int {
        return frameNum
    }

    fun setFrameNum(frameNum: Int) {
        this.frameNum = frameNum
    }

    fun resetFrame() {
        frameNum = 0
    }

    fun animationSteps(): Int {
        return animationSteps
    }

    fun setAnimationSteps(animationSteps: Int) {
        this.animationSteps = animationSteps
    }

    fun incrementAnimationSteps() {
        animationSteps++
    }

    fun bubbleFrame(): Int {
        return bubbleFrame
    }

    fun setBubbleFrame(bubbleFrame: Int) {
        this.bubbleFrame = bubbleFrame
    }

    fun resetBubbleFrame() {
        bubbleFrame = 0
    }

    fun bubbleSteps(): Int {
        return bubbleSteps
    }

    fun setBubbleSteps(bubbleSteps: Int) {
        this.bubbleSteps = bubbleSteps
    }

    fun incrementBubbleSteps() {
        bubbleSteps++
    }

    fun nextFrame() {
        frameNum++
        animationSteps = 0
    }

    fun nextBubbleFrame() {
        bubbleFrame++
        bubbleSteps = 0
    }

    fun reset() {
        frameNum = 0
        animationSteps = 0
        bubbleFrame = 0
        bubbleSteps = 0
    }
}