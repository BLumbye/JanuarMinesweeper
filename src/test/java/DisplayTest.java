import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sweepers.Router;
import org.sweepers.models.Cell;
import org.sweepers.models.Level;
import org.sweepers.models.Mine;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class DisplayTest {
    Level level;

    @Start
    private void start(Stage stage) throws IOException {
        level = new Level(9, 9, 1, null, null);
        level.generateTestLevel(createTestLevel());
        Scene scene = new Scene(Router.toGame(level));
        stage.setScene(scene);
        stage.show();
    }

    public Cell[][] createTestLevel() {
        Cell[][] cells = new Cell[9][9];
        cells[4][4] = new Mine(4, 4);
        return cells;
    }

    @Test
    void testLose(FxRobot robot) {
        robot.clickOn("#canvasFlag");

        Assertions.assertThat(robot.lookup("#txtEnd").queryText().getText()).isEqualTo("You lose!");
        Assertions.assertThat(robot.lookup("#txtEnd").queryText().getStyleClass()).contains("lose");
    }

    @Test
    void testWin(FxRobot robot) {
        Point2D point = robot.point("#canvasFlag").query();
        robot.clickOn(point.getX() + 64, point.getY());

        Assertions.assertThat(robot.lookup("#txtEnd").queryText().getText()).isEqualTo("You win!");
        Assertions.assertThat(robot.lookup("#txtEnd").queryText().getStyleClass()).contains("win");
    }

    @Test
    void testFlagCounter(FxRobot robot) {
        int count = 0;
        for (int i = 0; i < level.getWidth(); i++) {
            for (int j = 0; j < level.getHeight(); j++) {
                final int x = i;
                final int y = j;
                robot.interact(() -> { level.flag(x, y); });
                count++;
                Assertions.assertThat(robot.lookup("#txtFlags").queryText().getText()).isEqualTo(String.valueOf(count));
            }
        }
        for (int i = 0; i < level.getWidth(); i++) {
            for (int j = 0; j < level.getHeight(); j++) {
                final int x = i;
                final int y = j;
                robot.interact(() -> { level.flag(x, y); });
                count--;
                Assertions.assertThat(robot.lookup("#txtFlags").queryText().getText()).isEqualTo(String.valueOf(count));
            }
        }
    }
}
