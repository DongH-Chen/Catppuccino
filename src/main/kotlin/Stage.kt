package me.cdh

import me.cdh.CatAnimationManager.bubbleFrame
import me.cdh.CatAnimationManager.bubbleState
import me.cdh.CatAnimationManager.currBubbleFrames
import me.cdh.CatAnimationManager.currFrames
import me.cdh.CatAnimationManager.currentAction
import me.cdh.CatAnimationManager.frameNum
import me.cdh.CatAnimationManager.layingDir
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
                val x = if (direction == Direction.LEFT) 0 else 30 + 30
                Point(x, 40)
            }

            Behave.UP, Behave.LICKING, Behave.SITTING -> Point(30, 40 - 25)

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