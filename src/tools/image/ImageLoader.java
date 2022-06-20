package src.tools.image;

import src.player.Resource;
import src.sprites.entities.livingEntities.AnimationComponent;
import src.sprites.entities.livingEntities.Character;
import src.sprites.entities.livingEntities.LivingEntityState;
import src.tools.JsonReader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ImageLoader
{
    public enum ImageName
    {
        GRASS, WATER, ROCK, BLUE_FLAG, RED_FLAG, MENU_BACKGROUND, GAME_BORDER, GAME_BORDER_CORNER, RESOURCE_PANEL,
        COMBAT_TOP, COMBAT_BOTTOM, ERROR

    }

    /**
     * From enum to actual file name
     */
    final private static Map<ImageName, String> IMAGE_NAME_MAP = Map.ofEntries(
            Map.entry(ImageName.GRASS, "grass"),
            Map.entry(ImageName.ROCK, "rock"),
            Map.entry(ImageName.BLUE_FLAG, "blue_flag"),
            Map.entry(ImageName.RED_FLAG, "red_flag"),
            Map.entry(ImageName.WATER, "water"),
            Map.entry(ImageName.MENU_BACKGROUND, "menu_background"),
            Map.entry(ImageName.GAME_BORDER, "game_border"),
            Map.entry(ImageName.GAME_BORDER_CORNER, "game_border_corner"),
            Map.entry(ImageName.RESOURCE_PANEL, "resource_panel"),
            Map.entry(ImageName.COMBAT_TOP, "combat_top"),
            Map.entry(ImageName.COMBAT_BOTTOM, "combat_bottom"),

            Map.entry(ImageName.ERROR, "grass")
    );

    final private static Map<Character.CharacterEnum, String> CHARACTER_NAME_MAP = Map.ofEntries(
            Map.entry(Character.CharacterEnum.ORC, "orc"),
            Map.entry(Character.CharacterEnum.NECROMANCER_LIGHT, "necromancer_light")
    );

    final private static Map<LivingEntityState, String> STATE_NAME_MAP = Map.ofEntries(
            Map.entry(LivingEntityState.ATTACK1, "attack1"),
            Map.entry(LivingEntityState.ATTACK2, "attack2"),
            Map.entry(LivingEntityState.RUN, "run"),
            Map.entry(LivingEntityState.DEATH, "death"),
            Map.entry(LivingEntityState.DEAD, "dead"),
            Map.entry(LivingEntityState.HIT, "hit"),
            Map.entry(LivingEntityState.IDLE, "idle")
            );

    final private static Map<LivingEntityState, Integer> ANIMATION_LENGTH_MAP = Map.ofEntries(
            Map.entry(LivingEntityState.ATTACK1, 16),
            Map.entry(LivingEntityState.ATTACK2, 20),
            Map.entry(LivingEntityState.RUN, 8),
            Map.entry(LivingEntityState.DEATH, 15),
            Map.entry(LivingEntityState.DEAD, 1),
            Map.entry(LivingEntityState.HIT, 6),
            Map.entry(LivingEntityState.IDLE, 8)
            );

    final private static Map<Resource, String> RESOURCE_MAP = Map.ofEntries(
            Map.entry(Resource.GOLD, "gold"),
            Map.entry(Resource.ORE, "ore"),
            Map.entry(Resource.WOOD, "wood"),
            Map.entry(Resource.GEM, "gem"),
            Map.entry(Resource.MERCURY, "mercury"),
            Map.entry(Resource.CRYSTAL, "crystal"),
            Map.entry(Resource.SULPHUR, "sulphur"));

    private final Map<ImageName, BufferedImage> images;
    private final Map<Resource, BufferedImage> resources;
    private final Map<Character.CharacterEnum, Map<LivingEntityState, AnimationComponent>> characterAnimations;

    public ImageLoader() {
        images = new EnumMap<>(ImageName.class);
        resources = new EnumMap<>(Resource.class);
        characterAnimations = new EnumMap<>(Character.CharacterEnum.class);
    }

    /**
     * Returns the image with a given name.
     *
     * @param imgName Image name to look for.
     *
     * @return An Image
     */
    public BufferedImage getImage(ImageName imgName) {
        return images.get(imgName);
    }

    public BufferedImage getResourceImage(Resource resource) {return resources.get(resource); }

    /**
     * Loads all images and creates sheets.
     */
    private void loadImages() throws IOException {
        for (ImageName iterImageName : ImageName.values()) {
            final String name = "images/" + IMAGE_NAME_MAP.get(iterImageName) + ".png";
            final URL imgURL = ClassLoader.getSystemResource(name);

            if (imgURL == null) {
                throw new FileNotFoundException("Could not find resource " + name);
            }

            images.put(iterImageName, loadImage(imgURL));
        }
    }


    private void loadResources() throws IOException {
        for (Resource iterResource: Resource.values()){
            final String name = "images/resources/" + RESOURCE_MAP.get(iterResource) + ".png";
            final URL imgURL = ClassLoader.getSystemResource(name);

            if (imgURL == null) {
                throw new FileNotFoundException("Could not find resource " + name);
            }
            resources.put(iterResource, loadImage(imgURL));
        }
    }

    private void loadCharacterAnimations() throws IOException {
        for (Character.CharacterEnum characterEnum : Character.CharacterEnum.values()) {
            characterAnimations.get(characterEnum);
            Map<LivingEntityState, AnimationComponent> iterCharacterAnimations;
            iterCharacterAnimations = new EnumMap<>(LivingEntityState.class);
            characterAnimations.put(characterEnum, iterCharacterAnimations);

            Map<?, ?> jsonMap = JsonReader.readJsonCritical(characterEnum);

            int numberOfDirections = 8; //CombatEntity sprites have 8 directions, MapEntity sprites have 16
            if (characterEnum == Character.CharacterEnum.NECROMANCER_LIGHT) numberOfDirections = 16;

            for (LivingEntityState state : LivingEntityState.values()) {
                String charName = CHARACTER_NAME_MAP.get(characterEnum);
                final String name = "images/sheets/" + charName + "/" + charName + "_" + STATE_NAME_MAP.get(state) + ".png";
                final URL imgURL = ClassLoader.getSystemResource(name);
                if (imgURL == null) {
                    throw new FileNotFoundException("Could not find resource " + name);
                }

                final String animationLengthName = STATE_NAME_MAP.get(state) + "_length";
                int animationLength = (int) (double) jsonMap.get(animationLengthName);

                AnimationComponent animationComponent = new AnimationComponent(
                        loadSheet(loadImage(imgURL), numberOfDirections, animationLength));
                iterCharacterAnimations.put(state, animationComponent);
            }
        }
    }

    public AnimationComponent getCharacterAnimation(Character.CharacterEnum characterEnum, LivingEntityState state) {
        return characterAnimations.get(characterEnum).get(state);
    }

    public void loadAssets() throws IOException {
        loadImages();
        loadResources();
        loadCharacterAnimations();
    }

    /**
     * Loads and returns an image. If the image can't be loaded, a default image will be returned.
     *
     * @param url image's path
     *
     * @return image
     * @throws IOException image not found
     */
    private static BufferedImage loadImage(URL url) throws IOException {
        return ImageIO.read(url);
    }

    /**
     * Cuts a sheet into sub images and puts them each into a List<BufferedImage>. This function is used for when
     * many characters share the same image for specific parts of an animation.
     *
     * @param sheet       Sheet to process
     * @param endRow      Row from which it will stop taking images from
     *                    (i.e, end at index 5, so everything below will be ignored)
     * @param tilesPerRow Amount of sub images per row that are to be extracted
     *                    (i.e 3 characters per row)
     */
    private List<List<BufferedImage>> loadSheet(BufferedImage sheet, int endRow, int tilesPerRow){
        List<List<BufferedImage>> allDirectionsList = new ArrayList<>();
        for (int i = 0; i < endRow; i++) {
            allDirectionsList.add(new ArrayList<>());
        }

        int tileWidth = sheet.getWidth() / tilesPerRow;
        int tileHeight = sheet.getHeight() / endRow;

        for (int k = 0; k < endRow; k++) {
            for (int i = 0; i < tilesPerRow; i++) {
                allDirectionsList.get(k).add(sheet.getSubimage(
                        i * tileWidth, (k) * tileHeight, tileWidth, tileHeight));
            }
        }
        return allDirectionsList;
    }
}
