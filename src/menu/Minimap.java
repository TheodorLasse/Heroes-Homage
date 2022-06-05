package src.menu;

import src.Game;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Minimap extends JLabel {
    /**
     * The minimap of the game, located on the gameMenu component
     * @param game The main game object
     */
    public Minimap(Game game){
        this.setBorder(new LineBorder(Color.RED, 2));
        int minimapOffset = 40;
        int minimapSize = game.getMenuScreenDimension().width - minimapOffset * 2;
        this.setBounds(minimapOffset, minimapOffset, minimapSize, minimapSize);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);

            }
        });
    }
}
