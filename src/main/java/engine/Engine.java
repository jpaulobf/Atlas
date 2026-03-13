package engine;

/**
 * Manages the main game loop, ensuring the game logic runs at a fixed update rate (UPS)
 * while allowing for a variable or capped rendering frame rate (FPS).
 * This class implements a fixed timestep pattern with an accumulator to provide deterministic updates,
 * and an optional, precise sleep logic to cap the FPS, compensating for `Thread.sleep` inaccuracies.
 * The `run()` method is the heart of the engine, orchestrating updates, rendering with interpolation,
 * and performance tracking.
 */
public class Engine implements Runnable {
    
    // Game loop control variables
    private boolean running = false;
    private Thread thread;
    private Game game;
    private int targetFps = 60;
    private final int MAX_UPDATES_BEFORE_RENDER = 5; // Prevents the "spiral of death"
    private final int UPS = 60; // Logical Updates Per Second (Physics/Logic)
    private volatile int updates = 0;

    /**
     * Engine constructor, receives a game instance to manage the game loop.
     * @param game The game instance that will be updated and rendered by the game loop.
     * @param targetFps The target frames per second to cap rendering at. Use 0 for unlimited FPS.
     */
    public Engine(Game game, int targetFps) {
        this.game = game;
        this.targetFps = targetFps;
    }

    /**
     * Starts the game loop in a new thread. Does nothing if the loop is already running.
     */
    public synchronized void start() {
        if (running)
            return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Stops the game loop and waits for the thread to finish.
     */
    public synchronized void stop() {
        if (!running)
            return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main game loop method.
     * It uses a fixed timestep with an accumulator to call the game's update method a consistent number of times per second.
     * It then calls the render method, providing an interpolation value for smooth graphics.
     * If a target FPS is set, it uses a precise sleep logic to control the frame rate.
     * FPS and UPS are counted and displayed in the console each second.
     */
    @Override
    public void run() {
        long timer = System.currentTimeMillis();
        int frames = 0;

        // Nanoseconds per update based on UPS (1s / 60)
        final double nsPerUpdate = 1000000000.0 / UPS;
        
        // Nanoseconds per frame for FPS control (if applicable)
        final double nsPerFrame = (targetFps > 0) ? 1000000000.0 / targetFps : 0;
        
        long overSleep = 0; // Variable to compensate for Thread.sleep inaccuracy
        long lastTime = System.nanoTime();
        
        // Accumulator for the Fixed Time Step
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            long elapsed = now - lastTime;
            lastTime = now;
            
            // Add the elapsed time to the accumulator (in update slices)
            delta += elapsed / nsPerUpdate;

            int updateCount = 0;
            // While there is enough accumulated time for an update, update the game
            while (delta >= 1 && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                // We pass a fixed value to ensure determinism
                game.update((long) nsPerUpdate);
                delta--;
                updates++;
                updateCount++;
            }
            
            // The remaining 'delta' value is the interpolation factor for smooth rendering
            game.render(delta);

            frames++;

            // FPS counter (resets every second)
            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                game.renderFPS(frames);
                frames = 0;
                updates = 0;
            }

            // Precise Sleep Logic with Compensation
            if (targetFps > 0) {
                long workTime = System.nanoTime() - now;
                long sleepTime = (long) (nsPerFrame - workTime) - overSleep;

                if (sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime / 1000000, (int) (sleepTime % 1000000));
                    } catch (InterruptedException e) {
                    }

                    // Calculate how long we actually slept vs how long we wanted to
                    long actualSleep = System.nanoTime() - (now + workTime);
                    overSleep = actualSleep - sleepTime;
                } else {
                    // If we are behind (lag), don't sleep and reset the compensation
                    overSleep = 0;
                }
            }
        }
    }

    public int getUpdates() {
        return updates;
    }
}