import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.sweepers.models.Level;
import org.sweepers.models.Mine;
import org.sweepers.models.Mineless;

public class LevelTest {
  @Tag("Slow")
  @Test
  public void testValidConstructor() {
    for (int width = 4; width <= 25; width++) {
      for (int height = 4; height <= 25; height++) {
        for (int mines = 1; mines < width * height - 10; mines++) {
          Level level = new Level(height, width, mines);
          level.generateLevel();
          assertEquals(width, level.getWidth());
          assertEquals(height, level.getHeight());
          assertEquals(mines, level.getMines().get());
          assertEquals(mines, Arrays.stream(level.getLevel()).flatMap(Arrays::stream).filter(c -> c instanceof Mine).count());
          assertEquals(width * height - mines, Arrays.stream(level.getLevel()).flatMap(Arrays::stream).filter(c -> c instanceof Mineless).count());
        }
      }
    }

    for (int width = 99; width <= 100; width++) {
      for (int height = 99; height <= 100; height++) {
        for (int mines = 5000; mines < width * height - 10; mines++) {
          Level level = new Level(height, width, mines);
          level.generateLevel();
          assertEquals(width, level.getWidth());
          assertEquals(height, level.getHeight());
          assertEquals(mines, level.getMines().get());
        }
      }
    }
  }

  @Test
  public void testLowWidth() {
    for (int width = 0; width < 4; width++) {
      final int x = width;
      assertThrows(IllegalArgumentException.class, () -> {
        new Level(50, x, 1);
      });
    }
  }

  @Test
  public void testHighWidth() {
    for (int width = 101; width < 105; width++) {
      final int x = width;
      assertThrows(IllegalArgumentException.class, () -> {
        new Level(50, x, 1);
      });
    }
  }

  @Test
  public void testLowHeight() {
    for (int height = 0; height < 4; height++) {
      final int x = height;
      assertThrows(IllegalArgumentException.class, () -> {
        new Level(x, 50, 1);
      });
    }
  }

  @Test
  public void testHighHeight() {
    for (int height = 101; height < 105; height++) {
      final int x = height;
      assertThrows(IllegalArgumentException.class, () -> {
        new Level(x, 50, 1);
      });
    }
  }

  @Test
  public void testLowMines() {
    for (int mines = -5; mines < 1; mines++) {
      final int x = mines;
      assertThrows(IllegalArgumentException.class, () -> {
        new Level(50, 50, x);
      });
    }
  }

  @Test
  public void testHighMines() {
    for (int mines = 4 * 4; mines < 4 * 5; mines++) {
      final int x = mines;
      assertThrows(IllegalArgumentException.class, () -> {
        new Level(4, 4, x);
      });
    }
  }
}
