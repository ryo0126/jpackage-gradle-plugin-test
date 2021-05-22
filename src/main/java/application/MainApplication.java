package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import resource.Resources;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class MainApplication extends Application {

    private static final String TEXT_FILE_PATH_NAME = "/text_file/text_file.txt";

    @Override
    public void start(Stage primaryStage) {
        var button = new Button("Read text_file.txt");
        button.setOnAction(event -> {
            var filePath = Resources.getInstance().pathOf(TEXT_FILE_PATH_NAME);
            var content = getTextFileContent(filePath);
            var dialog = new Alert(
                Alert.AlertType.NONE,
                content,
                ButtonType.CLOSE
            );
            dialog.setTitle(filePath.toString());
            dialog.show();
        });

        var vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(button);

        var scene = new Scene(vBox, 400, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private String getTextFileContent(Path path) {
        var sb = new StringBuilder();
        try {
            return sb
                .append(Files.lines(path).collect(Collectors.joining()))
                .toString();
        } catch (IOException e) {
            return sb.append("エラーが発生しました: ")
                .append("\n")
                .append(e)
                .append("\n")
                .append(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).collect(Collectors.joining("\n")))
                .toString();
        }
    }

    @Override
    public void stop() throws Exception {
        Resources.getInstance().close();
    }
}
