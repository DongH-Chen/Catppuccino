package dev.cdh.affiliate

import dev.cdh.ImageCache
import dev.cdh.Behave
import dev.cdh.BubbleState
import dev.cdh.Direction
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Point
import java.awt.RenderingHints
import java.util.*
import javax.swing.JPanel
import kotlin.Boolean

class Stage(private val cat: Cat) : JPanel() {

    private val needsFlipping: Boolean
        get() = when (cat.currentAction) {
            Behave.LAYING, Behave.RISING, Behave.SLEEP -> cat.layingDir == Direction.LEFT
            Behave.CURLED -> cat.layingDir == Direction.RIGHT
            else -> false
        }

    init {
        isDoubleBuffered = true
        isOpaque = false
    }

    private fun calculateBubblePosition(): Point {
        val calculator = POSITION_CACHE[cat.currentAction]
        if (calculator != null) return calculator(cat.layingDir)
        return Point(BASE_X, BASE_Y)
    }

    override fun paintComponent(g: Graphics) {
        val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR)
        paintCat(g2d)
        paintBubbleIfNeeded(g2d)
    }

    private fun paintCat(g2d: Graphics2D) {
        val state = cat.animationState
        val frames = cat.currentFrames ?: return
        var img = frames[state.frameNum]
        if (needsFlipping) {
            val flipKey = "${cat.catType()}:${cat.currentAction.name}:${state.frameNum}"
            img = ImageCache.getOrFlip(img, flipKey)
        }
        g2d.drawImage(img, 0, 0, null)
    }

    private fun paintBubbleIfNeeded(g2d: Graphics2D) {
        if (cat.bubbleState == BubbleState.NONE) return
        val frames = cat.currentBubbleFrames ?: return
        if (frames.isEmpty()) return
        val state = cat.animationState
        val bubble = frames[state.bubbleFrame]
        val pos = calculateBubblePosition()
        g2d.drawImage(bubble, pos.x, pos.y, null)
    }

    companion object {
        private const val BASE_X = 30
        private const val BASE_Y = 40

        private val POSITION_CACHE: MutableMap<Behave, (Direction) -> Point> = createPositionCache()

        private fun createPositionCache(): MutableMap<Behave, (Direction) -> Point> {
            val cache = EnumMap<Behave, (Direction) -> Point>(Behave::class.java)
            cache[Behave.SLEEP] =
                { dir: Direction? -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
            cache[Behave.LAYING] =
                { dir: Direction? -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
            cache[Behave.LEFT] =
                { dir: Direction? -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
            cache[Behave.RIGHT] =
                { dir: Direction? -> Point(if (dir == Direction.LEFT) 0 else BASE_X + 30, BASE_Y) }
            cache[Behave.UP] = { Point(BASE_X, BASE_Y - 25) }
            cache[Behave.LICKING] = { Point(BASE_X, BASE_Y - 25) }
            cache[Behave.SITTING] = { Point(BASE_X, BASE_Y - 25) }
            return cache
        }
    }
}
