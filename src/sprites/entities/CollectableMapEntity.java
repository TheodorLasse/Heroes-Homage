package src.sprites.entities;

import src.Game;
import src.player.Resource;
import src.tools.Vector2D;
import java.util.Random;

public class CollectableMapEntity extends Entity{
    private final Resource resourcesType;
    private final Random rnd = new Random();
    private final EntityHandler entityHandler;

    public CollectableMapEntity(Vector2D position, Resource resource, EntityHandler entityHandler) {
        super(position, new Vector2D(3, 2), 0, Game.imageLoader.getResourceImage(resource));
        setEntityType(EntityType.COLLECTABLE);
        this.resourcesType = resource;
        this.entityHandler = entityHandler;
    }

    public Resource getResource() {
        return resourcesType;
    }

    public int getAmount() {
        entityHandler.remove(this);
        return rnd.nextInt(3) + 1;
    }
}
