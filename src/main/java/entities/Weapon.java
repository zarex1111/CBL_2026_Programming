package entities;

import java.awt.Color;


public class Weapon {
    Color color;
    int width;
    int height;
    int ammo;
    Bullet bulletType;
    String imageName;
    
    public Weapon(Color color, int sizeX, int sizeY, int ammo, Bullet bulletType) {
        this.color = color;
        this.width = sizeX;
        this.height = sizeY;
        this.ammo = ammo;
        this.bulletType = bulletType;
    }
    
    public void setImagePath(String s) {
        imageName = s;
    }
    
    public String getImagePath() {
        if (imageName == "") {
            return null;
        }
        return "/images/weapon/" + imageName;
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
    
    public Bullet getBulletType() {
        return bulletType;
    }
}
