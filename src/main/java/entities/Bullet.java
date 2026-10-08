package entities;

import engine.Sprite;
import java.awt.Color;

public class Bullet extends Sprite {
    int damage;
    int radius;
    double speed;
    Color color;
    
    public Bullet(int damage,
            int radius,
            double speed,
            Color color) {
        this.damage = damage;
        this.radius = radius;
        this.speed = speed;
        this.color = color;
        
        setDefaultFolder("bullet");
    }
    
    public int getRadius() {
        return radius;
    }
    
    public Color getColor() {
        return color;
    }
    
}
