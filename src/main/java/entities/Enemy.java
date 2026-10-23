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
    boolean canMove;
    int money;

    public Enemy(int healthPoints,
            int positionX, int positionY,
            int sizeX, int sizeY,
            double movementSpeed,
            int money) {
        this.healthPoints = healthPoints;
        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.movementSpeed = movementSpeed;
        this.money = money;
        
        setDefaultFolder("enemy");
    }
    
    public int getMonetaryValue() {
        return money;
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
    
    public void reduceHealth(int amount) {
        healthPoints -= amount;
    }
    
    public void setCanMove(boolean value) {
        canMove = value;
    }
    
    public boolean getCanMove() {
        return canMove;
    }
    
}
