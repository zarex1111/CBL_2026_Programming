package engine;

import entities.Bullet;
import entities.Character;
import entities.Enemy;
import entities.MovingBullet;
import entities.Upgrade;
import entities.Weapon;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
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
        
        
        // redefining the default paintMethod so it draws our sprites
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
    
    /*moves enemies that aren't collided with the character and removes those,
    that have collided with a bullet*/
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
    
    void executeUpgrade(Upgrade selectedUpgrade) {
        switch (selectedUpgrade.getUpgradeType()) {
            case BULLET -> {
                Bullet currentType = character.getCurrentWeapon().getBulletType();
                currentType.setRadius(
                        (int) (currentType.getRadius()
                                * selectedUpgrade.getPercentage()));
            }
            case WEAPON -> {
                Weapon currentWeapon = character.getCurrentWeapon();
                currentWeapon.setCooldown(
                        (int) (currentWeapon.getDefaultCooldown()
                                * selectedUpgrade.getPercentage()));
            }
            case NEW_WEAPON -> {
                if (selectedUpgrade == Upgrade.PINK_WEAPON) {
                    Bullet pinkBullet = new Bullet(150, 3, 15, Color.pink);
                    pinkBullet.setImagePath("pink.png");
                    Weapon pinkWeapon = new Weapon(
                            Color.pink, 60, 30, 100, pinkBullet);
                    pinkWeapon.setImagePath("pink.png");
                    character.addWeapon(pinkWeapon);
                }
            }
            default -> {
                return;
            }
        }
        character.changeMoney(selectedUpgrade.getPrice() * -1);
        
        startNewRound();
        gameState = playState;
    }
    
    Upgrade firstOption;
    Upgrade secondOption;
    
    Upgrade getShoppingOption() {
        double setProbability = Math.random();
        
        Upgrade possibleOption = Upgrade.values()[
                (int) (Math.random() * Upgrade.values().length)
            ];
        while (possibleOption.getRarityProbability() < setProbability) {
            possibleOption = Upgrade.values()[
                (int) (Math.random() * Upgrade.values().length)
            ];
        }

        return possibleOption;
    }
    
    void drawShoppingOption(Graphics g,
            int topLeftCornerX,
            int topLeftCornerY,
            int boxWidth,
            int boxHeight,
            int stringTopX,
            int stringTopY,
            int imageTopX,
            int imageTopY,
            int imageWidth,
            int imageHeight,
            String key,
            Upgrade upgradeOption) {
        Image optionBackgroundImage = upgradeOption.getBackgroundImage();
        
        g.drawImage(optionBackgroundImage,
                topLeftCornerX, topLeftCornerY, boxWidth, boxHeight, gamePanel);
        
        g.setColor(Color.white);
        int fontSize = (int) (screenHeight * 0.025);
        int upgradePrice = upgradeOption.getPrice();
        g.setFont(g.getFont().deriveFont(Font.BOLD, fontSize));
        g.drawString(key + " " + upgradeOption.getText(),
                stringTopX,
                stringTopY
        );
        g.drawString("Price: " + String.valueOf(upgradePrice),
                stringTopX,
                stringTopY + fontSize);
        
        Image icon;
        switch (upgradeOption.getUpgradeType()) {
            case BULLET -> icon = character.getCurrentWeapon().getBulletType().getImage();
            case WEAPON -> {
                icon = character.getCurrentWeapon().getImage();
                imageTopY += imageHeight / 2;
                imageHeight = (int) (imageHeight * (double) icon.getHeight(gamePanel) 
                        / icon.getWidth(gamePanel));
                imageTopY -= imageHeight / 2;
            }
            default -> {
                Sprite dummy = new Sprite();
                dummy.setDefaultFolder("weapon");
                dummy.setImagePath("pink.png");
                
                icon = dummy.getImage();
                imageTopY += imageHeight / 2;
                imageHeight = (int) (imageHeight * (double) icon.getHeight(gamePanel) 
                        / icon.getWidth(gamePanel));
                imageTopY -= imageHeight / 2;
            }
        }
        g.drawImage(
                icon, imageTopX, imageTopY,
                imageWidth,
                imageHeight,
                gamePanel);
    }
    
    /*detects whether a shop option has been selected and if it is possible to
    go through with it*/
    void chooseShopOption(int keyCode) {
        if (character.getMoney() >= firstOption.getPrice()
                && keyCode == KeyEvent.VK_Q) {
            executeUpgrade(firstOption);
        } else if (character.getMoney() >= secondOption.getPrice()
                && keyCode == KeyEvent.VK_E) {
            executeUpgrade(secondOption);
        }
    }
    
    //displays the shop options
    void createShopOptions(Graphics g) {
        if (firstOption == null && secondOption == null) {
            firstOption = getShoppingOption();
            secondOption = getShoppingOption();
            while (firstOption.getUpgradeType()
                    == secondOption.getUpgradeType()) {
                secondOption = getShoppingOption();
            }
        }
        
        drawShoppingOption(g,
                (int) (screenWidth * 0.2),
                (int) (screenHeight * 0.3),
                (int) (screenWidth * 0.2),
                (int) (screenHeight * 0.4),
                (int) (screenWidth * 0.225),
                (int) (screenHeight * 0.65),
                (int) (screenWidth * 0.225),
                (int) (screenHeight * 0.325),
                (int) (screenWidth * 0.15),
                (int) (screenWidth * 0.15),
                "(Q)",
                firstOption);
        
        drawShoppingOption(g,
                (int) (screenWidth * 0.6),
                (int) (screenHeight * 0.3),
                (int) (screenWidth * 0.2),
                (int) (screenHeight * 0.4),
                (int) (screenWidth * 0.625),
                (int) (screenHeight * 0.65),
                (int) (screenWidth * 0.625),
                (int) (screenHeight * 0.325),
                (int) (screenWidth * 0.15),
                (int) (screenWidth * 0.15),
                "(E)",
                secondOption);        
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
        
        g.setColor(Color.white);
        g.drawString("Zombie Shooter", (screenWidth 
                - g.getFontMetrics().stringWidth("Zombie Shooter")) 
                / 2, screenHeight / 2);
        
        g.setFont(g.getFont().deriveFont(Font.ITALIC, 40F));
        g.drawString("Press Space to start", (screenWidth 
                - g.getFontMetrics().stringWidth("Press Space to start")) / 2, 
                screenHeight / 2 + 100);
    }
    
    void drawPlayScreen(Graphics g) {
        drawBackgroundScreen(g);
        drawBullets(g);
        drawCharacter(g);
        drawEnemies(g);
        drawHealthBar(g);
    }
    
    void drawPausedScreen(Graphics g) {
        drawBackgroundScreen(g);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 25F));
        String text = "Press B to begin round";
        
        g.setColor(Color.black);
        g.drawString(text, 30, 700);
        drawCharacter(g);
        drawHealthBar(g);
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
        textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, screenWidth - 50 - textWidth, 30);
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
    
    void drawHealthBar(Graphics g) {
        int topLeftCornerX = (int) (screenWidth * 0.025);
        int topLeftCornerY = (int) (screenHeight * 0.025);
        int maxWidth = (int) (screenWidth * 0.3);
        int height = (int) (screenHeight * 0.05);
        
        int currentHealth = character.getHealth();
        int maxHealth = character.getMaxHealth();
        
        g.setColor(Color.black);
        g.fillRect(topLeftCornerX - (int) (screenHeight * 0.01),
                topLeftCornerY - (int) (screenHeight * 0.01),
                maxWidth + 2 * (int) (screenHeight * 0.01),
                height + 2 * (int) (screenHeight * 0.01));
        g.setColor(Color.red);
        g.fillRect(topLeftCornerX,
                topLeftCornerY,
                (int) ((double) (currentHealth) * maxWidth / maxHealth),
                height);
        
        g.setColor(Color.white);
        g.setFont(g.getFont().deriveFont(Font.BOLD, height));
        g.drawString(
                String.valueOf(currentHealth) + "/" + String.valueOf(maxHealth),
                topLeftCornerX,
                topLeftCornerY + (int) (screenHeight * 0.036));
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
    
    void startNewRound() {
        firstOption = null;
        secondOption = null;
        
        createEntities();
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
                } else if ((gameState == pausedState || gameState == storeState) 
                        && keyCode == KeyEvent.VK_B) {
                    startNewRound();
                    gameState = playState;
                } else if (gameState == storeState 
                        && (keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_E)) {
                    chooseShopOption(keyCode);
                } else if (keyCode == KeyEvent.VK_R && gameState == gameOverState) {
                    gameState = pausedState;
                    roundCounter = 0;
                    resetRound();
                } else if (gameState == storeState && keyCode == KeyEvent.VK_B) {
                    startNewRound();
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
        Weapon startWeapon = new Weapon(Color.BLACK, 50, 20, 70, basicBullet);
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
    
    /*we return a rectangle that is the hitbox of the character with a padding
    so that it is more accurate*/
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
    
    /*we return a rectangle that is the hitbox of the enemy with a padding
    so that it is more accurate*/
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
    
    /*we return a rectangle that is the hitbox of the bullet with a padding
    so that it is more accurate*/
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
        startNewRound();
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