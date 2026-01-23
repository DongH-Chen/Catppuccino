package me.cdh

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import java.time.LocalDateTime

object Main {
    private fun initSystemTrayAndCat() {
        CatTray.initSystemTray()
        CatManager.changeAction(Behave.CURLED)
    }

    private suspend fun CoroutineScope.startMainAnimationLoop() {
        while (isActive) {
            CatManager.handleFrames()
            CatManager.performMovement()
            CatManager.updateAnimation()
            CatManager.manageBubbleState()
            CatManager.win.repaint()
            delay(ANIMATION_UPDATE_DELAY)
        }
    }

    private suspend fun CoroutineScope.startWanderingBehavior() {
        val wanderInterval = if (LocalDateTime.now().hour in 8..18) DAYTIME_WANDER_INTERVAL
        else NIGHTTIME_WANDER_INTERVAL
        while (isActive) {
            CatManager.tryWandering()
            delay(wanderInterval)
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        runBlocking(Dispatchers.Default.limitedParallelism(1)) {
            launch(Dispatchers.Swing) { initSystemTrayAndCat() }
            launch { startMainAnimationLoop() }
            launch { startWanderingBehavior() }
        }
    }
}