package entities;

import java.awt.Color;


public class Bullet {
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
    }
    
    public int getRadius() {
        return radius;
    }
    
    public Color getColor() {
        return color;
    }
    
}
