package dev.cdh.affiliate

import dev.cdh.WINDOW_SIZE
import dev.cdh.constants.Behave
import dev.cdh.constants.BubbleState
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JWindow

class CatWindow(private val cat: Cat) : JWindow() {
    init {
        setupWindow()
        setupMouseListeners()
        add(Stage(cat))
    }

    private fun setupWindow() {
        setType(Type.UTILITY)
        setSize(WINDOW_SIZE, WINDOW_SIZE)
        setPreferredSize(Dimension(WINDOW_SIZE, WINDOW_SIZE))
        setLocationRelativeTo(null)
        setAlwaysOnTop(true)
        setBackground(Color(0, 0, 0, 0))
    }

    private fun setupMouseListeners() {
        val adapter: MouseAdapter = object : MouseAdapter() {
            private val dragOffset = Point(0, 0)

            override fun mousePressed(e: MouseEvent) {
                dragOffset.setLocation(e.getX(), e.getY())
            }

            override fun mouseDragged(e: MouseEvent) {
                setLocation(e.locationOnScreen.x - dragOffset.x, e.locationOnScreen.y - dragOffset.y)
                if (cat.changeAction(Behave.RISING)) {
                    cat.animationState.resetFrame()
                }
            }

            override fun mouseReleased(e: MouseEvent?) {
                if (cat.currentAction == Behave.RISING) {
                    cat.changeAction(Behave.LAYING)
                    cat.animationState.resetFrame()
                }
            }

            override fun mouseClicked(e: MouseEvent?) {
                cat.bubbleState = BubbleState.HEART
            }
        }
        addMouseListener(adapter)
        addMouseMotionListener(adapter)
    }
}