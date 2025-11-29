package me.cdh

import kotlinx.coroutines.*
import kotlinx.coroutines.swing.Swing
import java.time.LocalDateTime

private fun initSystemTrayAndCat() {
    CatTray.initSystemTray()
    CatAnimationManager.changeAction(Behave.CURLED)
}

private suspend fun CoroutineScope.startMainAnimationLoop() {
    while (isActive) {
        CatAnimationManager.handleFrames()
        CatAnimationManager.performMovement()
        CatAnimationManager.updateAnimation()
        CatAnimationManager.manageBubbleState()
        CatAnimationManager.win.repaint()
        delay(ANIMATION_UPDATE_DELAY)
    }
}

private suspend fun CoroutineScope.startWanderingBehavior() {
    val wanderInterval = if (LocalDateTime.now().hour in 8..18) DAYTIME_WANDER_INTERVAL
    else NIGHTTIME_WANDER_INTERVAL
    while (isActive) {
        CatAnimationManager.tryWandering()
        delay(wanderInterval)
    }
}

fun main() {
    runBlocking(Dispatchers.Default.limitedParallelism(1)) {
        launch(Dispatchers.Swing) { initSystemTrayAndCat() }
        launch { startMainAnimationLoop() }
        launch { startWanderingBehavior() }
    }
}
