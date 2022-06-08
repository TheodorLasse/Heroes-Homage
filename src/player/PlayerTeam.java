package src.player;

import src.sprites.Entities.LivingEntities.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class PlayerTeam {
    private final PlayerResources playerResources;
    private final PlayerTeamColor teamColor;
    private final List<LivingEntity> playerMapLivingEntities;

    public PlayerTeam(PlayerTeamColor teamColor){
        this.playerResources = new PlayerResources();
        this.teamColor = teamColor;
        this.playerMapLivingEntities = new ArrayList<>();
    }

    public PlayerTeamColor getTeamColor() {
        return teamColor;
    }

    public PlayerResources getPlayerResources() {
        return playerResources;
    }
}
