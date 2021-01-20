import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.sweepers.models.*;

public class FlagTest {
    @Test
    public void testFlagged() {
        int height = 100;
        int width = 100;
        Level level = new Level(height, width, 1, null, null);
        level.generateLevel();
        int count = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level.flag(i, j);
                if (i != 99 || j != 99) {
                    count++;
                    assertEquals(true, level.getLevel()[j][i].isFlagged());
                    assertEquals(count, level.getFlaggedInt());
                } else {
                    assertEquals(false, level.getLevel()[j][i].isFlagged());
                    assertEquals(count, level.getFlaggedInt());
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level.flag(i, j);
                if (i != 99 || j != 99) {
                    count--;
                    assertEquals(false, level.getLevel()[j][i].isFlagged());
                    assertEquals(count, level.getFlaggedInt());
                } else {
                    count++;
                    assertEquals(true, level.getLevel()[j][i].isFlagged());
                    assertEquals(count, level.getFlaggedInt());
                }
            }
        }
    }
    @Test
    public void testFlaggedReveal() {
        int height = 9;
        int width = 9;
        Level level = new Level(height, width, 1, null, null);
        level.generateTestLevel(createTestLevel());
        for (int i = 0; i < width; i++){
            for (int j = 4; j < height; j++){
                level.flag(i, j);
            }
        }        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level.onClick(i, j);
                if (j >= 4) {
                    assertEquals(true, level.getLevel()[j][i].isFlagged());
                    assertEquals(false, level.getLevel()[j][i].isRevealed());
                } else {
                    assertEquals(false, level.getLevel()[j][i].isFlagged());
                    assertEquals(true, level.getLevel()[j][i].isRevealed());
                }
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level.flag(i, j);
                assertEquals(false, level.getLevel()[j][i].isFlagged());
            }
        }
    }
    public Cell[][] createTestLevel() {
        Cell[][] level = new Cell[9][9];
        level[4][4] = new Mine(4, 4);
        return level;
    }
}
