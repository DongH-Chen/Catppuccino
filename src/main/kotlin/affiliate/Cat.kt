package dev.cdh.affiliate

import dev.cdh.Behave
import dev.cdh.BubbleState
import dev.cdh.Direction
import dev.cdh.Movement
import dev.cdh.Movement.generateRandomTarget
import dev.cdh.State
import java.awt.Point
import java.awt.image.BufferedImage
import java.util.random.RandomGenerator
import kotlin.math.abs

class Cat(private val resourceLoader: ResourcesLoader) {
    private val window: CatWindow = CatWindow(this)
    private val ran: RandomGenerator = RandomGenerator.getDefault()

    private var currentAction = Behave.SLEEP
    private var currentFrames: MutableList<BufferedImage?>? = null
    private var currentBubbleFrames: MutableList<BufferedImage?>? = null
    private var layingDir = Direction.RIGHT
    private var state = State.DEFAULT
    private var bubbleState = BubbleState.NONE
    private val wanderTarget = Point(0, 0)
    private val animationState = AnimationState()

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
            changeAction(if (ran.nextBoolean()) Behave.CURLED else Behave.SLEEP)
        }
    }

    private fun shouldTransitionFromSitting(): Boolean {
        return currentAction == Behave.SITTING &&
                animationState.frameNum() == currentAction.frame - 1
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
                changeAction(if (ran.nextBoolean()) Behave.LAYING else Behave.SITTING)
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
                    flag = if (ran.nextInt(3) >= 1)
                        changeAction(Behave.LAYING)
                    else
                        changeAction(Behave.SITTING)
                }
            }

            else -> {}
        }
        if (flag) animationState.resetFrame()
    }

    private fun performMovement() {
        val loc = window.location
        Movement.move(loc, currentAction)

        Movement.clampToScreen(loc, window.size)

        window.location = loc
    }

    fun tryWandering() {
        if (ran.nextBoolean()) return

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
        return resourceLoader.catType()
    }

    fun currentAction(): Behave {
        return currentAction
    }

    fun currentFrames(): MutableList<BufferedImage?>? {
        return currentFrames
    }

    fun currentBubbleFrames(): MutableList<BufferedImage?>? {
        return currentBubbleFrames
    }

    fun layingDir(): Direction {
        return layingDir
    }

    fun bubbleState(): BubbleState {
        return bubbleState
    }

    fun animationState(): AnimationState {
        return animationState
    }

    fun window(): CatWindow {
        return window
    }

    companion object {
        private fun isMovingAction(behave: Behave?): Boolean {
            return behave == Behave.LEFT || behave == Behave.RIGHT || behave == Behave.UP || behave == Behave.DOWN
        }
    }
}