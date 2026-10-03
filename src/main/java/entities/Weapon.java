package entities;

import java.awt.Color;


public class Weapon {
    Color color;
    int width;
    int height;
    
    public Weapon(Color color, int sizeX, int sizeY) {
        this.color = color;
        this.width = sizeX;
        this.height = sizeY;
    }
    
    public Color getColor() {
        return color;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
}
