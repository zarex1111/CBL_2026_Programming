package engine;

import entities.Character;
import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import windows.GameWindow;

public class GameEngine {
    
    Character character;
    
    GameWindow gameWindow;
    GamePanel gamePanel;
    
    static int FRAMES_PER_SECOND;
    static int TARGET_TIME;
    
    public GameEngine(int fps) {
        FRAMES_PER_SECOND = fps;
        TARGET_TIME = (int) (1000.0 / FRAMES_PER_SECOND);
    }
    
    class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawCharacter(g);
        }
    }
    
    void updateCharacterRotationAngle() {
        Point mouseOnScreen = MouseInfo.getPointerInfo().getLocation();
        Point panelOnScreen = gamePanel.getLocationOnScreen();
        Point mousePosition = new Point(
                mouseOnScreen.x - panelOnScreen.x,
                mouseOnScreen.y - panelOnScreen.y);
        
        int diffX = mousePosition.x - character.getPositionX();
        int diffY = mousePosition.y - character.getPositionY();
        double newAngle = Math.atan2(diffY, diffX);
        character.setRotationAngle(newAngle);
    }
    
    void updateCharacter() {
        updateCharacterRotationAngle();
    }

    void update() {
        updateCharacter();
    }
    
    void drawCharacter(Graphics g) {
        
        Graphics2D g2d = (Graphics2D) g;
        
        if (character == null) {
            return;
        }
        
        int topLeftCornerX = character.getPositionX() - character.getSizeX() / 2;
        int topLeftCornerY = character.getPositionY() - character.getSizeY() / 2;
        
        AffineTransform initialState = g2d.getTransform();
        AffineTransform newState = new AffineTransform();
        newState.rotate(character.getRotationAngle(),
                character.getPositionX(),
                character.getPositionY());
        g2d.transform(newState);
        
        g2d.setColor(Color.red);
        g2d.fillRect(
                topLeftCornerX,
                topLeftCornerY,
                character.getSizeX(),
                character.getSizeY());
        
        g2d.setColor(Color.white);
        g2d.drawString(
                String.valueOf(character.getHealth()),
                topLeftCornerX + character.getSizeX() / 2,
                topLeftCornerY + character.getSizeY() / 2);
        
        g2d.setTransform(initialState);

    }
    
    void draw() {
        gameWindow.repaint();
    }
    
    void constructWindow() {
        gameWindow = new GameWindow();
        gameWindow.setSize(500, 500);
        gameWindow.setTitle("GAME");
        
        gamePanel = new GamePanel();
        gameWindow.getContentPane().add(gamePanel);
        gamePanel.setBounds(0, 0, gameWindow.getWidth(), gameWindow.getHeight());
        
        gameWindow.setVisible(true);
    }
    
    void createEntities() {
        int screenWidth = gamePanel.getWidth();
        int screenHeight = gamePanel.getHeight();
        
        character = new Character(
                100, screenWidth / 2, screenHeight / 2, 50, 50);
    }

    public void run() {
        
        constructWindow();
        createEntities();

        Timer timer = new Timer(TARGET_TIME, e -> {
            update();
            draw();
            Toolkit.getDefaultToolkit().sync();
        });
        
        timer.start();
    }

}
