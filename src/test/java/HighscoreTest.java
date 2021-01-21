import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sweepers.Highscores;
import org.sweepers.models.Highscore;

public class HighscoreTest {
    @Test
    public void testGetSetSave() throws FileNotFoundException {
        String file1 = "test1";
        String file2 = "test2";

        Highscores hs = new Highscores();

        // Copy current scores
        List<Highscore> list1 = new ArrayList<>(hs.getHighscore(file1));
        List<Highscore> list2 = new ArrayList<>(hs.getHighscore(file2));

        // Add 5 random values
        Random rand = new Random();
        for (int i = 0; i < 5; i++) {
            Highscore score1 = new Highscore(String.valueOf(rand.nextInt(9999)), rand.nextInt(100000));
            Highscore score2 = new Highscore(String.valueOf(rand.nextInt(9999)), rand.nextInt(100000));
            hs.setHighscore(file1, score1);
            hs.setHighscore(file2, score2);

            list1.add(score1);
            list1.sort((e1, e2) -> (int) (e1.time - e2.time));
            if (list1.size() > 5) list1.remove(5);

            list2.add(score2);
            list2.sort((e1, e2) -> (int) (e1.time - e2.time));
            if (list2.size() > 5) list2.remove(5);
        }

        // Check that list updated correctly
        Assertions.assertThat(list1).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(hs.getHighscore(file1));
        Assertions.assertThat(list2).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(hs.getHighscore(file2));

        // Check that save and load works
        hs.saveHighscores();
        hs = new Highscores(); // Clears saved scores
        Assertions.assertThat(list1).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(hs.getHighscore(file1));
        Assertions.assertThat(list2).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(hs.getHighscore(file2));
    }
}
