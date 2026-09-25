package dev.cdh.affiliate

import dev.cdh.Behave
import java.time.LocalDateTime
import javax.swing.Timer

class CatController(private val cat: Cat) {
    private var wanderCount = 0
    private val wanderInterval: Int

    init {
        val hour = LocalDateTime.now().hour
        // During the day (9 AM - 5 PM), tries to wander about every 12 seconds;
        // at night, about every 60 seconds
        this.wanderInterval = if (hour in 9..<18) 360 else 1800
    }

    fun start() {
        cat.window().isVisible = true
        cat.changeAction(Behave.CURLED)
        Timer(TICK_MS) {
            cat.update()
            if (++wanderCount >= wanderInterval) {
                cat.tryWandering()
                wanderCount = 0
            }
        }.start()
    }

    companion object {
        /** Animation timer interval (milliseconds), about 30 FPS.  */
        private const val TICK_MS = 33
    }
}
