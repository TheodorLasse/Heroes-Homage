package src.menu;

import src.Game;
import src.map.GameMap;
import src.tools.Vector2D;
import src.tools.image.BufferedImageResize;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


public class Minimap extends JLabel {
    private final GameMap gameMap;
    private final BufferedImage minimapImage;

    private final static int MINIMAP_OFFSET = 40;
    /**
     * The minimap of the game, located on the gameMenu component
     * @param game The main game object
     */
    public Minimap(Game game){
        int minimapSize = game.getMenuScreenDimension().width - MINIMAP_OFFSET * 2;

        this.gameMap = game.getGameMap();
        minimapImage = BufferedImageResize.resize(gameMap.getBackground(), minimapSize, minimapSize);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                double percentX = (double)e.getX() / minimapSize;
                double percentY = (double)e.getY() / minimapSize;
                Dimension gameMapDim = gameMap.getMapSize();
                double mapFocusX = percentX * gameMapDim.width;
                double mapFocusY = percentY * gameMapDim.height;

                gameMap.setMapFocusCentre(new Vector2D(mapFocusX, mapFocusY));
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension labelSize = this.getSize();
        int posX = (int)((labelSize.width - minimapImage.getWidth()) * 0.5);
        int posY = (int)((labelSize.height - minimapImage.getHeight()) * 0.5);
        g.drawImage(minimapImage, posX, posY, null);
    }
}
