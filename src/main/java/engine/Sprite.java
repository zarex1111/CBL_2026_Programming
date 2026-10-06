package engine;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.imageio.ImageIO;
import windows.GameWindow;


public class Sprite {
    Image spriteImage;

    public Sprite(String imagePath) {
        try {
            spriteImage = ImageIO.read(
                    GameWindow.class.getResource(imagePath));
        } catch (IOException ex) {
            System.out.println("Not found the image");
        }
    }
    
    public void draw(Graphics2D g2d,
            int topLeftCornerX,
            int topLeftCornerY,
            int width,
            int height,
            ImageObserver gamePanel) {
        g2d.drawImage(spriteImage,
                topLeftCornerX,
                topLeftCornerY,
                width,
                height,
                gamePanel);
    }
}
