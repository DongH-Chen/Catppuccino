package me.cdh

import me.cdh.AnimationManager.bubbleFrame
import me.cdh.AnimationManager.bubbleState
import me.cdh.AnimationManager.changeAction
import me.cdh.AnimationManager.currentAction
import me.cdh.AnimationManager.frameNum
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JWindow

class CatWindow : JWindow() {
    init {
        type = Type.UTILITY
        val dim = Dimension(WINDOW_WIDTH, WINDOW_HEIGHT)
        preferredSize = dim
        minimumSize = dim
        setLocationRelativeTo(null)
        isAlwaysOnTop = true
        val mouseAdapter = object : MouseAdapter() {
            private var dragOffset: Point? = null
            override fun mousePressed(e: MouseEvent) {
                dragOffset = Point(e.x, e.y)
            }

            override fun mouseDragged(e: MouseEvent) {
                dragOffset?.let {
                    setLocation(e.locationOnScreen.x - it.x, e.locationOnScreen.y - it.y)
                }
                if (changeAction(Behave.RISING)) frameNum = 0
            }

            override fun mouseReleased(e: MouseEvent) {
                if (currentAction == Behave.RISING) {
                    changeAction(Behave.LAYING)
                    frameNum = 0
                }
            }

            override fun mouseClicked(e: MouseEvent) {
                bubbleState = BubbleState.HEART
                bubbleFrame = 0
            }
        }
        addMouseMotionListener(mouseAdapter)
        addMouseListener(mouseAdapter)
        background = Color(1.0f, 1.0f, 1.0f, 0.0f)
        isVisible = true
        add(Stage())
    }
}