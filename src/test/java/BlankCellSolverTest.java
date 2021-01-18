import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.sweepers.models.Cell;
import org.sweepers.models.Level;
import org.sweepers.models.Mine;

public class BlankCellSolverTest {
    @Test
    public void testBlankCellSolver() {
        Level level = new Level(9, 9, 1);
        level.generateTestLevel(createTestLevel());
        for (int i = 0; i < 9; i++) {
            level.flag(i, 6);
        }
        level.onClick(0, 0);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (i == 4 && (j == 4 || j == 5) || j > 5) {
                    assertEquals(false, level.getLevel()[j][i].isRevealed());
                } else {
                    assertEquals(true, level.getLevel()[j][i].isRevealed());
                }
            }
        }
    }

    public Cell[][] createTestLevel() {
        Cell[][] level = new Cell[9][9];
        level[4][4] = new Mine(4, 4);
        return level;
    }
}
