package src.sprites.Entities.LivingEntities;

import src.tools.Vector2D;
import src.tools.image.ImageLoader;

import java.util.Map;


public class CharacterOffsets {
    public final static Map<ImageLoader.Character, Vector2D> CHARACTER_OFFSETS = Map.ofEntries(
            Map.entry(ImageLoader.Character.NINJA_1, new Vector2D(-35, -50)),
            Map.entry(ImageLoader.Character.NINJA_2, new Vector2D(0, -50)),
            Map.entry(ImageLoader.Character.NINJA_3, new Vector2D(0, -50)),
            Map.entry(ImageLoader.Character.NINJA_4, new Vector2D(-33, -60)),
            Map.entry(ImageLoader.Character.NINJA_5, new Vector2D(-10, -60)),
            Map.entry(ImageLoader.Character.NINJA_6, new Vector2D(-5, -60))
            );
}
