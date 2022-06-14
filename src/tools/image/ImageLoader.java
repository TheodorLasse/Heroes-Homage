package src.tools.image;

import src.player.Resource;
import src.sprites.entities.livingEntities.Character;

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

    private enum NinjaSheetName
    {
        NINJA_SHEET_1, NINJA_SHEET_2, NINJA_SHEET_3, NINJA_SHEET_4, NINJA_SHEET_5, NINJA_SHEET_6,
        NINJA_SHEET_7, NINJA_SHEET_8, NINJA_SHEET_9, NINJA_SHEET_10, NINJA_SHEET_11, NINJA_SHEET_12, NINJA_SHEET_13,
        NINJA_SHEET_14, NINJA_SHEET_15, NINJA_SHEET_16, NINJA_SHEET_17, NINJA_SHEET_18, NINJA_SHEET_19, NINJA_SHEET_20,
        NINJA_SHEET_21, NINJA_SHEET_22, NINJA_SHEET_23, NINJA_SHEET_24, NINJA_SHEET_25, NINJA_SHEET_26, NINJA_SHEET_27,
        NINJA_SHEET_28, NINJA_SHEET_29, NINJA_SHEET_30, NINJA_SHEET_31, NINJA_SHEET_32
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

    final private static Map<NinjaSheetName, String> NINJA_SHEET_NAME_MAP = Map.ofEntries(
            Map.entry(NinjaSheetName.NINJA_SHEET_1, "Sheets/ninja_f1"),
            Map.entry(NinjaSheetName.NINJA_SHEET_2, "Sheets/ninja_f2"),
            Map.entry(NinjaSheetName.NINJA_SHEET_3, "Sheets/ninja_f3"),
            Map.entry(NinjaSheetName.NINJA_SHEET_4, "Sheets/ninja_f4"),
            Map.entry(NinjaSheetName.NINJA_SHEET_5, "Sheets/ninja_f5"),
            Map.entry(NinjaSheetName.NINJA_SHEET_6, "Sheets/ninja_f6"),
            Map.entry(NinjaSheetName.NINJA_SHEET_7, "Sheets/ninja_f7"),
            Map.entry(NinjaSheetName.NINJA_SHEET_8, "Sheets/ninja_f8"),
            Map.entry(NinjaSheetName.NINJA_SHEET_9, "Sheets/ninja_f9"),
            Map.entry(NinjaSheetName.NINJA_SHEET_10, "Sheets/ninja_f10"),
            Map.entry(NinjaSheetName.NINJA_SHEET_11, "Sheets/ninja_f11"),
            Map.entry(NinjaSheetName.NINJA_SHEET_12, "Sheets/ninja_f12"),
            Map.entry(NinjaSheetName.NINJA_SHEET_13, "Sheets/ninja_f13"),
            Map.entry(NinjaSheetName.NINJA_SHEET_14, "Sheets/ninja_f14"),
            Map.entry(NinjaSheetName.NINJA_SHEET_15, "Sheets/ninja_f15"),
            Map.entry(NinjaSheetName.NINJA_SHEET_16, "Sheets/ninja_f16"),
            Map.entry(NinjaSheetName.NINJA_SHEET_17, "Sheets/ninja_f17"),
            Map.entry(NinjaSheetName.NINJA_SHEET_18, "Sheets/ninja_f18"),
            Map.entry(NinjaSheetName.NINJA_SHEET_19, "Sheets/ninja_f19"),
            Map.entry(NinjaSheetName.NINJA_SHEET_20, "Sheets/ninja_f20"),
            Map.entry(NinjaSheetName.NINJA_SHEET_21, "Sheets/ninja_f21"),
            Map.entry(NinjaSheetName.NINJA_SHEET_22, "Sheets/ninja_f22"),
            Map.entry(NinjaSheetName.NINJA_SHEET_23, "Sheets/ninja_f23"),
            Map.entry(NinjaSheetName.NINJA_SHEET_24, "Sheets/ninja_f24"),
            Map.entry(NinjaSheetName.NINJA_SHEET_25, "Sheets/ninja_f25"),
            Map.entry(NinjaSheetName.NINJA_SHEET_26, "Sheets/ninja_f26"),
            Map.entry(NinjaSheetName.NINJA_SHEET_27, "Sheets/ninja_f27"),
            Map.entry(NinjaSheetName.NINJA_SHEET_28, "Sheets/ninja_f28"),
            Map.entry(NinjaSheetName.NINJA_SHEET_29, "Sheets/ninja_f29"),
            Map.entry(NinjaSheetName.NINJA_SHEET_30, "Sheets/ninja_f30"),
            Map.entry(NinjaSheetName.NINJA_SHEET_31, "Sheets/ninja_f31"),
            Map.entry(NinjaSheetName.NINJA_SHEET_32, "Sheets/ninja_f32"));

    final private static Map<Resource, String> RESOURCE_MAP = Map.ofEntries(
            Map.entry(Resource.GOLD, "Resources/gold"),
            Map.entry(Resource.ORE, "Resources/ore"),
            Map.entry(Resource.WOOD, "Resources/wood"),
            Map.entry(Resource.GEM, "Resources/gem"),
            Map.entry(Resource.MERCURY, "Resources/mercury"),
            Map.entry(Resource.CRYSTAL, "Resources/crystal"),
            Map.entry(Resource.SULPHUR, "Resources/sulphur"));

    private final Map<ImageName, BufferedImage> images;
    private final Map<Resource, BufferedImage> resources;
    private final Map<Character.CharacterEnum, List<BufferedImage>> animations;

    public ImageLoader() {
        images = new EnumMap<>(ImageName.class);
        resources = new EnumMap<>(Resource.class);
        animations = new EnumMap<>(Character.CharacterEnum.class);
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

    /**
     * Returns the animations of a given character
     *
     * @param character which character's animations
     *
     * @return An Image
     */
    public List<BufferedImage> getCharacter(Character.CharacterEnum character) {return animations.get(character); }

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

    private void loadNinjas() throws IOException {
        int startRow = 0;
        int endRow = 2;
        int tilesPerRow = 3;

        List<Character.CharacterEnum> ninjas = Arrays.asList(Character.CharacterEnum.NINJA_1,
                Character.CharacterEnum.NINJA_2, Character.CharacterEnum.NINJA_3,
                Character.CharacterEnum.NINJA_4, Character.CharacterEnum.NINJA_5, Character.CharacterEnum.NINJA_6);

        for (NinjaSheetName sheet: NinjaSheetName.values()){
            final String name = "images/" + NINJA_SHEET_NAME_MAP.get(sheet) + ".png";
            final URL imgURL = ClassLoader.getSystemResource(name);

            if (imgURL == null) {
                throw new FileNotFoundException("Could not find resource " + name);
            }

            loadSheet(loadImage(imgURL), startRow, endRow, tilesPerRow, ninjas);
        }
    }

    private void loadResources() throws IOException {
        for (Resource iterResource: Resource.values()){
            final String name = "images/" + RESOURCE_MAP.get(iterResource) + ".png";
            final URL imgURL = ClassLoader.getSystemResource(name);

            if (imgURL == null) {
                throw new FileNotFoundException("Could not find resource " + name);
            }
            resources.put(iterResource, loadImage(imgURL));
        }
    }

    private void loadBlackDragon() throws IOException {
        final String name = "images/blackDragon.png";
        final URL imgURL = ClassLoader.getSystemResource(name);
        if (imgURL == null) {
            throw new FileNotFoundException("Could not find resource " + name);
        }
        loadIndividualSheet(loadImage(imgURL), 0, 6, 30, Character.CharacterEnum.BLACK_DRAGON);
    }

    public void loadAssets() throws IOException {
        for (Character.CharacterEnum iterChar : Character.CharacterEnum.values()){
            animations.put(iterChar, new ArrayList<>());
        }
        loadImages();
        loadNinjas();
        loadResources();
        loadBlackDragon();
    }

    /**
     * Loads and returns an image. If the image can't be loaded, a default image will be returned.
     *
     * @param url
     *
     * @return image
     * @throws IOException
     */
    private static BufferedImage loadImage(URL url) throws IOException {
        return ImageIO.read(url);
    }

    /**
     * Cuts a sheet into sub images and puts them each into a List<BufferedImage>. This function is used for when
     * many characters share the same image for specific parts of an animation.
     * @param sheet Sheet to process
     * @param startRow Row from which it will start taking images from
     *                 (i.e, start from row index 2, so skip index 0 and 1)
     * @param endRow Row from which it will stop taking images from
     *               (i.e, end at index 5, so everything below will be ignored)
     * @param tilesPerRow Amount of sub images per row that are to be extracted
     *                    (i.e 3 characters per row)
     * @param characters List of character to add images to animation lists
     */
    private void loadSheet(BufferedImage sheet, int startRow, int endRow,
                           int tilesPerRow, List<Character.CharacterEnum> characters){
        int tileWidth = sheet.getWidth() / tilesPerRow;
        int tileHeight = sheet.getHeight() / endRow;

        for (int k = 0; k < endRow; k++) {
            for (int i = 0; i < tilesPerRow; i++) {
                int index = k * tilesPerRow + i;
                Character.CharacterEnum iterCharacter = characters.get(index);
                animations.get(iterCharacter).add(sheet.getSubimage(
                        i * tileWidth, (startRow + k) * tileHeight, tileWidth, tileHeight));
            }
        }
    }

    /**
     * Cuts a sheet into sub images and puts them each into a List<BufferedImage>. This function is used for the type
     * of sheet where an entire character's animation is on one sheet.
     * @param sheet Sheet to process
     * @param startRow Row from which it will start taking images from
     *                 (i.e, start from row index 2, so skip index 0 and 1)
     * @param endRow Row from which it will stop taking images from
     *               (i.e, end at index 5, so everything below will be ignored)
     * @param tilesPerRow Amount of sub images per row that are to be extracted
     *                    (i.e 3 characters per row)
     * @param character Which character to load with the images created
     */
    private void loadIndividualSheet(BufferedImage sheet, int startRow, int endRow,
                           int tilesPerRow, Character.CharacterEnum character){
        int tileWidth = sheet.getWidth() / tilesPerRow;
        int tileHeight = sheet.getHeight() / endRow;

        for (int k = 0; k < endRow; k++) {
            for (int i = 0; i < tilesPerRow; i++) {
                animations.get(character).add(sheet.getSubimage(
                        i * tileWidth, (startRow + k) * tileHeight, tileWidth, tileHeight));
            }
        }
    }
}
