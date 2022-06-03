package src.sprites.Entities;

import src.data.Vector2D;
import src.sprites.Sprite;
import src.data.time.DeltaTime;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling all the entities in the game. It updates all entities, notifies them about collisions, and contains functions for
 * adding/removing entities to/from the game.
 */
public class EntityHandler
{
    private List<Entity> entities;
    private List<Entity> toRemove;
    private List<Entity> toAdd;


    public EntityHandler() {
	entities = new ArrayList<>();
	toRemove = new ArrayList<>();
	toAdd = new ArrayList<>();
    }

    /**
     * Checks if a given Entity type exists within the entityhandler.
     */
    public <T> boolean isEntityTypeExisting(Class<T> type) {
	for (Sprite s:getIterator()) {
	    if (type.isAssignableFrom(s.getClass())){
	        return true;
	    }
	}
	return false;
    }

    /**
     * Updates the entity handler and all entities.
     *
     * @param deltaTime
     */
    public void update(DeltaTime deltaTime, Vector2D mapFocus) {
	// Update all entities
	for (Entity entity : entities) {
	    entity.update(deltaTime, mapFocus);
	}

	internalUpdate();
    }

    /**
     * Updates EntityHandler internally
     */
    public void internalUpdate() {
	// Check collisions
	addEntities();
	clearEntities();
    }

    /**
     * Schedules an entity for addition to the entity handler. It will be added when possible.
     *
     * @param entity
     */
    public void add(Entity entity) {
	toAdd.add(entity);
    }

    /**
     * Schedules an entity for removal from the entity handler. It will be removed when possible.
     *
     * @param entity
     */
    public void remove(Entity entity) {
	// Do not allow duplicates
	if (!toRemove.contains(entity)) {
	    toRemove.add(entity);
	}
    }

    /**
     * Returns an iterator with a sprite for each entity.
     *
     * @return Iterator with sprites
     */
    public ArrayList<Sprite> getIterator() {
		return new ArrayList<>(entities);
    }


    /**
     * Adds entities that have been scheduled for adding.
     */
    private void addEntities() {
	entities.addAll(toAdd);
	toAdd.clear();
    }

    /**
     * Removes entities that have been scheduled for removal.
     */
    private void clearEntities() {
	entities.removeAll(toRemove);
	toRemove.clear();
    }
}
