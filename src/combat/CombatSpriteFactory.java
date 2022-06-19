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
    private int gridOffsetX;

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
        gridOffsetX = (int)((screenSize.width - grid.getWidth()) * 0.5);
        g.drawImage(grid, gridOffsetX, gridOffsetY, null);

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

    /**
     * Returns the shaded area which illustrates where an entity can move
     * @param moveMap the list of values which determines which grid squares get shaded
     * @return BufferedImage of shaded area
     */
    public BufferedImage getMovementShade(boolean[][] moveMap){
        int gridSquareLength = getGridSquareLength();

        BufferedImage allowedMovement = new BufferedImage(
                screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) allowedMovement.getGraphics();
        float opacity = 0.5f;
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g2.setColor(Color.LIGHT_GRAY);

        for (int x = 0; x < moveMap.length; x++) {
            for (int y = 0; y < moveMap[x].length; y++) {
                if (!moveMap[x][y]) {
                    int posX = gridOffsetX + x * gridSquareLength;
                    int posY = gridOffsetY + y * gridSquareLength;
                    g2.fillRect(posX, posY, gridSquareLength, gridSquareLength);
                }
            }
        }



        return allowedMovement;
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
