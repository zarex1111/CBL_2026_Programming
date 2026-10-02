package engine;

import entities.Character;
import java.awt.*;
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
    
    void updateCharacter() {
        character.setHealth(character.getHealth() + 1);
    }

    void update() {
        updateCharacter();
    }
    
    void drawCharacter(Graphics g) {
        
        if (character == null) {
            return;
        }
        
        int topLeftCornerX = character.getPositionX() - character.getSizeX() / 2;
        int topLeftCornerY = character.getPositionY() - character.getSizeY() / 2;
        
        g.setColor(Color.red);
        g.fillRect(
                topLeftCornerX,
                topLeftCornerY,
                character.getSizeX(),
                character.getSizeY());
        g.setColor(Color.white);
        g.drawString(
                String.valueOf(character.getHealth()),
                topLeftCornerX,
                topLeftCornerY + character.getSizeY() / 2);

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
                100, screenWidth / 2, screenHeight / 2, 20, 20);
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
