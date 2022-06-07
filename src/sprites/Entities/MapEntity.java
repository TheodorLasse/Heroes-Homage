package src.sprites.Entities;

import src.map.GameMap;
import src.player.PlayerTeam;
import src.tools.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MapEntity extends Entity{
    protected PlayerTeam playerTeam;

    public MapEntity(final Vector2D position, final BufferedImage texture, final PlayerTeam playerTeam){
        super(position, new Vector2D(1,1), 0, texture);
        setEntityType(EntityType.OBSTACLE);
        this.playerTeam = playerTeam;
    }

    public PlayerTeam getPlayerTeam(){
        return playerTeam;
    }

    @Override public void draw(final Graphics g, final JComponent gc) {
        g.drawImage(getTexture(), (int) relativePosition.getX() * GameMap.TILE_SIZE,
                (int) relativePosition.getY() * GameMap.TILE_SIZE, gc);
    }
}