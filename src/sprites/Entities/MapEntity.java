package src.sprites.Entities;

import src.player.PlayerTeam;
import src.tools.Vector2D;

import java.awt.image.BufferedImage;

public class MapEntity extends Entity{
    PlayerTeam playerTeam;

    public MapEntity(final Vector2D position, final BufferedImage texture, final PlayerTeam playerTeam){
        super(position, new Vector2D(1,1), 0, texture);
        setEntityType(EntityType.OBSTACLE);
        this.playerTeam = playerTeam;
    }

    public PlayerTeam getPlayerTeam(){
        return playerTeam;
    }
}