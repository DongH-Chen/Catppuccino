package me.cdh

import me.cdh.CatManager.bubbleFrame
import me.cdh.CatManager.bubbleState
import me.cdh.CatManager.currBubbleFrames
import me.cdh.CatManager.currFrames
import me.cdh.CatManager.currentAction
import me.cdh.CatManager.frameNum
import me.cdh.CatManager.layingDir
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
            Behave.SLEEP, Behave.LAYING, Behave.LEFT, Behave.RIGHT -> Point(
                if (direction == Direction.LEFT) 0 else 30 + 30,
                40
            )

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