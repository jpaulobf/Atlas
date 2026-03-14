package game;

import engine.Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import audio.Audio;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The Atlas game.
 */
public class Atlas extends Game {

    private List<Entity> entities = new ArrayList<>();
    private Random random = new Random();
    private int level = 1; // Difficulty level (1 to 10)
    private BufferedImage background; // The background cache

    // Configurações visuais e lógicas das faixas
    private final int TOP_SAFE_ZONE = 100;
    private final int BOTTOM_SAFE_ZONE = 100;
    private final int MIDDLE_SAFE_ZONE = 120;
    private List<Projectile> projectiles = new ArrayList<>();

    public Atlas() {
        super("Atlas Game", 1600, 900, 60);
    }

    @Override
    public void init() {
        entities.clear();
        createBackground(); // Generates the background image once

        // Various colors
        Color[] colors = { Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.CYAN,
                Color.MAGENTA, Color.YELLOW, Color.PINK, Color.WHITE, Color.LIGHT_GRAY };

        int totalHeight = CANVAS_HEIGHT;

        // Available height for the lanes (Total - Safe Zones)
        int availableRoadHeight = totalHeight - TOP_SAFE_ZONE - BOTTOM_SAFE_ZONE - MIDDLE_SAFE_ZONE;

        // There are 8 lanes in total (4 above, 4 below)
        int laneHeight = availableRoadHeight / 8;
        int carSize = 60; // Slightly smaller than the lane
        int carYOffset = (laneHeight - carSize) / 2; // Center in the lane

        // Create the lanes
        for (int lane = 0; lane < 8; lane++) {
            // Calculate the Y position of the lane
            float laneY;
            if (lane < 4) {
                // Upper lanes (0 to 3)
                laneY = TOP_SAFE_ZONE + (lane * laneHeight);

            } else {
                // Lower lanes (4 to 7) - Skip the middle zone
                laneY = TOP_SAFE_ZONE + (4 * laneHeight) + MIDDLE_SAFE_ZONE + ((lane - 4) * laneHeight);
            }

            // Define the speed and direction of the lane
            // Even lanes go to the right, odd lanes to the left (example)
            boolean movingRight = (lane % 2 == 0);

            // Speed scales with the level (From 150px/s at Lvl 1 to ~600px/s at Lvl 10)
            float baseSpeed = 150.0f + (level * 40.0f);
            float speed = baseSpeed + random.nextFloat() * 100.0f; // Variação aleatória
            if (!movingRight)
                speed *= -1;

            // Number of cars scales with the level (More difficult = more cars)
            // Level 1-2: 1 carro. Level 10: até 4 carros.
            int maxCars = 1 + (int) Math.ceil(level / 3.0);
            int carsInLane = random.nextInt(maxCars) + 1;

            List<Entity> laneEntities = new ArrayList<>(); // Lista temporária para checar sobreposição na faixa atual

            for (int i = 0; i < carsInLane; i++) {
                float startX = 0;
                boolean overlaps = true;
                int attempts = 0;

                // Try to find a free position (maximum of 50 attempts to avoid blocking the
                // loop)
                while (overlaps && attempts < 50) {
                    startX = random.nextInt(CANVAS_WIDTH);
                    overlaps = false;

                    for (Entity other : laneEntities) {
                        // Check if it is too close to another car (50px gap)
                        float safeGap = 100.0f;
                        if (startX < other.x + other.width + safeGap && startX + carSize + safeGap > other.x) {
                            overlaps = true;
                            break;
                        }
                    }
                    attempts++;
                }

                // Only add if you found a safe place
                if (!overlaps) {
                    Color color = colors[random.nextInt(colors.length)];

                    Entity car = random.nextBoolean()
                            ? new Square(startX, laneY + carYOffset, carSize, carSize, speed, 0, color)
                            : new Circle(startX, laneY + carYOffset, carSize, carSize, speed, 0, color);

                    car.setShootingInterval(3 + random.nextInt(3));
                    entities.add(car);
                    laneEntities.add(car);
                }
            }
        }
    }

    @Override
    public void onUpdate(long deltaTime) {
        for (Entity e : entities) {
            e.update(deltaTime, CANVAS_WIDTH, CANVAS_HEIGHT);
        }

        List<Projectile> toRemove = new ArrayList<>();
        for (Projectile p : projectiles) {
            p.update(deltaTime, CANVAS_WIDTH, CANVAS_HEIGHT);
            if (p.y > CANVAS_HEIGHT) {
                toRemove.add(p);
            }
        }

        projectiles.removeAll(toRemove);

        // Generate the rain
        generateRain(deltaTime);

        // We remove the collision between cars to simulate fluid traffic (Freeway
        // style)
    }

    private void generateRain(long deltaTime) {
        if (random.nextInt(2) == 1)
            projectiles.add(createRainProjectile(random.nextInt(CANVAS_WIDTH), 0));
    }

    @Override
    public void onRender(Graphics g, double interpolation) {
        if (background != null) {
            g.drawImage(background, 0, 0, null);
        }

        // --- Draw Entities ---
        for (Entity e : entities) {
            e.render(g, interpolation);
        }

        for (Projectile p : projectiles) {
            p.render(g, interpolation);
        }
    }

    private Projectile createRainProjectile(float x, float y) {
        float speed = 300;
        float velX = 0;
        float velY = speed;
        return new Projectile(x, y, 3, 15, velX, velY, new Color(173, 216, 230));
    }

    /**
     * Renders the static scenario in a BufferedImage to optimize onRender.
     */
    private void createBackground() {
        background = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = background.getGraphics();

        int availableRoadHeight = CANVAS_HEIGHT - TOP_SAFE_ZONE - BOTTOM_SAFE_ZONE - MIDDLE_SAFE_ZONE;
        int laneHeight = availableRoadHeight / 8;
        int roadHeightBlock = laneHeight * 4;

        // 1. Safe Zones (Grass/Green)
        g.setColor(new Color(34, 139, 34)); // Forest Green
        g.fillRect(0, 0, CANVAS_WIDTH, TOP_SAFE_ZONE); // Top

        int middleY = TOP_SAFE_ZONE + roadHeightBlock;
        g.fillRect(0, middleY, CANVAS_WIDTH, MIDDLE_SAFE_ZONE); // Middle

        int bottomY = middleY + MIDDLE_SAFE_ZONE + roadHeightBlock;
        g.fillRect(0, bottomY, CANVAS_WIDTH, BOTTOM_SAFE_ZONE); // Base

        // 2. Roads (Asphalt/Gray)
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, TOP_SAFE_ZONE, CANVAS_WIDTH, roadHeightBlock); // Upper Road
        g.fillRect(0, middleY + MIDDLE_SAFE_ZONE, CANVAS_WIDTH, roadHeightBlock); // Lower Road

        // 3. Lane lines
        g.setColor(Color.WHITE);
        for (int i = 1; i < 4; i++) {
            int lineY = TOP_SAFE_ZONE + (i * laneHeight);
            g.fillRect(0, lineY, CANVAS_WIDTH, 3);

            lineY = middleY + MIDDLE_SAFE_ZONE + (i * laneHeight);
            g.fillRect(0, lineY, CANVAS_WIDTH, 3);
        }
        g.dispose(); // Releases the temporary graphic resource of the image
    }

    // --- Abstract Base Entity ---
    abstract class Entity {
        float x, y, prevX, prevY;
        float velX, velY;
        int width, height;
        Color color;
        Audio audio;
        private float timeSinceLastShot = 0;

        public Entity(float x, float y, int w, int h, float vx, float vy, Color c) {
            this.x = x;
            this.y = y;
            this.prevX = x;
            this.prevY = y;
            this.width = w;
            this.height = h;
            this.velX = vx;
            this.velY = vy;
            this.color = c;
            // Each square has its own independent audio
            this.audio = new Audio("/audio/boing.ogg");
        }

        private float shootingInterval = 3;

        public void setShootingInterval(float shootingInterval) {
            this.shootingInterval = shootingInterval;
        }

        public boolean canShoot() {
            return timeSinceLastShot >= shootingInterval;
        }

        public void update(long deltaTime, int canvasWidth, int canvasHeight) {
            prevX = x;
            prevY = y;
            double deltaSeconds = deltaTime / 1_000_000_000.0;

            // Movement
            x += velX * deltaSeconds;
            y += velY * deltaSeconds;

            // "Wrap-around" logic (cross the screen)
            // If you exit to the right, return to the left
            if (velX > 0 && x > canvasWidth) {
                x = -width;
                prevX = x; // Resets the history to avoid cross interpolation (flicker)
            }

            // If you leave on the left, return to the right
            if (velX < 0 && x + width < 0) {
                x = canvasWidth;
                prevX = x; // Reseta o histórico para evitar interpolação cruzada (flicker)
            }

            timeSinceLastShot += deltaSeconds;
        }

        public abstract void render(Graphics g, double interpolation);

        public void playBoing() {
            audio.play();
        }
    }

    // --- Concrete Implementations ---

    class Square extends Entity {
        public Square(float x, float y, int w, int h, float vx, float vy, Color c) {
            super(x, y, w, h, vx, vy, c);
        }

        @Override
        public void render(Graphics g, double interpolation) {
            float interpX = (float) (prevX + (x - prevX) * interpolation);
            float interpY = (float) (prevY + (y - prevY) * interpolation);

            g.setColor(color);
            g.fillRect((int) interpX, (int) interpY, (int) width, (int) height);
        }
    }

    class Circle extends Entity {
        public Circle(float x, float y, int w, int h, float vx, float vy, Color c) {
            super(x, y, w, h, vx, vy, c);
        }

        @Override
        public void render(Graphics g, double interpolation) {
            float interpX = (float) (prevX + (x - prevX) * interpolation);
            float interpY = (float) (prevY + (y - prevY) * interpolation);

            g.setColor(color);
            g.fillOval((int) interpX, (int) interpY, (int) width, (int) height);
        }
    }

    class Projectile {
        float x, y;

        float velX, velY;
        int width, height;
        Color color;

        public Projectile(float x, float y, int w, int h, float vx, float vy, Color c) {
            this.x = x;
            this.y = y;
            this.width = w;
            this.height = h;
            this.velX = vx;
            this.velY = vy;
            this.color = c;
        }

        public void update(long deltaTime, int canvasWidth, int canvasHeight) {
            double deltaSeconds = deltaTime / 1_000_000_000.0;
            y += velY * deltaSeconds;
            x += velX * deltaSeconds;

        }

        public void render(Graphics g, double interpolation) {
            g.setColor(color);
            g.fillOval((int) x, (int) y, (int) width, (int) height);
        }
    }

    public static void main(String[] args) {
        new Atlas().start();
    }
}
