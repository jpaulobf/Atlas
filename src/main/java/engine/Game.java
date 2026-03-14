package engine;

import input.KeyboardInputs;
import window.Window;
import window.GameWindow;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    protected Window gameWindow;
    protected Engine engine;
    protected KeyboardInputs input;
    private int currentFps = 0;
    protected final int CANVAS_WIDTH = 1600;
    protected final int CANVAS_HEIGHT = 900;

    // Cached scaling values
    private double scale = 1.0;
    private double offsetX = 0;
    private double offsetY = 0;
    private boolean paused = false;

    /**
     * Constructs a new Game instance with the specified title, width, and height
     * for the game window.
     * 
     * @param title
     * @param width
     * @param height
     */
   public Game(String title, int width, int height, int targetFps) {
        gameWindow = new GameWindow(title, width, height, this);
        input = new KeyboardInputs(gameWindow);
        gameWindow.addKeyListener(input);

        // The Engine will call the update and render methods of this Game instance
        // during the game loop.
        engine = new Engine(this, targetFps);

        // Recalculate scale only when window/canvas is resized
        gameWindow.getCanvas().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                recalculateScale();
            }
        });
    }

    /**
     * Starts the game by calling the init method and then starting the engine's
     * game loop.
     */
    public void start() {
        init();
        recalculateScale(); // Initial calculation
        engine.start();
    }

    /**
     * Recalculates the scaling factor and offsets to center the game content
     * maintaining the aspect ratio. This should be called on window resize.
     */
   private void recalculateScale() {
        Canvas canvas = gameWindow.getCanvas();
        if (canvas == null)
            return;

        double scaleX = (double) canvas.getWidth() / CANVAS_WIDTH;
        double scaleY = (double) canvas.getHeight() / CANVAS_HEIGHT;

        this.scale = Math.min(scaleX, scaleY);

        double drawWidth = CANVAS_WIDTH * this.scale;
        double drawHeight = CANVAS_HEIGHT * this.scale;

        this.offsetX = (canvas.getWidth() - drawWidth) / 2;
        this.offsetY = (canvas.getHeight() - drawHeight) / 2;
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

        if (!paused) {
            onUpdate(deltaTime);
        }
    }

    public void togglePause() {
        this.paused = !this.paused;
    }

    public boolean isPaused() {
        return paused;
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

        Graphics g;
        try {
            g = bs.getDrawGraphics();
        } catch (Exception e) {
            // If the buffer strategy fails (common when toggling fullscreen),
            // recreate the strategy for the new window state if valid, and skip this frame.
            if (canvas.isDisplayable()) {
                try {
                    canvas.createBufferStrategy(3);
                } catch (Exception ex) {
                    // If recreation fails (window peer not ready), we ignore and try again next
                    // frame.
                    // This prevents the crash loop during window transitions.
                }
            }
            return;
        }

        // Clear the screen before rendering the new frame
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Create a copy of the Graphics object to apply the transformation
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(offsetX, offsetY);
       g2d.scale(scale, scale);

        onRender(g2d, paused ? 0 : interpolation);
        if (paused) {
            drawPauseScreen(g2d);
        }
        g2d.dispose(); // Release the copy

        // Draw FPS counter in the top-left corner for debugging
        g.setColor(Color.BLACK);
        g.drawString("FPS: " + currentFps, 5, 15);

        g.dispose();

        // Only show the buffer if the contents were not lost during rendering
        if (!bs.contentsLost()) {
            bs.show();
        }

        // Syncs the display on some systems (Linux/Mac) to prevent tearing/stutter
        java.awt.Toolkit.getDefaultToolkit().sync();
    }

    private void drawPauseScreen(Graphics g) {
        // Calculate the dimensions and position for the pause rectangle
        int rectWidth = 320;
        int rectHeight = 150;
        int rectX = (CANVAS_WIDTH - rectWidth) / 2;
        int rectY = (CANVAS_HEIGHT - rectHeight) / 2;

        g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
        g.fillRect(rectX, rectY, rectWidth, rectHeight);
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(100f));
        g.drawString("Pause", rectX + 15, rectY + 100);
    }

    public void renderFPS(int fps) {
        this.currentFps = fps;
    }

    // --- Abstract methods
    public abstract void init();

    public abstract void onUpdate(long deltaTime);

    public abstract void onRender(Graphics g, double interpolation);
}