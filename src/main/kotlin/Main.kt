package me.cdh

import kotlinx.coroutines.*
import java.time.LocalDateTime
import javax.swing.SwingUtilities

object Main {
    private fun initSystemTrayAndCat() {
        CatTray.initSystemTray()
        AnimationManager.changeAction(Behave.CURLED)
    }

    private suspend fun CoroutineScope.startMainAnimationLoop() {
        while (isActive) {
            AnimationManager.handleFrames()
            AnimationManager.performMovement()
            AnimationManager.updateAnimation()
            AnimationManager.manageBubbleState()
            AnimationManager.win.repaint()
            delay(ANIMATION_UPDATE_DELAY)
        }
    }

    private suspend fun CoroutineScope.startWanderingBehavior() {
        val wanderInterval =
            if (LocalDateTime.now().hour in 8..18) DAYTIME_WANDER_INTERVAL else NIGHTTIME_WANDER_INTERVAL
        while (isActive) {
            AnimationManager.tryWandering()
            delay(wanderInterval)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking(Dispatchers.Default.limitedParallelism(1)) {
            SwingUtilities.invokeLater { initSystemTrayAndCat() }
            launch { startMainAnimationLoop() }
            launch { startWanderingBehavior() }
        }
    }
}