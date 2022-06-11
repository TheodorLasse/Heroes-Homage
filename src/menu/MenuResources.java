package src.menu;

import src.Game;
import src.player.PlayerResources;
import src.player.Resource;
import src.tools.Vector2D;
import src.tools.image.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

public class MenuResources extends JLabel {
    final private Game game;
    final private BufferedImage resourcePanel = Game.imageLoader.getImage(ImageLoader.ImageName.RESOURCE_PANEL);
    final private EnumMap<Resource, Vector2D> resources;

    public MenuResources(Game game){
        this.game = game;
        resources = new EnumMap<>(Resource.class);
        //Don't like it, but it's better than using swing
        List<Vector2D> imagePositions = Arrays.asList(
                new Vector2D(85, 50),
                new Vector2D(27, 87),
                new Vector2D(145, 87),
                new Vector2D(85, 101),
                new Vector2D(27, 138),
                new Vector2D(85, 158),
                new Vector2D(145, 138));

        int i = 0;
        for (Resource r: Resource.values()){
            resources.put(r, imagePositions.get(i));
            i++;
        }
    }

    private BufferedImage drawPanelNumbers(){
        BufferedImage panelNumbers = new BufferedImage(resourcePanel.getWidth(), resourcePanel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics g = panelNumbers.getGraphics();
        g.setColor(Color.BLACK);
        PlayerResources playerResources = game.getCurrentPlayer().getPlayerResources();

        for (Resource r : Resource.values()){
            Vector2D position = resources.get(r);
            int value = playerResources.getValue(r);
            g.drawString(Integer.toString(value), (int)position.getX(), (int)position.getY());
        }
        return panelNumbers;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage panelNumbers = drawPanelNumbers();
        Dimension labelSize = this.getSize();
        int posX = (int)((labelSize.width - resourcePanel.getWidth()) * 0.5);
        int posY = (int)((labelSize.height - resourcePanel.getHeight()) * 0.5);
        g.drawImage(resourcePanel, posX, posY, null);
        g.drawImage(panelNumbers, posX, posY, null);
    }
}
