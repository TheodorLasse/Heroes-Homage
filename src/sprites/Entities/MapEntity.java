package src.sprites.Entities;

import src.map.GameMap;
import src.player.PlayerTeam;
import src.sprites.Entities.Entity;
import src.sprites.Entities.EntityType;
import src.tools.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MapEntity extends Entity {
    protected PlayerTeam playerTeam;

    public MapEntity(final Vector2D position, final BufferedImage texture, final PlayerTeam playerTeam){
        super(position, new Vector2D(1,1), 0, texture);
        setEntityType(EntityType.OBSTACLE);
        this.playerTeam = playerTeam;
    }

    public PlayerTeam getPlayerTeam(){
        return playerTeam;
    }
}