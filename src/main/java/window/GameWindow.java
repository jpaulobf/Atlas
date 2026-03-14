package window;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import engine.Game;

/**
 * The GameWindow class is responsible for creating and managing the main
 * application window for the game.
 * It uses Java Swing to create a JFrame and a Canvas where the game will be
 * rendered.
 */
public class GameWindow implements Window {

    // --- Private fields for the JFrame and Canvas components
    private JFrame jFrame;
    private Canvas canvas;
    private ScreenState screenState = ScreenState.WINDOWED; // Default screen state
    private ScreenState preferredFullscreenState = ScreenState.BORDERLESS_FULLSCREEN; // Default fullscreen target
    private String title;
    private int width;
    private int height;
    private Game game;

    /**
     * Constructs a new GameWindow with the specified title, width, and height.
     * 
     * @param title  The title of the window.
     * @param width  The width of the window in pixels.
     * @param height The height of the window in pixels.
     */
    public GameWindow(String title, int width, int height, Game game) {

        this.title = title;
        this.width = width;
        this.height = height;
        this.game = game;
        initWindow();
    }

    /**
     * Initializes the JFrame and Canvas for the game window.
     */
    private void initWindow() {
        jFrame = new JFrame(title);

        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));

        // We keep the focus on the JFrame or manage it via an InputHandler later.
        // Setting this to false prevents the canvas from unexpectedly "stealing"
        // keyboard focus.
        canvas.setFocusable(false);

        // Prevents AWT from repainting the canvas, fixing flickering and FPS drops
        canvas.setIgnoreRepaint(true);

        jFrame.add(canvas);
        setScreenState(screenState);
        jFrame.requestFocus();
    }

    /**
     * Sets the screen state of the window (Windowed, Fullscreen, Borderless).
     * Handles disposing and recreating the window peer if necessary for decoration
     * changes.
     * 
     * @param state The desired ScreenState.
     */
    public void setScreenState(ScreenState state) {
        this.screenState = state;

        // Must be disposed to change decoration style or full-screen mode
        if (jFrame.isDisplayable()) {
            jFrame.dispose();
        }

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        // Reset common properties
        jFrame.setUndecorated(false);
        jFrame.setResizable(false);
        gd.setFullScreenWindow(null); // Exit exclusive fullscreen

        switch (this.screenState) {
            case WINDOWED:
                canvas.setPreferredSize(new Dimension(width, height));
                jFrame.pack();
                jFrame.setLocationRelativeTo(null);
                break;

            case BORDERLESS_FULLSCREEN:
                jFrame.setUndecorated(true);
                jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                break;

            case FULLSCREEN:
                jFrame.setUndecorated(true);
                if (gd.isFullScreenSupported()) {
                    gd.setFullScreenWindow(jFrame);
                } else {
                    // Fallback to Borderless if exclusive fullscreen is not supported
                    jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
                break;
        }

        jFrame.setVisible(true);
        jFrame.requestFocus();
        // SwingUtilities.invokeLater(() -> canvas.requestFocus()); // Re-requests focus
        // on the Canvas
    }

    /**
     * Adds a KeyListener to the JFrame to handle keyboard events.
     * 
     * @param listener The KeyListener implementation.
     */
    public void addKeyListener(KeyListener listener) {
        jFrame.addKeyListener(listener);
        // canvas.addKeyListener(listener);
    }

    /**
     * Toggles between Windowed mode and the preferred Fullscreen mode (Borderless
     * or Exclusive).
     */
    public void toggleFullScreen() {
        if (screenState == ScreenState.WINDOWED) {
            setScreenState(preferredFullscreenState);
        } else {
            setScreenState(ScreenState.WINDOWED);
        }
    }

    /**
     * Sets the preferred fullscreen mode (BORDERLESS_FULLSCREEN or FULLSCREEN) for
     * the toggle function.
     */
    public void setPreferredFullscreenState(ScreenState state) {
        this.preferredFullscreenState = state;
    }

    // --- Getters to access the Canvas and JFrame if needed
    public Canvas getCanvas() {
        return canvas;
    }

    public JFrame getJFrame() {
        return jFrame;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public Graphics2D getGraphics() {
        return (Graphics2D) canvas.getBufferStrategy().getDrawGraphics();
    }
}