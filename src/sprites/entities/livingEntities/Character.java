package src.sprites.entities.livingEntities;

import src.tools.Vector2D;


public class Character {
    /**
     * Sprites are not the same distance from the top left corner due to the structure of the sprite sheets.
     * This is to offset the sprites "back" to the top left corner.
     */
    public static Vector2D CHARACTER_OFFSET = new Vector2D(-83, -85);

    public enum CharacterEnum
    {
        ORC
    }
}
