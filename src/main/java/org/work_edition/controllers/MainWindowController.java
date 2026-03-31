package org.work_edition.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import org.work_edition.utils.Animations;
import java.io.IOException;
import static org.work_edition.utils.WindowManager.openWindow;
public class MainWindowController {

    @FXML private Label mainTitleLabel;
    @FXML private Label subtitleLabel;

    @FXML private Button settingsButton;
    @FXML private Button notesButton;
    @FXML private Button helpButton;
    @FXML private Button contactsButton;

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
        openWindow("/fxmls/SettingsWindow.fxml", "/styles/SettingsWindowStyle.css", "Настройки", 600, 400);
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
        System.out.println("NO REALIZE");
    }
}