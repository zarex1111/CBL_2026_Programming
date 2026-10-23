package entities;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import windows.GameWindow;

public enum Upgrade {
    INCREASE_BULLETS_SIZE_COMMON(
            1.05, "SIZE +5%", Rarity.COMMON, UpgradeType.BULLET),
    INCREASE_BULLETS_SIZE_RARE(
            1.2, "SIZE +20%", Rarity.RARE, UpgradeType.BULLET),
    INCREASE_BULLETS_SIZE_MYTHIC(
            1.7, "SIZE +70%", Rarity.EPIC, UpgradeType.BULLET),
    INCREASE_BULLETS_SIZE_LEGENDARY(
            3.0, "SIZE +200%", Rarity.LEGENDARY, UpgradeType.BULLET),
    DECREASE_WEAPON_CD_COMMON(
            0.95, "COOLDOWN -5%", Rarity.COMMON, UpgradeType.WEAPON),
    DECREASE_WEAPON_CD_RARE(
            0.9, "COOLDOWN -10%", Rarity.RARE, UpgradeType.WEAPON),
    DECREASE_WEAPON_CD_MYTHIC(
            0.7, "COOLDOWN -30%", Rarity.EPIC, UpgradeType.WEAPON),
    DECREASE_WEAPON_CD_LEGENDARY(
            0.2, "COOLDOWN -80%", Rarity.LEGENDARY, UpgradeType.WEAPON),
    PINK_WEAPON(
            0.0, "NEW WEAPON!", Rarity.RARE, UpgradeType.NEW_WEAPON);

    private final double percentage;
    private final String text;
    private final Rarity rarity;
    private final UpgradeType upgradeType;
    private final int price;
    
    private Image optionBackgroundImage;
    
    Upgrade(double percentage, String text, Rarity rarity, UpgradeType upgradeType) {
        this.percentage = percentage;
        this.text = text;
        this.rarity = rarity;
        this.upgradeType = upgradeType;
        
        setBackgroundImage(rarity);
        
        this.price = (int) (1.0 / rarity.getProbability()) * 50;
    }
    
    void setBackgroundImage(Rarity rarity) {
        String path = "/images/backgrounds/";
        switch (rarity) {
            case COMMON -> path += "commonOptionBackground.png";
            case RARE -> path += "rareOptionBackground.png";
            case EPIC -> path += "epicOptionBackground.png";
            case LEGENDARY -> path += "legendaryOptionBackground.png";
            default -> path += "optionBackground.png";
        }
        
        try {
            optionBackgroundImage = ImageIO.read(
                    GameWindow.class.getResource(path));
        } catch (IOException ex) {
            System.out.println("NO IMAGE FOR SHOPPING OPTIONS FOUND");
        }
    }
    
    public int getPrice() {
        return price;
    }
    
    public Image getBackgroundImage() {
        return optionBackgroundImage;
    }
    
    public double getRarityProbability() {
        return rarity.getProbability();
    }
    
    public UpgradeType getUpgradeType() {
        return upgradeType;
    }
    
    public double getPercentage() {
        return percentage;
    }
    
    public String getText() {
        return text;
    }
    
    public Rarity getRarity() {
        return rarity;
    }
}
