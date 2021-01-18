import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sweepers.control.StartscreenController;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

@ExtendWith(ApplicationExtension.class)
public class StartscreenTest {
    @Start
    public void start(Stage stage) throws IOException {
        StartscreenController startscreenController = new StartscreenController();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Startscreen.fxml"));
        loader.setController(startscreenController);
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testSizeButtons(FxRobot robot) throws InterruptedException {
        double percentage = StartscreenController.DIFFICULTIES.get("EASY");

        robot.clickOn("#btnSizeSmall");
        Pair<Integer,Integer> size = StartscreenController.SIZES.get("SMALL");
        Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo(size.getKey().toString());
        Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo(size.getValue().toString());
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(size.getKey() * size.getValue() * percentage)));

        robot.clickOn("#btnSizeMedium");
        size = StartscreenController.SIZES.get("MEDIUM");
        Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo(size.getKey().toString());
        Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo(size.getValue().toString());
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(size.getKey() * size.getValue() * percentage)));

        robot.clickOn("#btnSizeLarge");
        size = StartscreenController.SIZES.get("LARGE");
        Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo(size.getKey().toString());
        Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo(size.getValue().toString());
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(size.getKey() * size.getValue() * percentage)));
    }

    @Test
    public void testDifficultyButtons(FxRobot robot) {
        Pair<Integer,Integer> size = StartscreenController.SIZES.get("SMALL");
        int total = size.getKey() * size.getValue();

        robot.clickOn("#btnDifficultyEasy");
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(total * StartscreenController.DIFFICULTIES.get("EASY"))));

        robot.clickOn("#btnDifficultyMedium");
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(total * StartscreenController.DIFFICULTIES.get("MEDIUM"))));

        robot.clickOn("#btnDifficultyHard");
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(total * StartscreenController.DIFFICULTIES.get("HARD"))));
    }

    @Tag("Slow")
    @Test
    void testCustomSize(FxRobot robot) {
        robot.clickOn("#btnSizeCustom");
        
        Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).isDisable()).isFalse();
        Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).isDisable()).isFalse();

        // Check that legal values stay normal and that mines are updated
        double percentage = StartscreenController.DIFFICULTIES.get("EASY");
        int width = StartscreenController.SIZES.get("SMALL").getKey();
        int height = StartscreenController.SIZES.get("SMALL").getValue();
        for (width = 4; width <= 10; width++) {
            robot.clickOn("#fieldWidth").eraseText(5).write(String.valueOf(width)).clickOn("#btnSizeCustom");
            Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo(String.valueOf(width));
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(width * height * percentage)));
        }
        for (width = 95; width <= 100; width++) {
            robot.clickOn("#fieldWidth").eraseText(5).write(String.valueOf(width)).clickOn("#btnSizeCustom");
            Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo(String.valueOf(width));
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(width * height * percentage)));
        }
        width = 100;
        
        for (height = 4; height <= 10; height++) {
            robot.clickOn("#fieldHeight").eraseText(5).write(String.valueOf(height)).clickOn("#btnSizeCustom");
            Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo(String.valueOf(height));
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(width * height * percentage)));
        }
        for (height = 95; height <= 100; height++) {
            robot.clickOn("#fieldHeight").eraseText(5).write(String.valueOf(height)).clickOn("#btnSizeCustom");
            Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo(String.valueOf(height));
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(width * height * percentage)));
        }
        height = 100;

        // Check that values below 4 gets corrected
        for (width = 0; width < 4; width++) {
            robot.clickOn("#fieldWidth").eraseText(5).write(String.valueOf(width)).clickOn("#btnSizeCustom");
            Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo("4");
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(4 * height * percentage)));
        }
        width = 4;

        for (height = 0; height < 4; height++) {
            robot.clickOn("#fieldHeight").eraseText(5).write(String.valueOf(height)).clickOn("#btnSizeCustom");
            Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo("4");
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(width * 4 * percentage)));
        }
        height = 4;

        // Check that values above 100 gets corrected
        for (width = 101; width < 110; width++) {
            robot.clickOn("#fieldWidth").eraseText(5).write(String.valueOf(width)).clickOn("#btnSizeCustom");
            Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo("100");
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(100 * height * percentage)));
        }
        width = 100;

        for (height = 101; height < 110; height++) {
            robot.clickOn("#fieldHeight").eraseText(5).write(String.valueOf(height)).clickOn("#btnSizeCustom");
            Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo("100");
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(width * 100 * percentage)));
        }
    }
}
