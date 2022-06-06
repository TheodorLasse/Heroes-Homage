package src.combat;

import src.Game;
import src.sprites.Sprite;

import javax.swing.*;
import java.awt.*;

public class CombatComponent extends JComponent {
    private final Game game;

    public CombatComponent(Game game){
        this.game = game;
    }

    @Override
    public Dimension getPreferredSize(){
        return game.getCombatScreenDimension();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // Draw all sprites
        for (Sprite sprite : game.getCombatSpriteIterator()) {
            sprite.draw(g, this);
        }
    }
}
