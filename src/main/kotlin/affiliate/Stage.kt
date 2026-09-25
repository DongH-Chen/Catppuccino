package dev.cdh.affiliate

import dev.cdh.ImageCache
import dev.cdh.Behave
import dev.cdh.BubbleState
import dev.cdh.Direction
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.util.*
import javax.swing.JPanel
import kotlin.Boolean

class Stage(private val cat: Cat) : JPanel() {
    init {
        isDoubleBuffered = true
        setOpaque(false)
    }

    private fun needsFlipping(): Boolean {
        val action = cat.currentAction()
        val direction = cat.layingDir()
        return ((action == Behave.LAYING || action == Behave.RISING || action == Behave.SLEEP)
                && direction == Direction.LEFT)
                || (action == Behave.CURLED && direction == Direction.RIGHT)
    }

    private fun calculateBubblePosition(): Point {
        val calculator: PositionCalculator? = POSITION_CACHE.get(cat.currentAction())
        if (calculator != null) return calculator.calculate(cat.layingDir())
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
        val state = cat.animationState()
        val frames = cat.currentFrames()
        if (frames.isNullOrEmpty()) return
        var img = frames[state.frameNum()]
        if (needsFlipping()) {
            val flipKey = cat.catType() + ":" + cat.currentAction().name + ":" + state.frameNum()
            img = ImageCache.getOrFlip(img, flipKey)
        }
        g2d.drawImage(img, 0, 0, null)
    }

    private fun paintBubbleIfNeeded(g2d: Graphics2D) {
        if (cat.bubbleState() == BubbleState.NONE) return
        val frames = cat.currentBubbleFrames()
        if (frames.isNullOrEmpty()) return
        val state = cat.animationState()
        val bubble = frames[state.bubbleFrame()]
        val pos = calculateBubblePosition()
        g2d.drawImage(bubble, pos.x, pos.y, null)
    }

    private fun interface PositionCalculator {
        fun calculate(direction: Direction?): Point
    }

    companion object {
        private const val BASE_X = 30
        private const val BASE_Y = 40

        private val POSITION_CACHE: MutableMap<Behave?, PositionCalculator?> = createPositionCache()

        private fun createPositionCache(): MutableMap<Behave?, PositionCalculator?> {
            val cache = HashMap<Behave?, PositionCalculator?>(7)
            cache[Behave.SLEEP] =
                PositionCalculator { dir: Direction? -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
            cache[Behave.LAYING] =
                PositionCalculator { dir: Direction? -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
            cache[Behave.LEFT] =
                PositionCalculator { dir: Direction? -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
            cache[Behave.RIGHT] =
                PositionCalculator { dir: Direction? -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
            cache[Behave.UP] = PositionCalculator { Point(BASE_X, BASE_Y - 25) }
            cache[Behave.LICKING] = PositionCalculator { Point(BASE_X, BASE_Y - 25) }
            cache[Behave.SITTING] = PositionCalculator { Point(BASE_X, BASE_Y - 25) }
            return cache
        }
    }
}
