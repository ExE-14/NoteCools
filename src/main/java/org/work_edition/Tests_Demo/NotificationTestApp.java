package org.work_edition.Tests_Demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.awt.*;

public class NotificationTestApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button("Show");
        btn.setMinSize(100, 50);
        btn.setOnAction(e -> {
            NotificationDemo.showNotification("Я ЕБАЛ МАТЬ ПИКСА", "Она та еще шлюха...");
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        Scene scene = new Scene(root, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/styles/ForTestStyle.css").toExternalForm());
        primaryStage.setTitle("Test Notification");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}