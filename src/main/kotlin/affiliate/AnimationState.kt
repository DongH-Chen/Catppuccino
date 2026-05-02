package dev.cdh.affiliate

class AnimationState {
    var frameNum = 0

    var animationSteps = 0
    var bubbleFrame = 0
    var bubbleSteps = 0

    fun resetFrame() {
        frameNum = 0
    }

    fun incrementAnimationSteps() {
        animationSteps++
    }

    fun resetBubbleFrame() {
        bubbleFrame = 0
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