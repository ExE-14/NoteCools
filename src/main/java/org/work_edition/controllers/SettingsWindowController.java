package org.work_edition.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javafx.util.Duration;
import org.work_edition.utils.Animations;
import org.work_edition.utils.SceneManager;
import org.work_edition.utils.SceneManagerAware;

import java.io.IOException;
import java.util.Objects;

public class SettingsWindowController implements SceneManagerAware {

    @FXML private Pane pane;
    @FXML private ImageView imageView;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private SceneManager sceneManager;

    @SuppressWarnings("unused")
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    public void prepareForShow() {
        pane.setOpacity(0.0);
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> Animations.playFadeInAnimation(pane));
        pause.play();
    }

    @FXML
    public void initialize() {
        Animations.playFadeInAnimation(pane);

        // Для кнопок потом напишу анимацию, но сейчас просто так, чтобы было что показать

        try {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/settings.png")));
            StringBuilder ErrorMessage = new StringBuilder("SETTINGS WINDOW CONTROLLER | IMAGE ERROR: ");
            ((image.isError() ? new Runnable() {
                @Override
                public void run() {
                    ErrorMessage.append(image.getException() != null ? image.getException().getMessage() : "SETTINGS WINDOW CONTROLLER | UNKNOWN ERROR");
                    System.err.println(ErrorMessage);
                }
            } : new Runnable() {
                @Override
                public void run() {
                    imageView.setImage(image);
                }
            })).run();
        } catch (Exception e) {
            System.err.println("SETTINGS WINDOW CONTROLLER: " + e.getMessage());
            return;
        }

        saveButton.setOnAction(e -> {System.out.println("NO REALIZE");
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> {
                try {
                    sceneManager.switchToMainWindow();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            delay.play();
        });
        cancelButton.setOnAction(e -> {
            try {
                sceneManager.switchToMainWindow();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

}