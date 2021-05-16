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

import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        var button = new Button("Read text_file.txt");
        button.setOnAction(event -> {
            var dialog = new Alert(
                Alert.AlertType.NONE,
                getTextFileContent(),
                ButtonType.CLOSE
            );
            dialog.setTitle("Content of text_file.txt");
            dialog.show();
        });
        var vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(button);
        var scene = new Scene(vBox, 400, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private String getTextFileContent() {
        var sb = new StringBuilder();
        try {
            var filePath = Resources.getInstance().uriOf("/text_file/text_file.txt");
            return sb
                .append("File Path: ")
                .append(filePath)
                .append("\n")
                .append(
                    Files.lines(filePath).collect(Collectors.joining())
                )
                .toString();
        } catch (Exception e) {
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
