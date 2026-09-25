package dev.cdh

import java.awt.Dimension
import java.awt.GraphicsEnvironment
import java.awt.Point
import java.awt.Rectangle
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

object Movement {
    // The virtual desktop boundaries made up of all monitors
    // may include negative coordinates when using multiple monitors.
    val VIRTUAL_BOUNDS: Rectangle = calculateVirtualScreenBounds()

    private const val EDGE_OVERFLOW_X = 10
    private const val EDGE_OVERFLOW_Y = 35
    private const val MIN_TARGET_DISTANCE = 400
    private const val MAX_TARGET_ATTEMPTS = 100

    private fun calculateVirtualScreenBounds(): Rectangle {
        var virtualBounds = Rectangle()
        val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        val screens = ge.screenDevices
        for (screen in screens) {
            val config = screen.defaultConfiguration
            virtualBounds = virtualBounds.union(config.bounds)
        }
        if (virtualBounds.isEmpty) {
            virtualBounds.setSize(1920, 1080)
        }
        return virtualBounds
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

    fun clampToScreen(location: Point, windowSize: Dimension) {
        val minX = VIRTUAL_BOUNDS.x
        val minY = VIRTUAL_BOUNDS.y
        val maxX = VIRTUAL_BOUNDS.x + VIRTUAL_BOUNDS.width - windowSize.width
        val maxY = VIRTUAL_BOUNDS.y + VIRTUAL_BOUNDS.height - windowSize.height

        if (location.x > maxX) {
            location.x = maxX
        }
        if (location.x < minX - EDGE_OVERFLOW_X) {
            location.x = minX - EDGE_OVERFLOW_X
        }
        if (location.y > maxY) {
            location.y = maxY
        }
        if (location.y < minY - EDGE_OVERFLOW_Y) {
            location.y = minY - EDGE_OVERFLOW_Y
        }
    }

    fun generateRandomTarget(currentPos: Point, windowSize: Dimension): Point {
        val width = max(1, VIRTUAL_BOUNDS.width - windowSize.width - 20)
        val height = max(1, VIRTUAL_BOUNDS.height - windowSize.height - 20)

        var target: Point
        var attempts = 0
        do {
            target = Point(
                VIRTUAL_BOUNDS.x + Random.nextInt(width) + 10,
                VIRTUAL_BOUNDS.y + Random.nextInt(height) + 10
            )
            attempts++
        } while (attempts < MAX_TARGET_ATTEMPTS && abs(currentPos.y - target.y) <= MIN_TARGET_DISTANCE && abs(currentPos.x - target.x) <= MIN_TARGET_DISTANCE)

        return target
    }
}
