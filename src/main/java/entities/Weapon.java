package entities;

import engine.Sprite;
import java.awt.Color;


public class Weapon extends Sprite {
    Color color;
    int width;
    int height;
    int ammo;
    Bullet bulletType;
    
    public Weapon(Color color, int sizeX, int sizeY, int ammo, Bullet bulletType) {
        this.color = color;
        this.width = sizeX;
        this.height = sizeY;
        this.ammo = ammo;
        this.bulletType = bulletType;
        
        setDefaultFolder("weapon");
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
    
    public boolean hasAmmo() {
        return (ammo > 0);
    }
    
    public void shoot() {
        ammo -= 1;
    }
}
