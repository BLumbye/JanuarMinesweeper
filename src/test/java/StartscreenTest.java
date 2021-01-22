import java.io.IOException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sweepers.Router;
import org.sweepers.control.StartscreenController;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;

@ExtendWith(ApplicationExtension.class)
public class StartscreenTest {
    @Start
    void start(Stage stage) throws IOException {
        Scene scene = new Scene(Router.toStartscreen());

        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testSizeButtons(FxRobot robot) throws InterruptedException {
        double percentage = StartscreenController.DIFFICULTIES.get("Easy");

        robot.clickOn("#btnSizeSmall");
        Pair<Integer,Integer> size = StartscreenController.SIZES.get("Small");
        Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo(size.getKey().toString());
        Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo(size.getValue().toString());
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(size.getKey() * size.getValue() * percentage)));

        robot.clickOn("#btnSizeMedium");
        size = StartscreenController.SIZES.get("Medium");
        Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo(size.getKey().toString());
        Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo(size.getValue().toString());
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(size.getKey() * size.getValue() * percentage)));

        robot.clickOn("#btnSizeLarge");
        size = StartscreenController.SIZES.get("Large");
        Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo(size.getKey().toString());
        Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo(size.getValue().toString());
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(size.getKey() * size.getValue() * percentage)));
    }

    @Test
    void testDifficultyButtons(FxRobot robot) {
        Pair<Integer,Integer> size = StartscreenController.SIZES.get("Small");
        int total = size.getKey() * size.getValue();

        robot.clickOn("#btnDifficultyEasy");
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(total * StartscreenController.DIFFICULTIES.get("Easy"))));

        robot.clickOn("#btnDifficultyMedium");
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(total * StartscreenController.DIFFICULTIES.get("Medium"))));

        robot.clickOn("#btnDifficultyHard");
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(total * StartscreenController.DIFFICULTIES.get("Hard"))));
    }

    @Tag("Slow")
    @Test
    void testCustomSize(FxRobot robot) {
        robot.clickOn("#btnSizeCustom");
        
        Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).isDisable()).isFalse();
        Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).isDisable()).isFalse();

        // Check that legal values stay normal and that mines are updated
        double percentage = StartscreenController.DIFFICULTIES.get("Easy");
        int width = StartscreenController.SIZES.get("Small").getKey();
        int height = StartscreenController.SIZES.get("Small").getValue();
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
        for (width = 101; width < 105; width++) {
            robot.clickOn("#fieldWidth").eraseText(5).write(String.valueOf(width)).clickOn("#btnSizeCustom");
            Assertions.assertThat(robot.lookup("#fieldWidth").queryAs(TextField.class).getText()).isEqualTo("100");
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(100 * height * percentage)));
        }
        width = 100;

        for (height = 101; height < 105; height++) {
            robot.clickOn("#fieldHeight").eraseText(5).write(String.valueOf(height)).clickOn("#btnSizeCustom");
            Assertions.assertThat(robot.lookup("#fieldHeight").queryAs(TextField.class).getText()).isEqualTo("100");
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf((int) Math.round(width * 100 * percentage)));
        }
    }

    @Tag("Slow")
    @Test
    void testCustomDifficulty(FxRobot robot) {
        robot.clickOn("#btnDifficultyCustom");
        
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).isDisable()).isFalse();

        // Check that legal values stay normal
        int mines;
        int width = StartscreenController.SIZES.get("Small").getKey();
        int height = StartscreenController.SIZES.get("Small").getValue();
        for (mines = 1; mines <= 5; mines++) {
            robot.clickOn("#fieldMines").eraseText(5).write(String.valueOf(mines)).clickOn("#btnDifficultyCustom");
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf(mines));
        }
        for (mines = width * height - 15; mines <= width * height - 10; mines++) {
            robot.clickOn("#fieldMines").eraseText(5).write(String.valueOf(mines)).clickOn("#btnDifficultyCustom");
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf(mines));
        }

        // Check that 0 gets corrected
        mines = 0;
        robot.clickOn("#fieldMines").eraseText(5).write(String.valueOf(mines)).clickOn("#btnDifficultyCustom");
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf(1));

        // Check that high values get corrected
        for (mines = width * height - 9; mines <= width * height - 5; mines++) {
            robot.clickOn("#fieldMines").eraseText(5).write(String.valueOf(mines)).clickOn("#btnDifficultyCustom");
            Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf(width * height - 10));
        }

        // Check that mines get lowered when max is lowered
        robot.clickOn("#btnSizeMedium");
        width = StartscreenController.SIZES.get("Medium").getKey();
        height = StartscreenController.SIZES.get("Medium").getValue();
        mines = width * height - 10;
        robot.clickOn("#fieldMines").eraseText(5).write(String.valueOf(mines)).clickOn("#btnDifficultyCustom");
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf(mines));
        robot.clickOn("#btnSizeSmall");
        width = StartscreenController.SIZES.get("Small").getKey();
        height = StartscreenController.SIZES.get("Small").getValue();
        mines = width * height - 10;
        Assertions.assertThat(robot.lookup("#fieldMines").queryAs(TextField.class).getText()).isEqualTo(String.valueOf(mines));

    }
}
