package src.combat;

import src.Game;
import src.tools.Vector2D;
import src.tools.image.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CombatSpriteFactory {
    private final Dimension screenSize;
    private final BufferedImage top = Game.imageLoader.getImage(ImageLoader.ImageName.COMBAT_TOP);
    private final BufferedImage bottom = Game.imageLoader.getImage(ImageLoader.ImageName.COMBAT_BOTTOM);
    private final int gridOffsetY = top.getHeight() - bottom.getHeight();

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

        BufferedImage grid = getCombatGrid();
        g.drawImage(grid, (int)((screenSize.width - grid.getWidth()) * 0.5), gridOffsetY, null);

        return background;
    }

    private BufferedImage getCombatGrid(){
        int height = GameCombat.ARENA_SIZE.height;
        int width = GameCombat.ARENA_SIZE.width;
        int gridSquareLength = getGridSquareLength();

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

    public int getGridSquareLength(){
        return (int)(screenSize.getHeight() - gridOffsetY)/GameCombat.ARENA_SIZE.height;
    }

    public Vector2D getGridOffset(){
        int gridSquareLength = (int)(screenSize.getHeight() - gridOffsetY)/GameCombat.ARENA_SIZE.height;
        int width = GameCombat.ARENA_SIZE.width;
        int gridOffsetX = (int)((screenSize.width - gridSquareLength * width) * 0.5);
        return new Vector2D(gridOffsetX, gridOffsetY);
    }
}
