package src.player;

import src.sprites.entities.livingEntities.MapLivingEntity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerTeam {
    private final PlayerResources playerResources;
    private final PlayerTeamColor teamColor;
    private final List<MapLivingEntity> playerMapLivingEntities;

    public PlayerTeam(PlayerTeamColor teamColor){
        this.playerResources = new PlayerResources();
        this.teamColor = teamColor;
        this.playerMapLivingEntities = new ArrayList<>();
    }

    public PlayerTeamColor getTeamColor() {
        return teamColor;
    }

    public Color getColor(){
        Color teamColor = null;
        switch (this.teamColor){
            case RED -> teamColor = Color.RED;
            case BLUE -> teamColor = Color.BLUE;
            case PINK -> teamColor = Color.PINK;
            case TEAL -> teamColor = Color.CYAN;
            case GREEN -> teamColor = Color.green;
            case ORANGE -> teamColor = Color.orange;
            case PURPLE -> teamColor = Color.magenta;
            case YELLOW -> teamColor = Color.yellow;
        }
        return teamColor;
    }

    public PlayerResources getPlayerResources() {
        return playerResources;
    }
}
