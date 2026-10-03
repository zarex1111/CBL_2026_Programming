package entities;

import java.awt.Color;


public class Weapon {
    Color color;
    int width;
    int height;
    int ammo;
    
    public Weapon(Color color, int sizeX, int sizeY, int ammo) {
        this.color = color;
        this.width = sizeX;
        this.height = sizeY;
        this.ammo = ammo;
    }
    
    public int getAmmo() {
        return ammo;
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
