package src.tools;

import src.Game;
import src.menu.Minimap;
import src.menu.MenuResources;
import src.sprites.Sprite;

import javax.swing.*;
import java.awt.*;

/**
 * Class for drawing on the JFrame created in the Game class.
 */
public class MenuComponent extends JComponent
{
    private final Game game;
    private JLabel resources;

    public MenuComponent(Game game){
        this.game = game;

        GridBagLayout grid = new GridBagLayout();
        this.setLayout(grid);
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 400;
        c.weightx = 0.5;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 0;

        JLabel minimap = new Minimap(game);
        this.add(minimap, c);

        JLabel resources = new MenuResources();
    }

    @Override
    public Dimension getPreferredSize(){
        return game.getMenuScreenDimension();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // Draw all sprites
        for (Sprite sprite : game.getMenuSpriteIterator()) {
            sprite.draw(g, this);
        }
    }
}
