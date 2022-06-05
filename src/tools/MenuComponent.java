package src.tools;

import src.Game;
import src.menu.Minimap;
import src.sprites.Sprite;

import javax.swing.*;
import java.awt.*;

/**
 * Class for drawing on the JFrame created in the Game class.
 */
public class MenuComponent extends JComponent
{
    private final Game game;
    private JLabel minimap;
    private JLabel resources;

    public MenuComponent(Game game){
        this.game = game;

        minimap = new Minimap(game);
        this.add(minimap);
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
