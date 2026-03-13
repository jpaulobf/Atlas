package game;

import engine.Game;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * The Atlas game.
 */
public class Atlas extends Game {

    private float x = 100;
    private float y = 100;
    private float prevX = 100;
    private float prevY = 100;
    private float speed = 400.0f; // Pixels por segundo
    private int playerWidth = 50;
    private int playerHeight = 100;

    public Atlas() {
        super("Atlas Game", 1600, 900, 480);
    }

    @Override
    public void init() {
    }

    @Override
    public void onUpdate(long deltaTime) {
        // Armazena a posição antes de atualizar (para interpolação)
        prevX = x;
        prevY = y;

        // Converte deltaTime (nanosegundos) para segundos para o cálculo de física
        double deltaSeconds = deltaTime / 1_000_000_000.0;
        float moveAmount = (float) (speed * deltaSeconds);

        if (input.isKeyDown(KeyEvent.VK_W)) y -= moveAmount;
        if (input.isKeyDown(KeyEvent.VK_S)) y += moveAmount;
        if (input.isKeyDown(KeyEvent.VK_A)) x -= moveAmount;
        if (input.isKeyDown(KeyEvent.VK_D)) x += moveAmount;

        // Limites da tela (Colisão com bordas)
        int screenWidth = gameWindow.getCanvas().getWidth();
        int screenHeight = gameWindow.getCanvas().getHeight();

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x + playerWidth > screenWidth) x = screenWidth - playerWidth;
        if (y + playerHeight > screenHeight) y = screenHeight - playerHeight;
    }

    @Override
    public void onRender(Graphics g, double interpolation) {
        // Calcula a posição visual interpolando entre a posição anterior e a atual
        int renderX = (int) (prevX + (x - prevX) * interpolation);
        int renderY = (int) (prevY + (y - prevY) * interpolation);

        g.setColor(Color.RED);
        g.fillRect(renderX, renderY, playerWidth, playerHeight);
    }

    public static void main(String[] args) {
        new Atlas().start();
    }
}
