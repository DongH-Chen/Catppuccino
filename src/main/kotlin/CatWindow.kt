package me.cdh

import me.cdh.CatAnimationManager.bubbleFrame
import me.cdh.CatAnimationManager.bubbleState
import me.cdh.CatAnimationManager.changeAction
import me.cdh.CatAnimationManager.currentAction
import me.cdh.CatAnimationManager.frameNum
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
            private var dragOffset: Point = Point(0, 0)
            override fun mousePressed(e: MouseEvent) {
                dragOffset = Point(e.x, e.y)
            }

            override fun mouseDragged(e: MouseEvent) {
                setLocation(e.locationOnScreen.x - dragOffset.x, e.locationOnScreen.y - dragOffset.y)
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