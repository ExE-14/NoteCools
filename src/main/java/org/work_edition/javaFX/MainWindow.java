package org.work_edition.javaFX;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class MainWindow extends Application {
    public static class Main {
        public static void main(String[] args) {
            launch(args);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/MainWindow.fxml")); // загружаем FXML из ресурсов
        Parent root = loader.load();
        Scene scene = new Scene(root, 860, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/MainWindowStyle.css")).toExternalForm()); // для сисисэса
        primaryStage.setTitle("Заметки с приоритетом");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("/images/icon.jpg")).toExternalForm()));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}