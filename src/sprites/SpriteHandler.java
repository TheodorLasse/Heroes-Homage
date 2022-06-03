package src.sprites;

import src.tools.Vector2D;
import src.tools.time.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;

/**
 * Class for handling all sprites which includes updating the timers, adding new, removing or renewing old ones.
 */
public class SpriteHandler
{
    private BufferedImage background = null;
    private final Map<SpriteLayer, List<Sprite>> spriteLayers;
    private final Map<Sprite, DeltaTimer> timedSprites;
    private final List<Action> scheduledActions;

    public SpriteHandler() {
	spriteLayers = new EnumMap<>(SpriteLayer.class);
	timedSprites = new HashMap<>();
	scheduledActions = new ArrayList<>();

	// Initialize all layers with an empty list.
	for (SpriteLayer spriteLayer : SpriteLayer.values()) {
	    spriteLayers.put(spriteLayer, new ArrayList<>());
		}
    }

    /**
     * Schedules a sprite for addition to the spriteHandler.
     *
     * @param sprite Sprite to add.
     * @param spriteLayer  Layer to add to.
     */
    public void add(Sprite sprite, SpriteLayer spriteLayer) {
		scheduleAction(() -> spriteLayers.get(spriteLayer).add(sprite));
    }

    /**
     * Schedules a sprite for addition to the spriteHandler for a specified duration.
     *
     * @param sprite Sprite to add.
     * @param time   Time until removal.
     * @param spriteLayer  Layer to add to.
     */
    public void add(Sprite sprite, double time, SpriteLayer spriteLayer) {
		scheduleAction(() -> {
	    	spriteLayers.get(spriteLayer).add(sprite);
	    	timedSprites.put(sprite, new DeltaTimer(time));
		});
    }

    /**
     * Schedules a sprite for removal from the sprite handler.
     *
     * @param image Sprite to remove.
     */
    public void remove(Sprite image) {
		scheduleAction(() -> {
			// Remove from spriteLayer
			for (List<Sprite> sprites : spriteLayers.values()) {
				if (sprites.remove(image)) {
					break;
				}
			}

			timedSprites.remove(image);
		});
	}

    /**
     * Removes one image and adds another one. Can be used to update a sprite texture by giving the old sprite and sending in the new one.
     *
     * @param newSprite The new sprite to be added.
     * @param oldSprite The old sprite that will be replaced.
     * @param newSpriteLayer  The layer where the new sprite will pe added.
     */
    public void renew(Sprite newSprite, Sprite oldSprite, SpriteLayer newSpriteLayer) {
		remove(oldSprite);
		add(newSprite, newSpriteLayer);
    }

    public void setBackground(BufferedImage bg) {
	background = bg;
    }

    /**
     * Gets the entire background texture.
     *
     * @return Background texture.
     */
    public SpriteTexture getBackgroundTexture() {
	return new SpriteTexture(new Vector2D(), 0, background);
    }

    /**
     * Permanently adds an image to the background.
     *
     * @param image Image to add.
     * @param x     Vertical coordinate.
     * @param y     Horizontal coordinate.
     */
    public void addToBackground(BufferedImage image, int x, int y) {
		Graphics g = background.getGraphics();
		g.drawImage(image, x, y, null);
    }

    /**
     * Updates the timers for all sprites, and removes sprites that has run out of time
     *
     * @param deltaTime Elapsed time.
     */
    private void updateSpriteTimers(DeltaTime deltaTime) {
	for (Map.Entry<Sprite, DeltaTimer> pair : timedSprites.entrySet()) {

	    DeltaTimer timer = pair.getValue();
	    timer.update(deltaTime);

	    if (timer.isComplete()) {
		remove(pair.getKey());
	    }
	}
    }

    /**
     * Iterates through spriteMaps and updates the timer of sprites. Removes sprites that has run out of time.
     *
     * @param deltaTime Elapsed time
     */
    public void update(DeltaTime deltaTime) {
	updateSpriteTimers(deltaTime);

	executeScheduledActions();
    }

    /**
     * Returns a list with all sprites on a given layer.
     *
     * @param spriteLayer Sprite layer.
     *
     * @return A new list with sprites.
     */
    public ArrayList<Sprite> getLayerIterator(SpriteLayer spriteLayer) {
	if (spriteLayer == SpriteLayer.FIRST && background != null) {
	    ArrayList<Sprite> sprites = new ArrayList<>();
	    sprites.add(getBackgroundTexture());
	    sprites.addAll(spriteLayers.get(spriteLayer));
	    return sprites;
	}

	return new ArrayList<>(spriteLayers.get(spriteLayer));
    }

    /**
     * Schedules a new action.
     *
     * @param action Action to schedule.
     */
    private void scheduleAction(Action action) {
	scheduledActions.add(action);
    }

    /**
     * Executes all scheduled actions.
     */
    private void executeScheduledActions() {
	for (Action action : scheduledActions) {
	    action.execute();
	}
	scheduledActions.clear();
    }

    /**
     * Interface for actions that can be scheduled in the SpriteHandler.
     */
    @FunctionalInterface
    private interface Action
    {
	void execute();
    }
}
