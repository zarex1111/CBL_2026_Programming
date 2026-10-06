
import engine.GameEngine;

public class CBL {

    public static void main(String[] args) {
        System.out.println("Game initiated");
        
        // running a new game engine with private methods
        GameEngine engine = new GameEngine(60);

        engine.run();
    }

}
