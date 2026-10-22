package engine;

import entities.Bullet;
import entities.Character;
import entities.Enemy;
import entities.MovingBullet;
import entities.Weapon;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import javax.swing.*;
import windows.GameWindow;

public class GameEngine {
    
    public int screenWidth;
    public int screenHeight;
    private Image background;
    
    Character character;
    ArrayList<MovingBullet> bullets;
    ArrayList<Enemy> enemies;
    
    GameWindow gameWindow;
    GamePanel gamePanel;
    
    public int gameState = 0;
    
    public final int titleState = 0;
    public final int pausedState = 1;
    public final int playState = 2;
    public final int storeState = 3;
    public final int inventoryState = 4;
    public final int gameOverState = 5;
    
    public int roundCounter = 0;
    public int currentSizeUpgradeCount = 1;
    public int currentCooldownUpgradeCount = 1;
    
    static int FRAMES_PER_SECOND;
    static int TARGET_TIME;
    
    public GameEngine(int fps) {
        FRAMES_PER_SECOND = fps;
        TARGET_TIME = (int) (1000.0 / FRAMES_PER_SECOND);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
    }
    
    class GamePanel extends JPanel {
        
        public GamePanel() {
            this.setPreferredSize(new Dimension(screenWidth, screenHeight));
            this.setBackground(Color.black);
            this.setOpaque(true);
        }
        
        
        // redefining the default paintMathod so it draws our sprites
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            switch (gameState) {
                case titleState:
                    drawTitleScreen(g);
                    break;
                case playState:
                    drawPlayScreen(g);
                    break;
                case pausedState:
                    drawPausedScreen(g);
                    break;
                case storeState:
                    drawPausedScreen(g);
                    createShopOptions(g);
                    break;
                case gameOverState:
                    drawGameOverScreen(g);
                    break;
                default:
                    break;
            }
        }
    }
    
    void updateCharacterRotationAngle() {
        // getting the absolute position of the pointer
        Point mouseOnScreen = MouseInfo.getPointerInfo().getLocation();
        Point panelOnScreen = gamePanel.getLocationOnScreen();
        // transforming the absolute position into relative to the screen
        Point mousePosition = new Point(
                mouseOnScreen.x - panelOnScreen.x,
                mouseOnScreen.y - panelOnScreen.y);
        
        // difference between the character and the pointer forms an angle...
        int diffX = mousePosition.x - character.getPositionX();
        int diffY = mousePosition.y - character.getPositionY();
        // ... derived by two direction vectors
        double newAngle = Math.atan2(diffY, diffX);
        
        character.setRotationAngle(newAngle);
    }
    
    void updateCurrentWeapon() {
        Weapon currentWeapon = character.getCurrentWeapon();
        if (currentWeapon != null) {
            currentWeapon.reduceCooldown();
        }
    }
    
    void updateCharacter() {
        updateCharacterRotationAngle();
        updateCurrentWeapon();
    }
    
    void updateBullets() {
        // detecting bullets out of the screen size
        ArrayList<MovingBullet> forRemoval = new ArrayList<>();
        for (MovingBullet iterBullet : bullets) {
            // updating the bullets position
            iterBullet.move();
            if (iterBullet.isOutOfBounds(
                    gameWindow.getWidth(),
                    gameWindow.getHeight())) {
                forRemoval.add(iterBullet);
            }
        }
        bullets.removeAll(forRemoval);
    }
    
    void updateEnemyRotationAngle(Enemy enemy) {
        int diffX = character.getPositionX() - enemy.getPositionX();
        int diffY = character.getPositionY() - enemy.getPositionY();
        double newAngle = Math.atan2(diffY, diffX);
        enemy.setRotationAngle(newAngle);
    }
    
    void updateEnemies() {
        ArrayList<Enemy> deadEnemies = new ArrayList<>();
        for (Enemy iterEnemy : enemies) {
            updateEnemyRotationAngle(iterEnemy);
            if (iterEnemy.getCanMove()) {
                iterEnemy.move();
            }
            if (iterEnemy.getHealthPoints() <= 0) {
                deadEnemies.add(iterEnemy);
                character.changeMoney(iterEnemy.getMonetaryValue());
            }
        }
        enemies.removeAll(deadEnemies);
    }
    
    void chooseShopOption(int keyCode) {
        int priceSize = 100 * currentSizeUpgradeCount;
        int priceCooldown = 100 * currentCooldownUpgradeCount;
        if (keyCode == KeyEvent.VK_Q && character.getMoney() >= priceSize) {
            Bullet bulletType = character.getCurrentWeapon().getBulletType();
            bulletType.setRadius((int) (bulletType.getRadius() * 1.5));
            character.changeMoney(-1 * priceSize);
            currentSizeUpgradeCount++;
        } else if (character.getMoney() >= priceCooldown) {
            Weapon currentWeapon = character.getCurrentWeapon();
            currentWeapon.setCooldown(
                    (int) (currentWeapon.getDefaultCooldown() * 0.7));
            character.changeMoney(-1 * priceCooldown);
            currentCooldownUpgradeCount++;
        }
    }
    
    void createShopOptions(Graphics g) {
        Image optionBackgroundImage;
        int priceSize = 100 * currentSizeUpgradeCount;
        int priceCooldown = 100 * currentCooldownUpgradeCount;
        try {
            optionBackgroundImage = ImageIO.read(
                    GameWindow.class.getResource(
                            "/images/backgrounds/optionBackground.png"));
        } catch (IOException ex) {
            System.out.println("NO IMAGE FOR SHOPPING OPTIONS FOUND");
            return;
        }
        
        g.setColor(Color.black);
        g.drawImage(
                optionBackgroundImage,
                (int) (screenWidth * 0.2),
                (int) (screenHeight * 0.3),
                (int) (screenWidth * 0.2),
                (int) (screenHeight * 0.4),
                gamePanel);
        g.setColor(Color.white);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 20F));
        g.drawString("(Q) SIZE +50%",
                (int) (screenWidth * 0.225),
                (int) (screenHeight * 0.65)
        );
        g.drawImage(character.getCurrentWeapon().getBulletType().getImage(),
                (int) (screenWidth * 0.225),
                (int) (screenHeight * 0.325),
                (int) (screenWidth * 0.15),
                (int) (screenWidth * 0.15),
                gamePanel);
        
        g.setColor(Color.black);
        g.drawImage(
                optionBackgroundImage,
                (int) (screenWidth * 0.6),
                (int) (screenHeight * 0.3),
                (int) (screenWidth * 0.2),
                (int) (screenHeight * 0.4),
                gamePanel);
        g.setColor(Color.white);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 20F));
        g.drawString("(E) COOLDOWN -30%",
                (int) (screenWidth * 0.625),
                (int) (screenHeight * 0.65)
        );
        Weapon w = character.getCurrentWeapon();
        g.drawImage(w.getImage(),
                (int) (screenWidth * 0.625),
                (int) (screenHeight * 0.4),
                (int) (screenWidth * 0.15),
                (int) (screenWidth * 0.15 * w.getHeight() / w.getWidth()),
                gamePanel);
        g.setColor(Color.black);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 28F));
        g.drawString("Price of size upgrade: " + Integer.toString(priceSize), 
                (int) (screenWidth * 0.18), (int) (screenHeight * 0.75));
        g.drawString("Price of cooldown upgrade: " + Integer.toString(priceCooldown), 
                (int) (screenWidth * 0.56), (int) (screenHeight * 0.75));
        
    }

    void update() {
        updateCharacter();
        if (gameState == 2) {
            if (enemies.isEmpty()) {
                roundCounter++;
                gameState = storeState;
            } else {
                updateEnemies();
                updateBullets();
                checkCollisions();
            }
        }
    }
    
    void drawTitleScreen(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, screenWidth, screenHeight);
        
        g.setFont(g.getFont().deriveFont(Font.BOLD, 120F));
        //g.drawString("Zombie Shooter", (screenWidth 
         //       - g.getFontMetrics().stringWidth("Zombie Shooter")) / 2, screenHeight / 2 - 100);
        
        
        g.setColor(Color.white);
        g.drawString("Zombie Shooter", (screenWidth 
                - g.getFontMetrics().stringWidth("Zombie Shooter")) 
                / 2, screenHeight / 2);
        
        g.setFont(g.getFont().deriveFont(Font.ITALIC, 40F));
        g.drawString("Press Space to start", (screenWidth 
                - g.getFontMetrics().stringWidth("Press Space to start")) / 2, screenHeight / 2 + 100);
    }
    
    void drawPlayScreen(Graphics g) {
        drawBackgroundScreen(g);
        drawBullets(g);
        drawCharacter(g);
        drawEnemies(g);
    }
    
    void drawPausedScreen(Graphics g) {
        drawBackgroundScreen(g);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 25F));
        String text = "Press B to begin round";
        
        g.setColor(Color.black);
        g.drawString(text, 30, 700);
        drawCharacter(g);
    }
    
    void drawGameOverScreen(Graphics g) {
        drawBackgroundScreen(g);
        drawCharacter(g);
        
        g.setColor(new Color(0, 0, 0));
        g.fillRect(0, 0, screenWidth, screenHeight);
        
        g.setColor(Color.red);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 160F));
        String text = "GAME OVER";
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (screenWidth - textWidth) / 2, screenHeight / 2 - 20);
        
        g.setColor(Color.white);
        g.setFont(g.getFont().deriveFont(Font.ITALIC, 40F));
        text = "Press R to restart";
        textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, (screenWidth - textWidth) / 2, screenHeight / 2 + 160);
        
        g.setColor(Color.white);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 25F));
        text = "Rounds survived: " + Integer.toString(roundCounter);
        g.drawString(text, screenWidth - 225, 30);
    }
    
    void drawBackgroundScreen(Graphics g) {
        if (background != null) {
            g.drawImage(background, 0, 0, screenWidth, screenHeight, gamePanel);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }
        
        g.setColor(Color.black);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 25F));
        String text = "Rounds survived: " + Integer.toString(roundCounter);
        g.drawString(text, screenWidth - 250, 30);
        
        text = "Money: " + Integer.toString(character.getMoney());
        g.drawString(text, screenWidth - 150, 60);
    }
    
    void drawCharacterSprite(Graphics2D g2d) {
        int topLeftCornerX = character.getPositionX() - character.getSizeX() / 2;
        int topLeftCornerY = character.getPositionY() - character.getSizeY() / 2;
        
        String imagePath = character.getImagePath("character");
        if (imagePath == null) {
            // default representation of the character
            g2d.setColor(Color.red);
            g2d.fillRect(
                    topLeftCornerX,
                    topLeftCornerY,
                    character.getSizeX(),
                    character.getSizeY());
        } else {
            character.draw(g2d,
                    topLeftCornerX,
                    topLeftCornerY,
                    character.getSizeX(),
                    character.getSizeY(),
                    gamePanel);
        }
    }
    
    void drawWeaponSprite(Graphics2D g2d) {
        Weapon currentWeapon = character.getCurrentWeapon();
        if (currentWeapon != null) {
            int topLeftCornerX = character.getPositionX();
            int topLeftCornerY = character.getPositionY() 
                    - currentWeapon.getHeight() / 2;
            String imagePath = currentWeapon.getImagePath("weapon");
            if (imagePath == null) {
                // default representation of the weapon
                g2d.setColor(currentWeapon.getColor());
                g2d.fillRect(topLeftCornerX,
                        topLeftCornerY,
                        currentWeapon.getWidth(),
                        currentWeapon.getHeight()
                );
            } else {
                currentWeapon.draw(g2d,
                        topLeftCornerX,
                        topLeftCornerY,
                        currentWeapon.getWidth(),
                        currentWeapon.getHeight(),
                        gamePanel);
            }
            // drawing the ammo (DEPRECATED?)
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Times New Roman",
                    Font.PLAIN,
                    currentWeapon.getHeight() / 2)
            );
            g2d.drawString(String.valueOf(currentWeapon.getAmmo()),
                    character.getPositionX() + currentWeapon.getWidth() / 4,
                    character.getPositionY()
            );
        }
    }
    
    void drawEnemySprite(Graphics2D g2d, Enemy enemy) {
        int topLeftCornerX = enemy.getPositionX() - enemy.getSizeX() / 2;
        int topLeftCornerY = enemy.getPositionY() - enemy.getSizeY() / 2;
        
        String imagePath = enemy.getImagePath("enemy");
        
        if (imagePath == null) {
            // default representation of the character
            g2d.setColor(Color.red);
            g2d.fillRect(
                    topLeftCornerX,
                    topLeftCornerY,
                    enemy.getSizeX(),
                    enemy.getSizeY());
        } else {
            enemy.draw(g2d,
                    topLeftCornerX,
                    topLeftCornerY,
                    enemy.getSizeX(),
                    enemy.getSizeY(),
                    gamePanel);
        }
    }
    
    void drawCharacter(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        if (character == null) {
            return;
        }
        
        // rotating the whole game panel because the character is rotated
        AffineTransform initialState = g2d.getTransform();
        AffineTransform newState = new AffineTransform();
        newState.rotate(character.getRotationAngle(),
                character.getPositionX(),
                character.getPositionY());
        g2d.transform(newState);
        
        // and only then drawing the character and his weapon horizontally
        drawWeaponSprite(g2d);
        
        drawCharacterSprite(g2d);
        
        g2d.setTransform(initialState);

    }
    
    void drawBullets(Graphics g) {
        for (MovingBullet iterBullet : bullets) {
            String imagePath = iterBullet.getImagePath("bullet");
            // default representation - an oval with border
            if (imagePath == null) {
                g.setColor(iterBullet.getColor());
                g.fillOval(iterBullet.getPositionX(),
                        iterBullet.getPositionY(),
                        iterBullet.getRadius(),
                        iterBullet.getRadius());
                g.setColor(Color.black);
                g.drawOval(iterBullet.getPositionX(),
                        iterBullet.getPositionY(),
                        iterBullet.getRadius(),
                        iterBullet.getRadius());
            } else {
                
                Graphics2D g2d = (Graphics2D) g;
                
                int radius = iterBullet.getRadius();
                iterBullet.draw(g2d,
                        iterBullet.getPositionX() - radius,
                        iterBullet.getPositionY() - radius,
                        radius * 2,
                        radius * 2,
                        gamePanel);
            }
        }
    }
    
    void drawEnemies(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        for (Enemy iterEnemy : enemies) {
            AffineTransform initialState = g2d.getTransform();
            AffineTransform newState = new AffineTransform();
            newState.rotate(iterEnemy.getRotationAngle(), 
                    iterEnemy.getPositionX(), 
                    iterEnemy.getPositionY());
            g2d.transform(newState);
            
            drawEnemySprite(g2d, iterEnemy);
            
            g2d.setTransform(initialState);
        }
    }
    
    void draw() {
        // running an update on all elements drawn
        gameWindow.repaint();
    }
    
    void constructWindow() {
        gameWindow = new GameWindow();
        
        gameWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameWindow.setSize(screenWidth, screenHeight);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setTitle("GAME");
        
        // spawning bullets on pressing SPACE
        gameWindow.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (gameState == 0 && keyCode == KeyEvent.VK_SPACE) {
                    gameState = pausedState;
                } else if (keyCode == KeyEvent.VK_SPACE && gameState == playState) {
                    spawnBullet();
                } else if ((gameState == pausedState || gameState == storeState) && 
                        keyCode == KeyEvent.VK_B) {
                    createEntities();
                    gameState = playState;
                } else if (gameState == storeState &&
                        (keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_E)) {
                    chooseShopOption(keyCode);
                } else if (keyCode == KeyEvent.VK_R && gameState == gameOverState) {
                    gameState = pausedState;
                    roundCounter = 0;
                    resetRound();
                } else if (gameState == storeState && keyCode == KeyEvent.VK_B) {
                    createEntities();
                    gameState = playState;
                }
            }
        });
        
        gamePanel = new GamePanel();
        gameWindow.getContentPane().add(gamePanel);
        gamePanel.setBounds(0, 0, gameWindow.getWidth(), gameWindow.getHeight());
        
        gameWindow.setVisible(true);
    }
    
    void createEntities() {
        screenWidth = gamePanel.getWidth();
        screenHeight = gamePanel.getHeight();
        
        String imagePath = "/images/backgrounds/defaultBackground.png";
        try {
            background = ImageIO.read(GameWindow.class.getResource(imagePath));
        } catch (IOException ex) {
            System.out.println("Image not found");
        }

        // bullets stored in an arraylist for deleting them easily
        bullets = new ArrayList<>();
        
        createEnemies(3 + roundCounter, -2 + roundCounter, -4 + roundCounter);
    }
    
    void createInitialEntities() {
        // creating a character
        character = new Character(
                1000, screenWidth / 2, screenHeight / 2, 100, 100);
        character.setImagePath("default.png");
        
        // giving him a weapon
        Bullet basicBullet = new Bullet(100, 10, 10, Color.yellow);
        basicBullet.setImagePath("default.png");
        Weapon startWeapon = new Weapon(Color.BLACK, 50, 20, 40, basicBullet);
        startWeapon.setImagePath("default.png");
        startWeapon.setCooldown(10);
        character.addWeapon(startWeapon);
    }
    
    int[] randomPosition() {
        int[][] possiblePositions = new int[][] {
                new int[] {0, (int) (Math.random() * screenHeight)},
                new int[] {screenWidth, (int) (Math.random() * screenHeight)},
                new int[] {(int) (Math.random() * screenWidth), 0},
                new int[] {(int) (Math.random() * screenWidth), screenHeight}
            };
        return possiblePositions[(int) (Math.random() * 4)];
    }
    
    void spawnWave(int count, String imagePath, int coefficient) {
        for (int i = 0; i < count; i++) {
            int[] position = randomPosition();
            int posX = position[0];
            int posY = position[1];
            Enemy enemy;
            enemy = new Enemy(
                    100 * coefficient,
                    posX,
                    posY,
                    126 * coefficient,
                    100 * coefficient,
                    4.5 - coefficient,
                    20 * coefficient
            );
            enemy.setImagePath(imagePath);
            enemies.add(enemy);
        }
    }
    
    void createEnemies(int smallEnemies, int mediumEnemies, int largeEnemies) {
        enemies = new ArrayList<>();
        
        spawnWave(smallEnemies, "default.png", 1);
        spawnWave(mediumEnemies, "default.png", 2);
        spawnWave(largeEnemies, "default.png", 3);
    }
    
    void spawnBullet() {
        Weapon currentWeapon = character.getCurrentWeapon();
        if (currentWeapon == null || currentWeapon.onCooldown()) {
            return;
        }
        
        // retrieving the position and the angle of the spawned bullet
        double algebraicAngle = character.getRotationAngle();
        
        int weaponEndX = character.getPositionX() + (int) (
                Math.cos(character.getRotationAngle()) * currentWeapon.getWidth());
        int weaponEndY = character.getPositionY() + (int) (
                Math.sin(character.getRotationAngle()) * currentWeapon.getWidth());
        
        // adding it to the bullets
        Bullet bulletType = currentWeapon.getBulletType();
        MovingBullet newBullet = new MovingBullet(bulletType);
        newBullet.setMovingParametres(
                weaponEndX,
                weaponEndY,
                algebraicAngle
        );
        bullets.add(newBullet);
        currentWeapon.shoot();
    }
    
    Rectangle getCharacterBounds() {
        int width = character.getSizeX();
        int height = character.getSizeY();
        int padding = 90;
        int boxWidth = width - padding;
        int boxHeight = height - padding;
        int x = character.getPositionX() - boxWidth / 2;
        int y = character.getPositionY() - boxHeight / 2;
        return new Rectangle(x, y, boxWidth, boxHeight);
    }
    
    Rectangle getEnemyBounds(Enemy enemy) {
        int width = enemy.getSizeX();
        int height = enemy.getSizeY();
        int padding = 50;
        int boxWidth = width - padding;
        int boxHeight = height - padding;
        int x = enemy.getPositionX() - boxWidth / 2;
        int y = enemy.getPositionY() - boxHeight / 2;
        return new Rectangle(x, y, boxWidth, boxHeight);
    }
    
    Rectangle getBulletBounds(MovingBullet bullet) {
        int radius = bullet.getRadius();
        int x = bullet.getPositionX();
        int y = bullet.getPositionY();
        return new Rectangle(x, y, radius * 2, radius * 2);
    }
    
    void checkCollisions() {
        
        Rectangle characterBounds = getCharacterBounds();
        ArrayList<MovingBullet> hitBullets = new ArrayList<>();
        
        for (MovingBullet bullet : bullets) {
            Rectangle bulletBounds = getBulletBounds(bullet);
            
            for (Enemy enemy : enemies) {
                Rectangle enemyBounds = getEnemyBounds(enemy);
                
                if (bulletBounds.intersects(enemyBounds)) {
                    hitBullets.add(bullet);
                    enemy.reduceHealth(bullet.getDamage());
                    break;
                }
            }
        }
        
        for (Enemy enemy : enemies) {
            Rectangle enemyBounds = getEnemyBounds(enemy);
            
            if (characterBounds.intersects(enemyBounds)) {
                character.setHealth(character.getHealth() - 10);
                enemy.setCanMove(false);
                if (character.getHealth() <= 0) {
                    gameState = gameOverState;
                }
            } else {
                enemy.setCanMove(true);
            }
        }
        
        bullets.removeAll(hitBullets);
    }
    
    void resetRound() {
        createEntities();
        createInitialEntities();
    }

    public void run() {
        
        // building the initial stage of the game
        constructWindow();
        resetRound();
        
        // FPS-based timer from the JSwing package
        Timer timer = new Timer(TARGET_TIME, e -> {
            // numerically updating the objects
            update();
            // drawing the calculated objects
            draw();
            // speeding up
            Toolkit.getDefaultToolkit().sync();
        });
        
        timer.start();
    }

}