package dev.cdh.affiliate

import dev.cdh.Movement
import dev.cdh.constants.Behave
import dev.cdh.constants.BubbleState
import dev.cdh.constants.Direction
import dev.cdh.constants.State
import java.awt.Point
import java.awt.image.BufferedImage
import kotlin.math.abs
import kotlin.random.Random

class Cat(private val resourcesLoader: ResourcesLoader) {
    val window = CatWindow(this)

    var currentAction = Behave.SLEEP
    lateinit var currentFrames: MutableList<BufferedImage?>
    lateinit var currentBubbleFrames: MutableList<BufferedImage?>
    var layingDir = Direction.RIGHT
    private var state = State.DEFAULT
     var bubbleState = BubbleState.NONE
        set(value) {
            if (field != value) {
                field = value
                loadBubbleFrames(value)
                animationState.resetBubbleFrame()
            }
        }
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
        currentFrames = resourcesLoader.loadFrames(behave)!!
    }

    private fun loadBubbleFrames(state: BubbleState?) {
        currentBubbleFrames = resourcesLoader.loadBubbleFrames(state!!)!!
    }

    private fun updateAnimation() {
        animationState.incrementAnimationSteps()

        if (animationState.animationSteps >= currentAction.delay) {
            if (shouldTransitionFromLaying()) {
                handleLayingTransition()
            } else if (shouldTransitionFromSitting()) {
                handleSittingTransition()
            } else {
                animationState.nextFrame()
            }
        }

        if (animationState.frameNum >= currentAction.frame) {
            animationState.resetFrame()
        }
    }

    private fun shouldTransitionFromLaying(): Boolean {
        return currentAction == Behave.LAYING && animationState.frameNum == currentAction.frame - 1
    }

    private fun handleLayingTransition() {
        if (animationState.animationSteps - currentAction.delay > 40) {
            animationState.reset()
            changeAction(if (Random.nextBoolean()) Behave.CURLED else Behave.SLEEP)
        }
    }

    private fun shouldTransitionFromSitting(): Boolean {
        return currentAction == Behave.SITTING &&
                animationState.frameNum == currentAction.frame - 1
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

        if (animationState.bubbleSteps >= bubbleState.delay) {
            animationState.nextBubbleFrame()
        }

        if (animationState.bubbleFrame >= bubbleState.frame) {
            animationState.resetBubbleFrame()
            if (bubbleState == BubbleState.HEART) {
                bubbleState = BubbleState.NONE
            }
        }
    }

    private fun updateBubbleStateBasedOnAction() {
        if (currentAction == Behave.SLEEP || currentAction == Behave.CURLED) {
            bubbleState = BubbleState.ZZZ
        } else if (currentAction != Behave.SITTING) {
            bubbleState = BubbleState.NONE
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
        if (abs(curPos.x - wanderTarget.x) >= 3) {
            changeAction(if (curPos.x > wanderTarget.x) Behave.LEFT else Behave.RIGHT)
        } else {
            changeAction(if (curPos.y > wanderTarget.y) Behave.UP else Behave.DOWN)
        }
        state = if (wanderTarget.distance(curPos) < 3) State.DEFAULT else State.WANDER
    }

    private fun handleMovementActions() {
        var flag = false
        when {
            currentAction == Behave.LEFT -> layingDir = Direction.LEFT
            currentAction == Behave.RIGHT -> layingDir = Direction.RIGHT
            state != State.WANDER && ((currentAction == Behave.UP) or (currentAction == Behave.DOWN)) -> flag =
                if (Random.nextInt(3) >= 1) changeAction(Behave.LAYING) else changeAction(Behave.SITTING)
            else -> {}
        }
        if (flag) animationState.resetFrame()
    }

    private fun performMovement() {
        val loc = window.location
        Movement.move(loc, currentAction)

        Movement.clampToScreen(loc, Movement.SCREEN_SIZE, window.size)

        window.location = loc
    }

    fun tryWandering() {
        if (Random.nextBoolean()) return

        state = State.WANDER
        val screenLoc = window.locationOnScreen
        val target = Movement.generateRandomTarget(screenLoc, window.size)
        wanderTarget.location = target
    }
}