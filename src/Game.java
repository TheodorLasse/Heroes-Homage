package src;

import src.map.GameMap;
import src.menu.GameMenu;
import src.player.PlayerTeam;
import src.player.PlayerTeamColors;
import src.player.Resource;
import src.tools.image.ImageLoader;
import src.menu.MenuComponent;
import src.tools.Vector2D;
import src.tools.input.KeyHandler;
import src.tools.time.DeltaTime;
import src.sprites.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 * Class where the entire game is implemented. Controls the central update function which controls deltaTime and calls upon all other
 * updates etc. Also initializes the game, shows the window and the end screen.
 */

public class Game {
    private final Logger logger = Logger.getLogger("");
    private static final double MAX_FPS = 144;
    public static final ImageLoader imageLoader = new ImageLoader();
    private final GameMap gameMap;
    private final GameMenu gameMenu;

    private final GameComponent gameComponent;
    private final MenuComponent menuComponent;
    Dimension screenSize;

    private ArrayList<PlayerTeam> playerTeamList;

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
        gameMap = new GameMap(getMapScreenDimension());
        gameComponent = new GameComponent(this);
        gameMenu = new GameMenu(getMenuScreenDimension());
        menuComponent = new MenuComponent(this);
        setUpGame();
        setUpWindow();
        setUpIO();
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
            menuComponent.repaint();

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
    public Iterable<Sprite> getGameSpriteIterator() {
        return gameMap.getIterator();
    }

    /**
     * Returns an iterable var of all sprites. Basically merges all sprites into one list for menuComponent. The list's
     * order is the order sprites get drawn.
     */
    public Iterable<Sprite> getMenuSpriteIterator() {
        return gameMenu.getIterator();
    }

    public GameMap getGameMap(){
        return this.gameMap;
    }

    public PlayerTeam getCurrentPlayer(){
        return playerTeamList.get(0);
    }

    /**
     * Sets up game parameters such as the player teams
     */
    private void setUpGame(){
        playerTeamList = new ArrayList<>();
        for (PlayerTeamColors color: PlayerTeamColors.values()){
            playerTeamList.add(new PlayerTeam(color));
            if (playerTeamList.size() == gameMap.getTeamCount()) break;
        }
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
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
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

    /**
     * Sets listeners for key presses and mouse clicks
     */
    private void setUpIO() {
        final KeyHandler keyHandler = new KeyHandler(gameComponent);
        keyHandler.addKeyListener(gameMap);
        gameComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Vector2D mousePos = new Vector2D(e.getX(), e.getY());
                gameMap.onMouseClick(mousePos, e.getButton());
            }
        });
        menuComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Vector2D mousePos = new Vector2D(e.getX(), e.getY());
                gameMenu.onMouseClick(mousePos, e.getButton());
            }
        });
    }
}

