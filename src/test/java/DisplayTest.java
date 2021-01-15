import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sweepers.control.GameController;
import org.sweepers.models.Cell;
import org.sweepers.models.Level;
import org.sweepers.models.Mine;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.robot.Motion;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class DisplayTest {
    @Start
    private void start(Stage stage) throws IOException {
        Level level = new Level(9, 9, 1);
        level.generateTestLevel(createTestLevel());
        GameController gameController = new GameController(level);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameView.fxml"));
        loader.setController(gameController);
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    public Cell[][] createTestLevel() {
        Cell[][] level = new Cell[9][9];
        level[4][4] = new Mine(4, 4);
        return level;
    }

    @Test
    void testLose(FxRobot robot) throws InterruptedException {
        robot.clickOn("#canvasFlag");

        Assertions.assertThat(robot.lookup("#txtEnd").queryText().getText()).isEqualTo("You lose!");
        Assertions.assertThat(robot.lookup("#txtEnd").queryText().getStyleClass()).contains("lose");
    }

    @Test
    void testWin(FxRobot robot) throws InterruptedException {
        Point2D point = robot.point("#canvasFlag").query();
        robot.clickOn(point.getX() + 64, point.getY());

        Assertions.assertThat(robot.lookup("#txtEnd").queryText().getText()).isEqualTo("You win!");
        Assertions.assertThat(robot.lookup("#txtEnd").queryText().getStyleClass()).contains("win");
    }
}
