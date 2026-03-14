package game;

import engine.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import audio.Audio;


/**
 * The Atlas game.
 */
public class Atlas extends Game {

    private float x = 100;
    private float y = 100;
    private float prevX = 100;
    private float prevY = 100;
    private float speed = 400.0f; // Pixels per second
    private int playerWidth = 50;
    private int playerHeight = 100;
    private Audio boing;

    public Atlas() {
        super("Atlas Game", 1600, 900, 480);
    }

    @Override
    public void init() {
        // Resource loading should happen in init()
        boing = new Audio("/audio/boing.ogg");
    }

    @Override
    public void onUpdate(long deltaTime) {
        // Store position before update (for interpolation)
        prevX = x;
        prevY = y;

        // Convert deltaTime (nanoseconds) to seconds for physics calculation
        double deltaSeconds = deltaTime / 1_000_000_000.0;
        float moveAmount = (float) (speed * deltaSeconds);

        if (input.isKeyDown(KeyEvent.VK_W)) y -= moveAmount;
        if (input.isKeyDown(KeyEvent.VK_S)) y += moveAmount;
        if (input.isKeyDown(KeyEvent.VK_A)) x -= moveAmount;
        if (input.isKeyDown(KeyEvent.VK_D)) x += moveAmount;

        // Screen limits (Collision with borders)
        int screenWidth = gameWindow.getCanvas().getWidth();
        int screenHeight = gameWindow.getCanvas().getHeight();

        if (x < 0) {
            x = 0;
            boing.play();
        } else if (x + playerWidth > screenWidth) {
            x = screenWidth - playerWidth;
            boing.play();
        }

        if (y < 0) {
            y = 0;
            boing.play();
        } else if (y + playerHeight > screenHeight) {
            y = screenHeight - playerHeight;
            boing.play();
        }
    }

    @Override
    public void onRender(Graphics g, double interpolation) {
        // Calculate visual position interpolating between previous and current position
        int renderX = (int) (prevX + (x - prevX) * interpolation);
        int renderY = (int) (prevY + (y - prevY) * interpolation);

        g.setColor(Color.RED);
        g.fillRect(renderX, renderY, playerWidth, playerHeight);
    }

    public static void main(String[] args) {
        new Atlas().start();
    }
}
