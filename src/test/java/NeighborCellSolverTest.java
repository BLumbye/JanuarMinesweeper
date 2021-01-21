import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.sweepers.models.*;

public class NeighborCellSolverTest {
    
    @Test
    public void testNeighborCellSolver(){
        int height = 9;
        int width = 9;
        Level level = new Level(height, width, 1, null, null);
        level.generateTestLevel(createTestLevel());
        level.flag(4, 4);
        level.onClick(3, 3);
        level.onClick(3, 3);
        for (int i = 2; i < 7; i++) {
            for (int j = 2; j < 7; j++) {
                if (i == 4 && j == 4) {
                    assertEquals(true, level.getLevel()[j][i].isFlagged());
                    assertEquals(false, level.getLevel()[j][i].isRevealed());
                } else {
                    assertEquals(true, level.getLevel()[j][i].isRevealed());
                }
            }
        }
    }

    @Test
    public void testNeighborCellSolverLost(){
        int height = 9;
        int width = 9;
        Level level = new Level(height, width, 1, null, null);
        level.generateTestLevel(createTestLevel());
        level.flag(4, 3);
        level.onClick(3, 3);
        assertEquals(true, level.onClick(3, 3));
    }


    
    public Cell[][] createTestLevel() {
        Cell[][] level = new Cell[9][9];
        level[4][4] = new Mine(4, 4);
        return level;
    }
}