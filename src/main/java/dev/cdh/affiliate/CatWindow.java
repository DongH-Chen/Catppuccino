package dev.cdh.affiliate;

import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static dev.cdh.affiliate.CatManager.*;

public final class CatWindow extends JWindow {
    private static final int WINDOW_HEIGHT = 100,
    WINDOW_WIDTH = 100;
    public CatWindow() {
        setType(Type.UTILITY);
        var dim = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
        setPreferredSize(dim);
        setMinimumSize(dim);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private final Point dragOffset = new Point(0, 0);

            @Override
            public void mousePressed(MouseEvent e) {
                dragOffset.setLocation(e.getX(), e.getY());
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                setLocation(e.getLocationOnScreen().x - dragOffset.x, e.getLocationOnScreen().y - dragOffset.y);
                if (changeAction(Behave.RISING)) frameNum = 0;
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (currentAction == Behave.RISING) {
                    changeAction(Behave.LAYING);
                    frameNum = 0;
                }
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                bubbleState = BubbleState.HEART;
                bubbleFrame = 0;
            }
        };
        addMouseMotionListener(mouseAdapter);
        addMouseListener(mouseAdapter);
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        add(new Stage());
    }
}