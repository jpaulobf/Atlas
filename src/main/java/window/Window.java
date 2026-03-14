package window;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;

import engine.Game;

public interface Window {
    void addKeyListener(KeyListener listener);

    Graphics2D getGraphics();

    Canvas getCanvas();

    Game getGame();

    void toggleFullScreen();
}