package src.menu;

import src.Game;
import src.tools.image.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class MenuBackground {
    private final BufferedImage menuBackground;

    MenuBackground(Dimension screenSize){
        menuBackground = initBackground(screenSize);
    }

    public BufferedImage getMenuBackground() {
        return menuBackground;
    }

    /**
     * Initialize the background image
     */
    private BufferedImage initBackground(Dimension screenSize){
        BufferedImage background = new BufferedImage(screenSize.width, screenSize.height, TYPE_INT_ARGB);
        Graphics g = background.getGraphics();
        BufferedImage tile = Game.imageLoader.getImage(ImageLoader.ImageName.MENU_BACKGROUND);
        int tilesWide = (int) Math.ceil(screenSize.getWidth() / tile.getWidth());
        int tilesHeight = (int) Math.ceil(screenSize.getHeight() / tile.getHeight());
        for (int x = 0; x < tilesWide; x++){
            for (int y = 0; y < tilesHeight; y++){
                g.drawImage(tile, x * tile.getWidth(), y * tile.getHeight(), null);
            }
        }
        return background;
    }
}
