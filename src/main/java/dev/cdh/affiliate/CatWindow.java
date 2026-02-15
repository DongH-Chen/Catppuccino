package dev.cdh.affiliate;

import dev.cdh.constants.Behave;
import dev.cdh.constants.BubbleState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class CatWindow extends JWindow {
    private static final int WINDOW_SIZE = 100;
    private final Cat cat;

    public CatWindow(Cat cat) {
        this.cat = cat;
        setupWindow();
        setupMouseListeners();
        add(new Stage(cat));
    }

    private void setupWindow() {
        setType(Type.UTILITY);
        setSize(WINDOW_SIZE, WINDOW_SIZE);
        setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0));
    }

    private void setupMouseListeners() {
        MouseAdapter adapter = new MouseAdapter() {
            private final Point dragOffset = new Point(0, 0);

            @Override
            public void mousePressed(MouseEvent e) {
                dragOffset.setLocation(e.getX(), e.getY());
            }

            @Override
            public void mouseDragged(final MouseEvent e) {
                setLocation(e.getLocationOnScreen().x - dragOffset.x, e.getLocationOnScreen().y - dragOffset.y);
                if (cat.changeAction(Behave.RISING)) {
                    cat.animationState().resetFrame();
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (cat.currentAction() == Behave.RISING) {
                    cat.changeAction(Behave.LAYING);
                    cat.animationState().resetFrame();
                }
            }

            @Override
            public void mouseClicked(final MouseEvent e) {
                cat.setBubbleState(BubbleState.HEART);
            }
        };
        addMouseListener(adapter);
        addMouseMotionListener(adapter);
    }
}