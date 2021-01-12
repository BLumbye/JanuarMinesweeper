import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.sweepers.models.Mineless;

public class MinelessTest {
  @Test
  public void testConstructor() {
    for (int x = 0; x < 100; x++) {
      for (int y = 0; y < 100; y++) {
        for (int neighbors = 0; neighbors < 10; neighbors++) {
          Mineless mineless = new Mineless(x, y, neighbors);
          assertEquals(x, mineless.getX());
          assertEquals(y, mineless.getY());
          assertEquals(false, mineless.isRevealed());
          assertEquals(neighbors, mineless.getNeighbors());
        }
      }
    }
  }

  @Test
  public void testReveal() {
    Mineless mineless = new Mineless(0, 0, 0);
    assertEquals(false, mineless.isRevealed());
    mineless.reveal();
    assertEquals(true, mineless.isRevealed());
  }
}
