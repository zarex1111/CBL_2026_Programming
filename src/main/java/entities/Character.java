package entities;

import java.util.ArrayList;

public class Character {

    int health;
    double rotationAngle;
    int positionX;
    int positionY;
    int sizeX;
    int sizeY;
    ArrayList<Weapon> inventory;
    Weapon currentWeapon;

    public Character(
            int health, int positionX, int positionY, int sizeX, int sizeY) {
        this.health = health;
        this.positionX = positionX;
        this.positionY = positionY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        inventory = new ArrayList<>();
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
    
    public double getRotationAngle() {
        return rotationAngle;
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
    
    public void addWeapon(Weapon w) {
        inventory.add(w);
        currentWeapon = w;
    }
    
    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

}
