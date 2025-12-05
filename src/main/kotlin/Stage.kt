package me.cdh

import me.cdh.AnimationManager.bubbleFrame
import me.cdh.AnimationManager.bubbleState
import me.cdh.AnimationManager.currBubbleFrames
import me.cdh.AnimationManager.currFrames
import me.cdh.AnimationManager.currentAction
import me.cdh.AnimationManager.frameNum
import me.cdh.AnimationManager.layingDir
import java.awt.Graphics
import java.awt.Point
import javax.swing.JPanel

class Stage : JPanel() {

    init {
        isOpaque = false
    }

    private fun needsFlipping(behave: Behave, direction: Direction) =
        (behave == Behave.LAYING || behave == Behave.RISING || behave == Behave.SLEEP)
                && direction == Direction.LEFT
                || behave == Behave.CURLED
                && direction == Direction.RIGHT

    private fun calculateBubblePosition(action: Behave, direction: Direction): Point {
        return when (action) {
            Behave.SLEEP, Behave.LAYING, Behave.LEFT, Behave.RIGHT -> {
                val x = if (direction == Direction.LEFT) 0 else 60
                Point(x, 40)
            }

            Behave.UP, Behave.LICKING, Behave.SITTING -> Point(30, 15)

            else -> Point(30, 40)
        }
    }

    override fun paintComponent(g: Graphics?) {
        val img = currFrames.getOrNull(frameNum) ?: return
        val flippedImage = if (needsFlipping(currentAction, layingDir)) flipImg(img) else img
        g?.drawImage(flippedImage, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT, null) ?: return
        if (bubbleState != BubbleState.NONE) {
            currBubbleFrames.getOrNull(bubbleFrame)?.let { bubbleImg ->
                val position = calculateBubblePosition(currentAction, layingDir)
                g.drawImage(bubbleImg, position.x, position.y, BUBBLE_SIZE, BUBBLE_SIZE, null)
            }
        }
    }
}