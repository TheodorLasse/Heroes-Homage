package src.menu;

import src.Game;
import src.sprites.Sprite;
import src.tools.image.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Class for drawing on the JFrame created in the Game class.
 */
public class MenuComponent extends JComponent
{
    private final Game game;
    private final BufferedImage menuBackground;

    public MenuComponent(Game game){
        this.game = game;

        menuBackground = new MenuBackground(getPreferredSize()).getMenuBackground();

        GridBagLayout grid = new GridBagLayout();
        this.setLayout(grid);
        this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 300;
        c.weightx = 0.5;
        c.gridwidth = 4;
        c.gridx = 0;
        c.gridy = 0;

        JLabel minimap = new Minimap(game);
        this.add(minimap, c);

        c.ipady = 200;
        c.gridy = 1;

        JLabel resources = new MenuResources(game);
        this.add(resources, c);
    }



    @Override
    public Dimension getPreferredSize(){
        return game.getMenuScreenDimension();
    }

    @Override
    protected void paintComponent(Graphics g){
        g.drawImage(menuBackground, 0, 0, null);
        super.paintComponent(g);
    }
}
