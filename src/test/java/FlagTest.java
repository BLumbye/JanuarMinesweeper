import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.sweepers.models.Level;

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

    // TODO: test om man kan sætte flag på revealed
}
