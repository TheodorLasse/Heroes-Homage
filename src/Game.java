package src;

import src.combat.CombatComponent;
import src.combat.GameCombat;
import src.map.GameMap;
import src.player.PlayerTeam;
import src.player.PlayerTeamColor;
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
    private final GameCombat gameCombat;

    private CardLayout card;
    private JPanel panelContainer;
    private final GameComponent gameComponent;
    private final MenuComponent menuComponent;
    private final CombatComponent combatComponent;
    Dimension screenSize;

    private final ArrayList<PlayerTeam> playerTeamList = new ArrayList<>();

    public Game(){
        setUpLogger();

        // Try to load all images and audio.
        // If the loading fails, then exit the program because there is no point in running the game without these resources.
        try {
            imageLoader.loadAssets();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.toString(), e);
            System.exit(1);
        }

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        gameMap = new GameMap(this, getMapScreenDimension(), playerTeamList);
        gameCombat = new GameCombat(this);
        gameComponent = new GameComponent(this);
        menuComponent = new MenuComponent(this);
        combatComponent = new CombatComponent(this);

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
            panelContainer.repaint();

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
        if (gameCombat.isBattle()){
            gameCombat.update(deltaTime);
        }else {
            gameMap.update(deltaTime);
        }
    }

    public void newCombat(Army attacker, Army defender){
        card.show(panelContainer, "combat");
        gameCombat.setUpBattlefield(attacker, defender);
    }

    public void finishCombat(){
        card.show(panelContainer, "map");
    }

    public Dimension getMapScreenDimension(){
        return new Dimension((int)(screenSize.width * 0.8), screenSize.height);
    }

    public Dimension getMenuScreenDimension(){
        return new Dimension((int)(screenSize.width * 0.2), screenSize.height);
    }

    public Dimension getCombatScreenDimension(){
        return new Dimension(screenSize);
    }

    /**
     * Returns an iterable var of all sprites. Basically merges all sprites into one list for gameComponent. The list's order matters,
     * sprites are drawn before entities, etc
     */
    public Iterable<Sprite> getGameMapSpriteIterator() {
        return gameMap.getIterator();
    }

    /**
     * Returns an iterable var of all sprites. Basically merges all sprites into one list for gameComponent. The list's order matters,
     * sprites are drawn before entities, etc
     */
    public Iterable<Sprite> getCombatSpriteIterator() {
        return gameCombat.getIterator();
    }

    public GameMap getGameMap(){
        return this.gameMap;
    }

    public PlayerTeam getCurrentPlayer(){
        return gameMap.getCurrentPlayer();
    }

    /**
     * Creates the game window.
     */
    private void setUpWindow() {
        JFrame frame = new JFrame("Game");
        card = new CardLayout();
        panelContainer = new JPanel();
        panelContainer.setLayout(card);

        JPanel mapJPanel = new JPanel();
        mapJPanel.setLayout(new BorderLayout());
        mapJPanel.add(gameComponent, BorderLayout.WEST);
        mapJPanel.add(menuComponent, BorderLayout.EAST);

        JPanel combatJPanel = new JPanel();
        combatJPanel.setLayout(new BorderLayout());
        combatJPanel.add(combatComponent, BorderLayout.CENTER);

        panelContainer.add(mapJPanel, "map");
        panelContainer.add(combatJPanel, "combat");

        frame.add(panelContainer);

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
        combatComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Vector2D mousePos = new Vector2D(e.getX(), e.getY());
                gameCombat.onMouseClick(mousePos, e.getButton());
            }
        });
    }
}

