package entities;

public enum Rarity {
    COMMON(1),
    RARE(0.5),
    EPIC(0.25),
    LEGENDARY(0.1);
    
    private final double probability;
    
    Rarity(double probability) {
        this.probability = probability;
    }
    
    double getProbability() {
        return probability;
    }
}
