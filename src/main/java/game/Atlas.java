package game;

import engine.Game;
import java.awt.Color;
import java.awt.Graphics;
import audio.Audio;
import java.util.Random;


/**
 * The Atlas game.
 */
public class Atlas extends Game {

    private float x = 100;
    private float y = 100;
    private float prevX = 100;
    private float prevY = 100;
    private float speed = 800.0f; // Pixels per second
    private float velX;
    private float velY;
    private int playerWidth = 50;
    private int playerHeight = 100;
    private Audio boingX;
    private Audio boingY;
    private Random random = new Random();

    public Atlas() {
        super("Atlas Game", 1600, 900, 60);
    }

    @Override
    public void init() {
        // Resource loading should happen in init()
        boingX = new Audio("/audio/boing.ogg");
        boingY = new Audio("/audio/boing.ogg");

        // Random start position near center
        float centerX = CANVAS_WIDTH / 2.0f;
        float centerY = CANVAS_HEIGHT / 2.0f;

        // Randomize between (Center - 50) and (Center + 50)
        x = (centerX - 50) + random.nextFloat() * 100;
        y = (centerY - 50) + random.nextFloat() * 100;

        prevX = x;
        prevY = y;

        // Choose a random direction (0 to 360 degrees) and calculate velocity vectors
        double angle = random.nextDouble() * 2 * Math.PI; // 0 to 2PI radians
        velX = (float) (Math.cos(angle) * speed);
        velY = (float) (Math.sin(angle) * speed);
    }

    @Override
    public void onUpdate(long deltaTime) {
        // Store position before update (for interpolation)
        prevX = x;
        prevY = y;

        // Convert deltaTime (nanoseconds) to seconds for physics calculation
        double deltaSeconds = deltaTime / 1_000_000_000.0;
        
        // Apply velocity to position
        x += velX * deltaSeconds;
        y += velY * deltaSeconds;

        if (x < 0) {
            x = 0;
            velX *= -1; // Bounce horizontally
            boingX.play();
        } else if (x + playerWidth > CANVAS_WIDTH) {
            x = CANVAS_WIDTH - playerWidth;
            velX *= -1; // Bounce horizontally
            boingX.play();
        }

        if (y < 0) {
            y = 0;
            velY *= -1; // Bounce vertically
            boingY.play();
        } else if (y + playerHeight > CANVAS_HEIGHT) {
            y = CANVAS_HEIGHT - playerHeight;
            velY *= -1; // Bounce vertically
            boingY.play();
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
