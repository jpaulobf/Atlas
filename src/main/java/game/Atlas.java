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
    private float speed = 600.0f; // Pixels per second
    private float velX;
    private float velY;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float squashCycleX = 0.0f;
    private float squashCycleY = 0.0f;
    private float squashSpeed = 15.0f; // Controla a velocidade da animação (quanto maior, mais rápido o 'boing')
    private int playerWidth = 100;
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
        
        // Animação de Squash no eixo X (Senoide de 0 a PI)
        if (squashCycleX > 0) {
            squashCycleX += squashSpeed * deltaSeconds;
            if (squashCycleX >= Math.PI) {
                squashCycleX = 0;
                scaleX = 1.0f;
            } else {
                scaleX = 1.0f - 0.3f * (float) Math.sin(squashCycleX);
            }
        }
        
        // Animação de Squash no eixo Y
        if (squashCycleY > 0) {
            squashCycleY += squashSpeed * deltaSeconds;
            if (squashCycleY >= Math.PI) {
                squashCycleY = 0;
                scaleY = 1.0f;
            } else {
                scaleY = 1.0f - 0.3f * (float) Math.sin(squashCycleY);
            }
        }

        // Apply velocity to position
        x += velX * deltaSeconds;
        y += velY * deltaSeconds;

        if (x < 0) {
            x = 0;
            velX *= -1; // Bounce horizontally
            if (squashCycleX == 0) squashCycleX = 0.01f; // Inicia animação suave
            boingX.play();
        } else if (x + playerWidth > CANVAS_WIDTH) {
            x = CANVAS_WIDTH - playerWidth;
            velX *= -1; // Bounce horizontally
            if (squashCycleX == 0) squashCycleX = 0.01f; // Inicia animação suave
            boingX.play();
        }

        if (y < 0) {
            y = 0;
            velY *= -1; // Bounce vertically
            if (squashCycleY == 0) squashCycleY = 0.01f; // Inicia animação suave
            boingY.play();
        } else if (y + playerHeight > CANVAS_HEIGHT) {
            y = CANVAS_HEIGHT - playerHeight;
            velY *= -1; // Bounce vertically
            if (squashCycleY == 0) squashCycleY = 0.01f; // Inicia animação suave
            boingY.play();
        }
    }

    @Override
    public void onRender(Graphics g, double interpolation) {
        // Calculate visual position interpolating between previous and current position
        float interpX = (float) (prevX + (x - prevX) * interpolation);
        float interpY = (float) (prevY + (y - prevY) * interpolation);

        // Calcula o tamanho deformado
        float drawnWidth = playerWidth * scaleX;
        float drawnHeight = playerHeight * scaleY;

        // Centraliza o desenho para que a deformação ocorra para o meio, e não para o topo-esquerda
        float drawX = interpX + (playerWidth - drawnWidth) / 2.0f;
        float drawY = interpY + (playerHeight - drawnHeight) / 2.0f;

        g.setColor(Color.RED);
        g.fillRect((int)drawX, (int)drawY, (int)drawnWidth, (int)drawnHeight);
    }

    public static void main(String[] args) {
        new Atlas().start();
    }
}
