package entities;

public class Character {

    int health;
    double rotationAngle;
    int positionX;
    int positionY;
    int sizeX;
    int sizeY;

    public Character(
            int health, int positionX, int positionY, int sizeX, int sizeY) {
        this.health = health;
        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
    
    public void setPosition(int x, int y) {
        positionX = x;
        positionY = y;
    }
    
    public int getPositionX() {
        return positionX;
    }
    
    public int getPositionY() {
        return positionX;
    }
    
    public int getSizeX() {
        return sizeX;
    }
    
    public int getSizeY() {
        return sizeY;
    }
    
    public void setRotationAngle(double angle) {
        rotationAngle = angle;
    }
    
    public void setHealth(int newHealth) {
        health = newHealth;
    }
    
    public int getHealth() {
        return health;
    }

}
