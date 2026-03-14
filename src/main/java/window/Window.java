package window;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;

import engine.Game;

/**
 * The Window interface defines the contract for any window implementation used
 * in
 * the game engine. It abstracts away the details of how the window is created
 * and managed, allowing for different implementations (e.g., using Swing, AWT,
 * or even a custom rendering system) without affecting the game logic.
 * It provides methods for adding key listeners, retrieving the graphics context
 * for rendering, accessing the game instance, and toggling fullscreen mode.
 * Implementing classes must provide concrete implementations for these methods
 * to
 * integrate with the game engine and allow for user input and rendering.
 */
public interface Window {

    /**
     * Adds a KeyListener to the window to handle keyboard input events.
     * 
     * @param listener
     */
    void addKeyListener(KeyListener listener);

    /**
     * Retrieves the Graphics2D context for rendering. This method should return a
     * valid Graphics2D object that can be used for drawing operations. The caller
     * is responsible for managing the graphics context (e.g., disposing it after
     * use if necessary).
     * 
     * @return
     */
    Graphics2D getGraphics();

    /**
     * Returns the Canvas component associated with this window. The Canvas is where
     * the game will be rendered, and it can also be used for handling input events.
     * 
     * @return
     */
    Canvas getCanvas();

    /**
     * Returns the Game instance associated with this window. This allows the window
     * to access game state and logic, which can be useful for input handling or
     * other
     * interactions between the window and the game.
     * 
     * @return
     */
    Game getGame();

    /**
     * Toggles between fullscreen and windowed mode. The implementation should
     * handle
     * the necessary changes to the window's display mode and size when switching
     * between the two modes.
     */
    void toggleFullScreen();
}