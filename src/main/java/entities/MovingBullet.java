package entities;

import java.awt.Color;

public class MovingBullet extends Bullet {
    
    int positionX;
    int positionY;
    double angle;

    public MovingBullet(Bullet bulletType) {
        super(bulletType.damage, bulletType.radius, bulletType.speed, bulletType.color);
    }
    
    public void setMovingParametres(int positionX,
            int positionY,
            double angle) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.angle = angle;
    }
    
    public void move() {
        int diffX = (int) (speed * Math.sin(angle));
        int diffY = (int) (speed * Math.cos(angle));
        
        positionX += diffX;
        positionY += diffY;
    }
    
    public int getPositionX() {
        return positionX;
    }
    
    public int getPositionY() {
        return positionY;
    }
    
    public boolean isOutOfBounds(int right, int bottom) {
        return (positionX < 0 
                || positionX >= right 
                || positionY < 0 
                || positionY >= bottom);
    }
}
