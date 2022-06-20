package src.sprites.entities.livingEntities;

import src.Army;
import src.Game;
import src.player.PlayerTeam;
import src.sprites.entities.CollectableMapEntity;
import src.sprites.entities.Entity;
import src.sprites.entities.EntityHandler;
import src.tools.Vector2D;
import src.tools.WindowFocus;
import src.tools.aStar.Path;
import src.tools.aStar.PathFinder;
import src.tools.aStar.PathMap;
import src.tools.time.DeltaTime;

public class MapLivingEntity extends LivingEntity {
    protected final Game game;
    protected Path queuedPath;
    protected Army army;
    /**
     * A unit on the GameMap that can move, belongs to a team and has an army
     *
     * @param position         Entity's position
     * @param character        Entity's character, i.e list of BufferedImages
     * @param game             The game object which this Entity calls on for starting combat
     * @param team             Entity's team
     * @param mapEntityHandler EntityHandler which keeps track of Entities on the GameMap
     */
    public MapLivingEntity(Vector2D position, Character.CharacterEnum character, Game game, PlayerTeam team, EntityHandler mapEntityHandler) {
        super(position, character, team, mapEntityHandler);
        this.game = game;
        this.army = new Army(team);
    }

    @Override
    protected void move(DeltaTime deltaTime, WindowFocus focus) {
        super.move(deltaTime, focus);
        if (!isInactive()) queuedPath = path;
    }

    @Override
    protected void updateRotation(Vector2D direction){
        super.updateRotation(direction);
        this.rotation.addRadians(Math.PI);
    }

    @Override
    public boolean onMouseClick3(PathMap map, PathFinder finder, Vector2D mouseMapPos) {
        boolean result = super.onMouseClick3(map, finder, mouseMapPos);
        if (path != null && !path.equals(queuedPath)) {
            queuedPath = path;
            path = null;
        }
        return result;
    }

    @Override
    protected void interactAction(Entity entity) {
        super.interactAction(entity);
        switch (entity.getEntityType()){
            case LIVING -> {
                MapLivingEntity otherEntity = (MapLivingEntity) entity;
                if (otherEntity.getPlayerTeam() != this.team) {
                    game.newCombat(this.army, otherEntity.getArmy());
                }
            }
            case COLLECTABLE -> {
                CollectableMapEntity otherEntity = (CollectableMapEntity) entity;
                getPlayerTeam().getPlayerResources().addResource(otherEntity.getResource(), otherEntity.getAmount());
            }
        }
    }

    public Army getArmy() {
        return army;
    }

    public Path getQueuedPath() {
        return queuedPath;
    }
}
