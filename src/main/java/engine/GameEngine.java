package engine;

import entities.Bullet;
import entities.Character;
import entities.Enemy;
import entities.MovingBullet;
import entities.Weapon;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import javax.swing.*;
import windows.GameWindow;

public class GameEngine {
    
    Character character;
    ArrayList<MovingBullet> bullets;
    ArrayList<Enemy> enemies;
    
    GameWindow gameWindow;
    GamePanel gamePanel;
    
    static int FRAMES_PER_SECOND;
    static int TARGET_TIME;
    
    public GameEngine(int fps) {
        FRAMES_PER_SECOND = fps;
        TARGET_TIME = (int) (1000.0 / FRAMES_PER_SECOND);
    }
    
    class GamePanel extends JPanel {
        // redefining the default paintMathod so it draws our sprites
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBullets(g);
            drawCharacter(g);
            drawEnemies(g);
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
    
    void updateCharacter() {
        updateCharacterRotationAngle();
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
        for (Enemy iterEnemy : enemies) {
            updateEnemyRotationAngle(iterEnemy);
            iterEnemy.move();
        }
    }

    void update() {
        updateCharacter();
        updateBullets();
        updateEnemies();
    }
    
    void drawCharacterSprite(Graphics2D g2d) {
        int topLeftCornerX = character.getPositionX() - character.getSizeX() / 2;
        int topLeftCornerY = character.getPositionY() - character.getSizeY() / 2;
        
        String imagePath = character.getImagePath();
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
            String imagePath = currentWeapon.getImagePath();
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
        
        String imagePath = enemy.getImagePath();
        
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
        if (bullets == null) {
            return;
        }
        for (MovingBullet iterBullet : bullets) {
            String imagePath = iterBullet.getImagePath();
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
        gameWindow.setSize(500, 500);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setTitle("GAME");
        
        // spawning bullets on pressing SPACE
        gameWindow.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_SPACE) {
                    spawnBullet();
                }
            }
        });
        
        gamePanel = new GamePanel();
        gameWindow.getContentPane().add(gamePanel);
        gamePanel.setBounds(0, 0, gameWindow.getWidth(), gameWindow.getHeight());
        
        gameWindow.setVisible(true);
    }
    
    void createEntities() {
        int screenWidth = gamePanel.getWidth();
        int screenHeight = gamePanel.getHeight();
        
        // creating a character
        character = new Character(
                100, screenWidth / 2, screenHeight / 2, 100, 100);
        character.setImagePath("default.png");
        
        // giving him a weapon
        Bullet basicBullet = new Bullet(10, 10, 10, Color.yellow);
        basicBullet.setImagePath("default.png");
        Weapon startWeapon = new Weapon(Color.BLACK, 50, 20, 40, basicBullet);
        startWeapon.setImagePath("default.png");
        character.addWeapon(startWeapon);
        
        // bullets stored in an arraylist for deleting them easily
        bullets = new ArrayList<>();
        
        enemies = new ArrayList<>();
        
        // spawning n enemies
        int n = 3;
        
        for (int i = 0; i < n; i++) {
            Enemy enemy = new Enemy(100, (i + 1) * 100, 10, 100, 100, 2);
            enemies.add(enemy);
            enemies.get(i).setImagePath("default.png");
        }
    }
    
    void spawnBullet() {
        Weapon currentWeapon = character.getCurrentWeapon();
        if (currentWeapon == null) {
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
    }

    public void run() {
        
        // building the initial stage of the game
        constructWindow();
        createEntities();
        
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
