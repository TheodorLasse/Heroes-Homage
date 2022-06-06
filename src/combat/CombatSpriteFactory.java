package src.combat;

import src.Game;
import src.tools.image.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CombatSpriteFactory {
    private final Dimension screenSize;
    private final BufferedImage top = Game.imageLoader.getImage(ImageLoader.ImageName.COMBAT_TOP);
    private final BufferedImage bottom = Game.imageLoader.getImage(ImageLoader.ImageName.COMBAT_BOTTOM);

    public CombatSpriteFactory(Dimension screenSize){
        this.screenSize = screenSize;
    }

    public BufferedImage getCombatBackground(){
        BufferedImage background = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = background.getGraphics();

        int backgroundWidth = top.getWidth(); //top width = bottom width assumed

        int numberImagesWide = (int)Math.ceil((double)background.getWidth() / backgroundWidth);
        int numberImagesHigh = (int)Math.ceil((double)(background.getHeight() - top.getHeight()) / bottom.getHeight());
        for (int i = 0; i < numberImagesWide; i++) {
            g.drawImage(top, i * top.getWidth(), 0, null);
        }

        for (int j = 0; j < numberImagesWide; j++) {
            for (int k = 0; k < numberImagesHigh; k++) {
                g.drawImage(bottom, j * bottom.getWidth(), top.getHeight() + k * bottom.getHeight(), null);
            }
        }

        int gridOffset = top.getHeight() - bottom.getHeight();
        BufferedImage grid = getCombatGrid();
        g.drawImage(grid, (int)((screenSize.width - grid.getWidth()) * 0.5), gridOffset, null);

        return background;
    }

    public BufferedImage getCombatGrid(){
        int height = GameCombat.ARENA_SIZE.height;
        int width = GameCombat.ARENA_SIZE.width;
        int gridOffset = top.getHeight() - bottom.getHeight();
        int gridSquareLength = (int)(screenSize.getHeight() - gridOffset)/GameCombat.ARENA_SIZE.height;

        BufferedImage grid = new BufferedImage(gridSquareLength * width, gridSquareLength * height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = (Graphics2D) grid.getGraphics();
        float thickness = 2;
        g2.setStroke(new BasicStroke(thickness));
        g2.setColor(Color.BLACK);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                g2.drawRect(x * gridSquareLength, y * gridSquareLength, gridSquareLength, gridSquareLength);
            }
        }
        return grid;
    }
}
