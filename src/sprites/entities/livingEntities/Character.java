package src.sprites.entities.livingEntities;

import src.tools.Vector2D;


public class Character {
    /**
     * Sprites are not the same distance from the top left corner due to the structure of the sprite sheets.
     * This is to offset the sprites "back" to the top left corner.
     */
    public static Vector2D CHARACTER_OFFSET = new Vector2D(-108, -62);

    public enum CharacterEnum
    {
        NINJA_1, NINJA_2, NINJA_3, NINJA_4, NINJA_5, NINJA_6, BLACK_DRAGON
    }
}
