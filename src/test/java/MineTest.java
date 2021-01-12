import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.sweepers.models.Mine;

public class MineTest {
  @Test
  public void testConstructor() {
    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        Mine mine = new Mine(x, y);
        assertEquals(x, mine.getX());
        assertEquals(y, mine.getY());
        assertEquals(false, mine.isRevealed());
      }
    }
  }

  @Test
  public void testReveal() {
    Mine mine = new Mine(0, 0);
    assertEquals(false, mine.isRevealed());
    mine.reveal();
    assertEquals(true, mine.isRevealed());
  }
}