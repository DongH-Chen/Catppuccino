package dev.cdh

import dev.cdh.constants.Behave
import java.awt.*
import kotlin.math.abs
import kotlin.random.Random

object Movement {
    val SCREEN_SIZE: Dimension = calculateVirtualScreenBounds()

    private fun calculateVirtualScreenBounds(): Dimension {
        var virtualBounds = Rectangle()
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val screens = ge.screenDevices
        for (screen in screens) {
            val config = screen.defaultConfiguration
            virtualBounds = virtualBounds.union(config.bounds)
        }
        val result = Dimension()
        result.setSize(virtualBounds.getWidth(), virtualBounds.getHeight())
        return result
    }

    fun move(location: Point, action: Behave) {
        when (action) {
            Behave.RIGHT -> location.translate(1, 0)
            Behave.LEFT -> location.translate(-1, 0)
            Behave.UP -> location.translate(0, -1)
            Behave.DOWN -> location.translate(0, 1)
            else -> {}
        }
    }

    fun clampToScreen(location: Point, screenSize: Dimension, windowSize: Dimension) {
        when {
            location.x > screenSize.width - windowSize.width-> location.setLocation(screenSize.width - windowSize.width, location.y)
            location.x < -10 -> location.setLocation(-10, location.y)
            location.y > screenSize.height - windowSize.height -> location.setLocation(location.x, screenSize.height - windowSize.height)
            location.y < -35 -> location.setLocation(location.x, -35)
            else -> {}
        }
    }

    fun generateRandomTarget(currentPos: Point, windowSize: Dimension): Point {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        var target: Point
        do {
            target = Point(
                Random.nextInt(screenSize.width - windowSize.width - 20) + 10,
                Random.nextInt(screenSize.height - windowSize.height - 20) + 10
            )
        } while (abs(currentPos.y - target.y) <= 400 &&
            abs(currentPos.x - target.x) <= 400
        )

        return target
    }
}