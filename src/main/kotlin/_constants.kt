package dev.cdh

interface Animate {
    val frame: Int
    val delay: Int
}

enum class Behave(override val frame: Int, override val delay: Int) : Animate {
    UP(4, 6),
    DOWN(4, 6),
    LEFT(4, 6),
    RIGHT(4, 6),
    CURLED(2, 24),
    LAYING(4, 12),
    SITTING(4, 12),
    LICKING(4, 24),
    RISING(2, 24),
    SLEEP(1, 6)
}

enum class BubbleState(override val frame: Int, override val delay: Int) : Animate {
    ZZZ(4, 18),
    HEART(4, 30),
    NONE(-1, -1)
}

enum class Direction {
    RIGHT, LEFT
}

enum class State {
    DEFAULT, WANDER
}