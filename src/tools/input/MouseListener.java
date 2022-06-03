package src.tools.input;

import src.Game;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener {

    public MouseListener(final JComponent pane, Game game) {
        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                game.mouseClick(e.getX(), e.getY());
            }
        });
    }
}
