package engine;

import input.KeyboardInputs;
import window.GameWindow;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

/**
 * The Game class serves as the base class for any specific game implementation.
 * It provides a structure for initializing the game, updating game logic, and
 * rendering graphics. It also manages the game window and input handling.
 * Subclasses must implement the abstract methods `init()`, `onUpdate(long
 * deltaTime)`, and `onRender(Graphics g, double interpolation)` to define their
 * specific game behavior.
 * The Game class interacts with the Engine to run the game loop and with the
 * GameWindow to manage the display and input.
 */
public abstract class Game {

    // Main game components
    protected GameWindow gameWindow;
    protected Engine engine;
    protected KeyboardInputs input;
    private int currentFps = 0;

    /**
     * Constructs a new Game instance with the specified title, width, and height
     * for the game window.
     * 
     * @param title
     * @param width
     * @param height
     */
    public Game(String title, int width, int height) {
        gameWindow = new GameWindow(title, width, height);
        input = new KeyboardInputs(gameWindow);
        gameWindow.addKeyListener(input);

        // The Engine will call the update and render methods of this Game instance
        // during the game loop.
        engine = new Engine(this, 120);
    }

    /**
     * Starts the game by calling the init method and then starting the engine's
     * game loop.
     */
    public void start() {
        init();
        engine.start();
    }

    /**
     * Updates the game logic. This method is called by the Engine at a fixed
     * timestep.
     * 
     * @param deltaTime
     */
    public void update(long deltaTime) {
        if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
            System.exit(0);
        }
        onUpdate(deltaTime);
    }

    /**
     * Renders the game graphics. This method is called by the Engine during the
     * game loop.
     * 
     * @param interpolation
     */
    public void render(double interpolation) {
        Canvas canvas = gameWindow.getCanvas();

        // Verify that the canvas is still valid before attempting to render. If the
        // window was closed or is not displayable, we should skip rendering to avoid
        // issues.
        if (canvas == null || !canvas.isDisplayable()) {
            return;
        }

        BufferStrategy bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(3); // Triple buffering
            return;
        }

        Graphics g = bs.getDrawGraphics();

        // Clear the screen before rendering the new frame
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Send the Graphics object and interpolation value to the subclass's onRender
        // method
        onRender(g, interpolation);

        // Draw FPS counter in the top-left corner for debugging
        g.setColor(Color.BLACK);
        g.drawString("FPS: " + currentFps, 5, 15);

        g.dispose();
        bs.show();
    }

    public void renderFPS(int fps) {
        this.currentFps = fps;
    }

    // --- Abstract methods
    public abstract void init();
    public abstract void onUpdate(long deltaTime);
    public abstract void onRender(Graphics g, double interpolation);
}