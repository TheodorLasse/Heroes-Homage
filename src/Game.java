package src;

import src.tools.GameComponent;
import src.tools.ImageLoader;
import src.tools.MenuComponent;
import src.tools.Vector2D;
import src.tools.input.KeyHandler;
import src.tools.input.MouseListener;
import src.tools.time.DeltaTime;
import src.sprites.Sprite;
import src.sprites.SpriteHandler;
import src.sprites.SpriteLayer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 * Class where the entire game is implemented. Controlls the central update function which controls deltaTime and calls upon all other
 * updates etc. Also initializes the game, shows the window and the end screen.
 */

public class Game {
    private final Logger logger = Logger.getLogger("");
    private static final double MAX_FPS = 144;
    public static final ImageLoader imageLoader = new ImageLoader();
    private SpriteHandler spriteHandler;
    private GameMap gameMap;


    public GameComponent gameComponent;
    public MenuComponent menuComponent;
    Dimension screenSize;

    public Game(){
        setUpLogger();

        // Try to load all images and audio.
        // If the loading fails, then exit the program because there is no point in running the game without these resources.
        try {
            imageLoader.loadImages();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            System.exit(1);
        }


        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gameComponent = new GameComponent(this);
        menuComponent = new MenuComponent(this);
        spriteHandler = new SpriteHandler();
        gameMap = new GameMap(screenSize);
        setUpWindow();

        final KeyHandler keyHandler = new KeyHandler(gameComponent);
        keyHandler.addKeyListener(gameMap);
        final MouseListener mouseMapListener = new MouseListener(gameComponent, this);
        final MouseListener mouseMenuListener = new MouseListener(menuComponent, this);
    }

    /**
     * Starts the game.
     */
    public void start() {

        long lastUpdate = System.nanoTime();

        // Minimum number of nanoseconds for each game loop
        final double minFrameTime = DeltaTime.NANO_SECONDS_IN_SECOND / MAX_FPS;

        while (true) {
            long startTime = System.nanoTime();
            long deltaTime = startTime - lastUpdate;
            lastUpdate = startTime;

            update(new DeltaTime(deltaTime));
            gameComponent.repaint();

            long totalTime = System.nanoTime() - startTime;
            // To avoid using 100% of cpu core when not needed, sleep until the minimum frame time is met
            if (totalTime < minFrameTime) {
                try {
                    TimeUnit.NANOSECONDS.sleep((long) minFrameTime - totalTime);
                } catch (InterruptedException ignored) {}
            }
        }
    }

    /**
     * Updates the game.
     */
    private void update(DeltaTime deltaTime) {
        spriteHandler.update(deltaTime);
        gameMap.update(deltaTime);
    }

    public Dimension getMapScreenDimension(){
        return new Dimension((int)(screenSize.width * 0.8), screenSize.height);
    }

    public Dimension getMenuScreenDimension(){
        return new Dimension((int)(screenSize.width * 0.2), screenSize.height);
    }


    /**
     * Returns an iterable var of all sprites. Basically merges all sprites into one list for gameComponent. The list's order matters,
     * sprites are drawn before entities, etc
     */
    public Iterable<Sprite> getSpriteIterator() {
        List<Sprite> sprites = new ArrayList<>();

        sprites.addAll(gameMap.getIterator());
        sprites.addAll(spriteHandler.getLayerIterator(SpriteLayer.FIRST));
        sprites.addAll(spriteHandler.getLayerIterator(SpriteLayer.LAST));

        return sprites;
    }

    /**
     * Creates the game window.
     */
    private void setUpWindow() {
        JFrame frame = new JFrame("Game");
        frame.setLayout(new BorderLayout());
        Container contentPane = frame.getContentPane();
        contentPane.add(gameComponent, BorderLayout.WEST);
        contentPane.add(menuComponent, BorderLayout.EAST);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Adds a new FileHandler to the logger. If the FileHandler fails, a ConsoleHandler is used as backup.
     */
    private void setUpLogger() {
        try {
            FileHandler fh = new FileHandler("LogFile.log");
            logger.addHandler(fh);
            fh.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString(), e);

            // If the logger don't have any handlers, add a ConsoleHandler
            if (logger.getHandlers().length == 0) {
                ConsoleHandler ch = new ConsoleHandler();
                logger.addHandler(ch);
            }
        }
    }

    public void mouseMapClick(Vector2D mousePos, int mouseButton) {
        gameMap.onMouseClick(mousePos, mouseButton);
    }

    public void mouseMenuClick(Vector2D mousePos) {

    }
}

