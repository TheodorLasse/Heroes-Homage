package src;

import src.player.PlayerTeam;
import src.sprites.Entities.CombatLivingEntity;
import src.sprites.Entities.EntityHandler;
import src.tools.Vector2D;
import src.tools.image.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Army {
    private final PlayerTeam team;
    private final ArrayList<CombatLivingEntity> combatEntities;
    private final List<Vector2D> startingPositions;
    private EntityHandler combatEntityHandler;

    public Army(PlayerTeam team){
        this.team = team;
        combatEntities = new ArrayList<>();
        startingPositions = Arrays.asList(new Vector2D(),
                new Vector2D(0, 1), new Vector2D(0, 2), new Vector2D(0, 3),
                new Vector2D(0, 4), new Vector2D(0, 5), new Vector2D(0, 6));

        //combatEntities.add(new CombatLivingEntity(startingPositions.get(0), new Vector2D(1, 2), Game.imageLoader.getImage(ImageLoader.ImageName.NINJA_1), team));
        //combatEntities.add(new CombatLivingEntity(startingPositions.get(0), new Vector2D(2, 2), Game.imageLoader.getImage(ImageLoader.ImageName.NINJA_2), team));
        //combatEntities.add(new CombatLivingEntity(startingPositions.get(0), new Vector2D(1, 2), Game.imageLoader.getImage(ImageLoader.ImageName.NINJA_3), team));
    }

    public void setCombatEntityHandler(EntityHandler combatEntityHandler) {
        this.combatEntityHandler = combatEntityHandler;
        for (CombatLivingEntity entity : combatEntities){
            entity.setCombatEntityHandler(combatEntityHandler);
        }
    }

    public ArrayList<CombatLivingEntity> getCombatEntities() {
        return combatEntities;
    }

    public List<Vector2D> getStartingPositions() {
        return startingPositions;
    }
}
