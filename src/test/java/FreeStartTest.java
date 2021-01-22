import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.sweepers.models.*;

import javafx.util.Pair;


public class FreeStartTest {

    @Test
    public void testFreeStart(){
        int height = 9;
        int width = 9;
        Level level = new Level(height, width, 70, null, null);
        
        Pair<Integer, Integer> startPosition = new Pair<Integer, Integer>(4, 4);
        level.generateLevel(startPosition);
        
        level.onClick(4, 4);
    
        for (int i = Math.max(startPosition.getValue() - 1, 0); i <= Math.min(startPosition.getValue() + 1,
        height - 1); i++) {
            for (int j = Math.max(startPosition.getKey() - 1, 0); j <= Math.min(startPosition.getKey() + 1,
            width - 1); j++) {
                assertEquals(true, level.getLevel()[i][j].isRevealed());
            }
        }
    }
}
