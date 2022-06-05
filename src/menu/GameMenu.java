package src.menu;

import src.Game;
import src.sprites.Entities.EntityHandler;
import src.sprites.Sprite;
import src.sprites.SpriteHandler;
import src.sprites.SpriteLayer;
import src.tools.ImageLoader;
import src.tools.Vector2D;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class GameMenu {
    private Dimension screenSize;
    private final SpriteHandler menuSpriteHandler;
    private final EntityHandler menuEntityHandler;
    private final BufferedImage background;

    /**
     * Contains and controls the menu
     * @param screenSize Size of the screen allocated for GameMenu
     */
    public GameMenu(Dimension screenSize){
        this.screenSize = screenSize;
        menuSpriteHandler = new SpriteHandler();
        menuEntityHandler = new EntityHandler();
        background = new BufferedImage(screenSize.width, screenSize.height, TYPE_INT_ARGB);
        initBackground();
        menuSpriteHandler.setBackground(background);
    }
    /**
     * Initialize the background image
     */
    private void initBackground(){
        Graphics g = background.getGraphics();
        BufferedImage tile = Game.imageLoader.getImage(ImageLoader.ImageName.MENU_BACKGROUND);
        int tilesWide = (int) Math.ceil(screenSize.getWidth() / tile.getWidth());
        int tilesHeight = (int) Math.ceil(screenSize.getHeight() / tile.getHeight());
        for (int x = 0; x < tilesWide; x++){
            for (int y = 0; y < tilesHeight; y++){
                g.drawImage(tile, x * tile.getWidth(), y * tile.getHeight(), null);
            }
        }
    }

    /**
     *
     * @return Iterable of all of the Entity objects located in the GameMap
     */
    public ArrayList<Sprite> getIterator(){
        return menuSpriteHandler.getLayerIterator(SpriteLayer.FIRST);
    }

    public void onMouseClick(Vector2D mousePos, int button) {
    }
}
