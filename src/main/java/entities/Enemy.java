package entities;

import engine.Sprite;

public class Enemy extends Sprite {
    
    int healthPoints;
    double rotationAngle;
    int positionX;
    int positionY;
    int sizeX;
    int sizeY;
    double movementSpeed;

    public Enemy(int healthPoints,
            int positionX, int positionY,
            int sizeX, int sizeY,
            double movementSpeed) {
        this.healthPoints = healthPoints;
        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.movementSpeed = movementSpeed;
        
        setDefaultFolder("enemy");
    }
    
    public void setPosition(int x, int y) {
        positionX = x;
        positionY = y;
    }
    
    public int getPositionX() {
        return positionX;
    }
    
    public int getPositionY() {
        return positionY;
    }
    
    public int getSizeX() {
        return sizeX;
    }
    
    public int getSizeY() {
        return sizeY;
    }
    
    public double getRotationAngle() {
        return rotationAngle;
    }
    
    public void setRotationAngle(double angle) {
        rotationAngle = angle;
    }
    
    public void setHealth(int newHealthPoints) {
        healthPoints = newHealthPoints;
    }
    
    public int getHealthPoints() {
        return healthPoints;
    }
    
    public void move() {
        positionX += (int) (Math.cos(rotationAngle) * movementSpeed);
        positionY += (int) (Math.sin(rotationAngle) * movementSpeed);
    }
    
}
