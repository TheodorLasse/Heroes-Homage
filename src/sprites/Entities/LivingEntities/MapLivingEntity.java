package src.sprites.Entities.LivingEntities;

import src.Army;
import src.Game;
import src.player.PlayerTeam;
import src.sprites.Entities.Entity;
import src.sprites.Entities.EntityHandler;
import src.tools.Vector2D;
import src.tools.image.ImageLoader;

public class MapLivingEntity extends LivingEntity {
    protected final Game game;
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
    public MapLivingEntity(Vector2D position, ImageLoader.Character character, Game game, PlayerTeam team, EntityHandler mapEntityHandler) {
        super(position, character, team, mapEntityHandler);
        this.game = game;
        this.army = new Army(team);
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
        }
    }

    public Army getArmy() {
        return army;
    }
}
