package org.work_edition.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.work_edition.utils.Animations;
import org.work_edition.utils.SceneManager;
import org.work_edition.utils.SceneManagerAware;

import java.io.IOException;

public class MainWindowController implements SceneManagerAware {

    @FXML private Label mainTitleLabel;
    @FXML private Label subtitleLabel;

    @FXML private Button settingsButton;
    @FXML private Button notesButton;
    @FXML private Button helpButton;
    @FXML private Button contactsButton;

    private SceneManager sceneManager;

    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    // инициализатор, автоматом вызоветс (после загрузки FXML)
    @FXML
    public void initialize() {
        Animations.playFadeInAnimation(mainTitleLabel, subtitleLabel);

        Animations.addHoverAnimetion(settingsButton);
        Animations.addHoverAnimetion(notesButton);
        Animations.addHoverAnimetion(helpButton);
        Animations.addHoverAnimetion(contactsButton);
    }

    // обработчики кнопок
    @FXML
    private void handleSettings() throws IOException {
        sceneManager.switchTo("/fxmls/SettingsWindow.fxml", null, "Настройки");
    }

    @FXML
    private void handleNotes() {
        System.out.println("NO REALIZE");
    }

    @FXML
    private void handleHelp() {
        System.out.println("NO REALIZE");
    }

    @FXML
    private void handleContacts() {
        try {
            sceneManager.switchTo("/fxmls/ContactsWindow.fxml", null, "Контакты");
        } catch (Exception ex) {
            System.err.println("MAIN CONTROLLER: ERROR SWITCHING TO CONTACTS WINDOW");
            ex.printStackTrace();

            // от безнадеги
            if (ex.getCause() != null) {
                System.err.println("Cause: " + ex.getCause());
                ex.getCause().printStackTrace();
            } // и от совсем безнадеги
            if (ex.getSuppressed() != null && ex.getSuppressed().length > 0) {
                System.err.println("Suppressed:");
                for (Throwable t : ex.getSuppressed()) {
                    System.err.println("  " + t);
                    t.printStackTrace();
                }
            } // и это все равно не помогло, так что просто покажем алерт, мол, что что-то пошло не так АХАХАХАХХААА
             javafx.scene.control.Alert alert = new Alert(Alert.AlertType.ERROR);
             alert.setTitle("ERROR");
             alert.setHeaderText("Не удалось открыть окно контактов");
             alert.setContentText("Пожалуйста, попробуйте снова позже. (Или никогда)");
             alert.showAndWait();
        }
    }
}