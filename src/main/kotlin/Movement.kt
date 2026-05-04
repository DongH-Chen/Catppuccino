package dev.cdh

import dev.cdh.constants.Behave
import java.awt.*
import kotlin.math.abs
import kotlin.random.Random

const val WINDOW_SIZE = 100
val SCREEN_SIZE: Dimension
    get() {
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

fun Point.generateRandomTarget(windowSize: Dimension): Point {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    var target: Point
    do {
        target = Point(
            Random.nextInt(screenSize.width - windowSize.width - 20) + 10,
            Random.nextInt(screenSize.height - windowSize.height - 20) + 10
        )
    } while (abs(this.y - target.y) <= 400 &&
        abs(this.x - target.x) <= 400
    )

    return target
}

fun Point.clampToScreen(screenSize: Dimension, windowSize: Dimension) {
    when {
        this.x > screenSize.width - windowSize.width -> this.setLocation(screenSize.width - windowSize.width, this.y)
        this.x < -10 -> this.setLocation(-10, this.y)
        this.y > screenSize.height - windowSize.height -> this.setLocation(
            this.x,
            screenSize.height - windowSize.height
        )

        this.y < -35 -> this.setLocation(this.x, -35)
        else -> {}
    }
}

fun Point.move(action: Behave) {
    when (action) {
        Behave.RIGHT -> this.translate(1, 0)
        Behave.LEFT -> this.translate(-1, 0)
        Behave.UP -> this.translate(0, -1)
        Behave.DOWN -> this.translate(0, 1)
        else -> {}
    }
}