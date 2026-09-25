package dev.cdh.affiliate

import dev.cdh.Behave
import dev.cdh.BubbleState
import dev.cdh.Direction
import dev.cdh.Movement
import dev.cdh.Movement.generateRandomTarget
import dev.cdh.State
import java.awt.Point
import java.awt.image.BufferedImage
import kotlin.math.abs
import kotlin.random.Random

class Cat(private val resourceLoader: ResourcesLoader) {
    val window: CatWindow = CatWindow(this)

    var currentAction = Behave.SLEEP
        private set
    var currentFrames: MutableList<BufferedImage>? = null
        private set
    var currentBubbleFrames: MutableList<BufferedImage>? = null
    var layingDir = Direction.RIGHT
        private set
    private var state = State.DEFAULT
    var bubbleState = BubbleState.NONE
        private set
    private val wanderTarget = Point(0, 0)
    val animationState = AnimationState()

    init {
        loadFramesForAction(currentAction)
        loadBubbleFrames(bubbleState)
    }

    fun update() {
        handleFrames()
        performMovement()
        updateAnimation()
        manageBubbleState()
        window.repaint()
    }

    fun changeAction(behave: Behave): Boolean {
        if (currentAction != behave) {
            currentAction = behave
            loadFramesForAction(behave)
            return true
        }
        return false
    }

    private fun loadFramesForAction(behave: Behave) {
        currentFrames = resourceLoader.loadFrames(behave)
    }

    private fun loadBubbleFrames(state: BubbleState?) {
        currentBubbleFrames = resourceLoader.loadBubbleFrames(state)
    }

    private fun updateAnimation() {
        animationState.incrementAnimationSteps()

        if (animationState.animationSteps() >= currentAction.delay) {
            if (shouldTransitionFromLaying()) {
                handleLayingTransition()
            } else if (shouldTransitionFromSitting()) {
                handleSittingTransition()
            } else {
                animationState.nextFrame()
            }
        }

        if (animationState.frameNum() >= currentAction.frame) {
            animationState.resetFrame()
        }
    }

    private fun shouldTransitionFromLaying(): Boolean {
        return currentAction == Behave.LAYING && animationState.frameNum() == currentAction.frame - 1
    }

    private fun handleLayingTransition() {
        // Switch after about 0.8 seconds (24 steps × 33ms), keeping in sync with the original rhythm
        if (animationState.animationSteps() - currentAction.delay > 24) {
            animationState.reset()
            changeAction(if (Random.nextBoolean()) Behave.CURLED else Behave.SLEEP)
        }
    }

    private fun shouldTransitionFromSitting(): Boolean {
        return currentAction == Behave.SITTING && animationState.frameNum() == currentAction.frame - 1
    }

    private fun handleSittingTransition() {
        changeAction(Behave.LICKING)
        animationState.reset()
    }

    private fun manageBubbleState() {
        if (bubbleState != BubbleState.HEART) {
            updateBubbleStateBasedOnAction()
        }

        animationState.incrementBubbleSteps()

        if (animationState.bubbleSteps() >= bubbleState.delay) {
            animationState.nextBubbleFrame()
        }

        if (animationState.bubbleFrame() >= bubbleState.frame) {
            animationState.resetBubbleFrame()
            if (bubbleState == BubbleState.HEART) {
                setBubbleState(BubbleState.NONE)
            }
        }
    }

    private fun updateBubbleStateBasedOnAction() {
        if (currentAction == Behave.SLEEP || currentAction == Behave.CURLED) {
            setBubbleState(BubbleState.ZZZ)
        } else if (currentAction != Behave.SITTING) {
            setBubbleState(BubbleState.NONE)
        }
    }

    private fun handleFrames() {
        if (currentAction == Behave.RISING) return

        if (state == State.WANDER) {
            handleWandering()
        }

        handleMovementActions()
    }

    private fun handleWandering() {
        val curPos = window.locationOnScreen
        if (wanderTarget.distance(curPos) < 3) {
            // Reach the target: stop wandering and return to standby mode to avoid
            // getting stuck at the edge of the screen
            state = State.DEFAULT
            if (isMovingAction(currentAction)) {
                changeAction(if (Random.nextBoolean()) Behave.LAYING else Behave.SITTING)
                animationState.resetFrame()
            }
            return
        }
        if (abs(curPos.x - wanderTarget.x) >= 3) {
            changeAction(if (curPos.x > wanderTarget.x) Behave.LEFT else Behave.RIGHT)
        } else {
            changeAction(if (curPos.y > wanderTarget.y) Behave.UP else Behave.DOWN)
        }
    }

    private fun handleMovementActions() {
        var flag = false
        when (currentAction) {
            Behave.LEFT -> layingDir = Direction.LEFT
            Behave.RIGHT -> layingDir = Direction.RIGHT
            Behave.UP, Behave.DOWN -> {
                if (state != State.WANDER) {
                    flag = if (Random.nextInt(3) >= 1) changeAction(Behave.LAYING)
                    else changeAction(Behave.SITTING)
                }
            }

            else -> {}
        }
        if (flag) animationState.resetFrame()
    }

    private fun performMovement() {
        val oldLoc = window.location
        val newLoc = Point(oldLoc)
        Movement.move(newLoc, currentAction)

        Movement.clampToScreen(newLoc, window.size)
        if (newLoc != oldLoc) {
            window.location = newLoc
        }
    }

    fun tryWandering() {
        if (Random.nextBoolean()) return

        state = State.WANDER
        val screenLoc = window.locationOnScreen
        val target = generateRandomTarget(screenLoc, window.size)
        wanderTarget.location = target
    }

    /**
     * Cancel roaming mode (for example, when the user picks up the kitten).
     */
    fun stopWandering() {
        state = State.DEFAULT
    }

    // Setters
    fun setBubbleState(state: BubbleState) {
        if (bubbleState != state) {
            bubbleState = state
            loadBubbleFrames(state)
            animationState.resetBubbleFrame()
        }
    }

    // Getters
    fun catType(): String {
        return resourceLoader.selectedCatType
    }

    companion object {
        private fun isMovingAction(behave: Behave?): Boolean {
            return behave == Behave.LEFT || behave == Behave.RIGHT || behave == Behave.UP || behave == Behave.DOWN
        }
    }
}