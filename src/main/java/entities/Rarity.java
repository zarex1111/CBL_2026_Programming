package entities;

public enum Rarity {
    COMMON(1),
    RARE(0.7),
    EPIC(0.4),
    LEGENDARY(0.1);
    
    private final double probability;
    
    Rarity(double probability) {
        this.probability = probability;
    }
    
    double getProbability() {
        return probability;
    }
}
