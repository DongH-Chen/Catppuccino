package dev.cdh.affiliate

import dev.cdh.ImageCache
import dev.cdh.constants.Behave
import dev.cdh.constants.BubbleState
import dev.cdh.constants.Direction
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.util.*
import javax.swing.JPanel

internal class Stage(private val cat: Cat) : JPanel() {
    private val bubbleRect = Rectangle()

    init {
        isDoubleBuffered = true
        setOpaque(false)
        initializePositionCache()
    }

    private fun initializePositionCache() {
        POSITION_CACHE[Behave.SLEEP] = { dir: Direction -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
        POSITION_CACHE[Behave.LAYING]={ dir: Direction -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
        POSITION_CACHE[Behave.LEFT] = { dir: Direction -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
        POSITION_CACHE[Behave.RIGHT] = { dir: Direction -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
        POSITION_CACHE[Behave.UP] = { Point(BASE_X, BASE_Y - 25) }
        POSITION_CACHE[Behave.LICKING] = { Point(BASE_X, BASE_Y - 25) }
        POSITION_CACHE[Behave.SITTING] = { Point(BASE_X, BASE_Y - 25) }
    }

    private fun needsFlipping(): Boolean {
        val action = cat.currentAction
        val direction = cat.layingDir
        return (action == Behave.LAYING || action == Behave.RISING || action == Behave.SLEEP)
                && direction == Direction.LEFT
                || action == Behave.CURLED
                && direction == Direction.RIGHT
    }

    private fun calculateBubblePosition(): Point {
        val calculator = POSITION_CACHE[cat.currentAction]
        if (calculator != null) return calculator(cat.layingDir)
        return Point(BASE_X, BASE_Y)
    }

    override fun paintComponent(g: Graphics) {
        val g2d = g.create() as Graphics2D
        try {
            paintCat(g2d)
            paintBubbleIfNeeded(g2d)
        } finally {
            g2d.dispose()
        }
    }

    private fun paintCat(g2d: Graphics2D) {
        val state = cat.animationState
        val frames = cat.currentFrames
        if (frames.isEmpty()) return
        var img = frames[state.frameNum]
        if (needsFlipping()) {
            val flipKey = "${cat.currentAction.name}${state.frameNum}"

            img = ImageCache.getOrFlip(img!!, flipKey)
        }
        g2d.drawImage(img, 0, 0, getWidth(), getHeight(), null)
    }

    private fun paintBubbleIfNeeded(g2d: Graphics2D) {
        if (cat.bubbleState == BubbleState.NONE) return
        val frames = cat.currentBubbleFrames
        if (frames.isEmpty()) return
        val state = cat.animationState
        val bubble = frames[state.bubbleFrame]
        val pos = calculateBubblePosition()
        bubbleRect.setBounds(pos.x, pos.y, BUBBLE_SIZE, BUBBLE_SIZE)
        g2d.drawImage(bubble, bubbleRect.x, bubbleRect.y, bubbleRect.width, bubbleRect.height, null)
    }

    companion object {
        private const val BASE_X = 30
        private const val BASE_Y = 40
        private const val BUBBLE_SIZE = 30
        private val POSITION_CACHE: MutableMap<Behave?, (Direction)->Point> =
            EnumMap<Behave, (Direction)->Point>(Behave::class.java)
    }
}