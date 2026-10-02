package engine;

import javax.swing.*;

import entities.Character;
import windows.GameWindow;

public class GameEngine {
    
    Character character;

    public void run() {

        JFrame gameWindow = new GameWindow();
        gameWindow.setVisible(true);
        int screenWidth = gameWindow.getWidth();
        int screenHeight = gameWindow.getHeight();
        
        character = new Character(
                100, screenWidth / 2, screenHeight / 2);
    }

}
