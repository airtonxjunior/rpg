package Rpg;
import java.util.Random;
public class Dado {
    private static final Random random = new Random();
    
    
    public static int rolar(int lado){
        if (lado <= 0) {
            return 1;
        }
        return random.nextInt(lado) + 1;
    }
    
}
