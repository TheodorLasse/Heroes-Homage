package src;

import src.map.MapFocus;
import src.player.PlayerTeam;
import src.sprites.Entities.CombatEntity;
import src.tools.Vector2D;
import src.tools.image.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Army {
    private final PlayerTeam team;
    private final ArrayList<CombatEntity> combatEntities;
    private final List<Vector2D> startingPositions;

    public Army(PlayerTeam team){
        this.team = team;
        combatEntities = new ArrayList<>();
        startingPositions = Arrays.asList(new Vector2D(),
                new Vector2D(0, 1), new Vector2D(0, 2), new Vector2D(0, 3),
                new Vector2D(0, 4), new Vector2D(0, 5), new Vector2D(0, 6));

        combatEntities.add(new CombatEntity(startingPositions.get(0), new Vector2D(1, 1), Game.imageLoader.getImage(ImageLoader.ImageName.ROCK)));
    }

    public ArrayList<CombatEntity> getCombatEntities() {
        return combatEntities;
    }

    public List<Vector2D> getStartingPositions() {
        return startingPositions;
    }
}
