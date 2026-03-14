package input;

import window.GameWindow;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Handles keyboard input events.
 */
public class KeyboardInputs implements KeyListener {

    private GameWindow gameWindow;
    private boolean[] keys = new boolean[1024];

    public KeyboardInputs(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Update key state
        if (e.getKeyCode() >= 0 && e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = true;
        }

        // Check for ALT + ENTER to toggle fullscreen
        if (e.getKeyCode() == KeyEvent.VK_ENTER && e.isAltDown()) {
            gameWindow.toggleFullScreen();
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {
            gameWindow.getGame().togglePause();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Update key state
        if (e.getKeyCode() >= 0 && e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = false;
        }
    }

    /**
     * Checks if a specific key is currently held down.
     * 
     * @param keyCode The KeyEvent code.
     */
    public boolean isKeyDown(int keyCode) {
        if (keyCode >= 0 && keyCode < keys.length) {
            return keys[keyCode];
        }
        return false;
    }
}