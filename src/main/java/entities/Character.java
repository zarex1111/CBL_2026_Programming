package entities;

public class Character {

    int health;
    double rotationAngle;
    int positionX;
    int positionY;

    public Character(int health, int positionX, int positionY) {
        this.health = health;
        this.positionX = positionX;
        this.positionY = positionY;
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
    
    public void setRotationAngle(double angle) {
        rotationAngle = angle;
    }
    
    public void setHealth(int newHealth) {
        health = newHealth;
    }

}
