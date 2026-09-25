package dev.cdh.affiliate

data class AnimationState(
    var frameNum: Int = 0,
    var animationSteps: Int = 0,
    var bubbleFrame: Int = 0,
    var bubbleSteps: Int = 0
) {

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