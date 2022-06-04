package src.tools.input;

import src.Game;
import src.tools.GameComponent;
import src.tools.MenuComponent;
import src.tools.Vector2D;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener {

    public MouseListener(final JComponent pane, Game game) {
        pane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

                Vector2D mousePos = new Vector2D(e.getX(), e.getY());
                if(pane == game.gameComponent){
                    game.mouseMapClick(mousePos, e.getButton());
                }
                else if (pane == game.menuComponent){
                    game.mouseMenuClick(mousePos);
                }

            }
        });
    }
}
