package dev.cdh.affiliate

import dev.cdh.constants.Behave
import java.time.LocalDateTime
import javax.swing.Timer

class CatController(private val cat: Cat) {
    private var wanderCount = 0
    private val wanderInterval: Int by lazy(LazyThreadSafetyMode.NONE) {
        val hour = LocalDateTime.now().hour
        if (hour in 8..<18) 600 else 3000
    }

    fun start() {
        cat.window.isVisible = true
        cat.changeAction(Behave.CURLED)
        Timer(20) {
            cat.update()
            if (++wanderCount >= wanderInterval) {
                cat.tryWandering()
                wanderCount = 0
            }
        }.start()
    }
}