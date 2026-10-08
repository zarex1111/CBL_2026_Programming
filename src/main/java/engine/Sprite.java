package engine;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.IOException;
import javax.imageio.ImageIO;
import windows.GameWindow;


public class Sprite {
    Image spriteImage;
    
    String imageName;
    String defaultFolder;
    
    public void setImagePath(String s) {
        imageName = s;
        String imagePath = getImagePath();
        try {
            spriteImage = ImageIO.read(
                    GameWindow.class.getResource(imagePath));
        } catch (IOException ex) {
            System.out.println("Not found the image " + imagePath);
        }
    }
    
    public String getImageName() {
        return imageName;
    }
    
    public void setDefaultFolder(String folder) {
        defaultFolder = folder;
    }
    
    public String getDefaultFolder() {
        return defaultFolder;
    }
    
    public String getImagePath(String folder) {
        if (imageName.equals("")) {
            return null;
        }
        return "/images/" + folder + "/" + imageName;
    }
    
    public String getImagePath() {
        if (defaultFolder.equals("")) {
            return null;
        }
        return getImagePath(defaultFolder);
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
