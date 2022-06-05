package src;

import src.Game;
import src.sprites.Sprite;

import javax.swing.*;
import java.awt.*;

/**
 * Class for drawing on the JFrame created in the Game class.
 */
public class GameComponent extends JComponent
{
    private final Game game;

    public GameComponent(Game game){
        this.game = game;
    }

    @Override
    public Dimension getPreferredSize(){
        return game.getMapScreenDimension();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // Draw all sprites
        for (Sprite sprite : game.getGameSpriteIterator()) {
            sprite.draw(g, this);
        }
    }
}
