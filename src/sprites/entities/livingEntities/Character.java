package src.sprites.entities.livingEntities;

import src.tools.Vector2D;

import java.util.Map;


public class Character {
    /**
     * Sprites are not the same distance from the top left corner due to the structure of the sprite sheets.
     * This is to offset the sprites "back" to the top left corner.
     */
    public final static Map<CharacterEnum, Vector2D> CHARACTER_OFFSETS = Map.ofEntries(
            Map.entry(CharacterEnum.NINJA_1, new Vector2D(-35, -50)),
            Map.entry(CharacterEnum.NINJA_2, new Vector2D(0, -50)),
            Map.entry(CharacterEnum.NINJA_3, new Vector2D(0, -50)),
            Map.entry(CharacterEnum.NINJA_4, new Vector2D(-33, -60)),
            Map.entry(CharacterEnum.NINJA_5, new Vector2D(-10, -60)),
            Map.entry(CharacterEnum.NINJA_6, new Vector2D(-5, -60))
            );

    public enum CharacterEnum
    {
        NINJA_1, NINJA_2, NINJA_3, NINJA_4, NINJA_5, NINJA_6
    }
}
