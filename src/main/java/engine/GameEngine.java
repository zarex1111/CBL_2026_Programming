package engine;

import entities.Character;
import java.awt.Color;
import javax.swing.*;
import windows.GameWindow;

public class GameEngine {
    
    Character character;
    
    JFrame gameWindow;
    
    int FILES_PER_SECOND;
    
    public GameEngine(int fps) {
        FILES_PER_SECOND = fps;
    }
    
    void update() {
    }
    
    void drawCharacter() {
        JLabel characterSprite = new JLabel();
        characterSprite.setSize(character.getSizeX(), character.getSizeY());
        
        characterSprite.setBackground(Color.red);
        
        int topLeftCornerX = character.getPositionX() - character.getSizeX() / 2;
        int topLeftCornerY = character.getPositionY() - character.getSizeY() / 2;
        
        gameWindow.add(characterSprite);
        characterSprite.setBounds(
                topLeftCornerX,
                topLeftCornerY,
                character.getSizeX(),
                character.getSizeY());
    }
    
    void draw() {
        drawCharacter();
    }

    public void run() {

        gameWindow = new GameWindow();
        gameWindow.setVisible(true);
        int screenWidth = gameWindow.getWidth();
        int screenHeight = gameWindow.getHeight();
        
        character = new Character(
                100, screenWidth / 2, screenHeight / 2, 20, 20);
        
        double targetTime = 1 / FILES_PER_SECOND;
        while (true) {
            update();
            draw();
        }
    }

}
