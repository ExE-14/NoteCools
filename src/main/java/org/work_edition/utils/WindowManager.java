package org.work_edition.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/// TODO: Нужно сделать настройку "Открывать в новом окне" для каждой кнопки, и если она включена, то открывать окно через WindowManager, а не через SceneManager
/// пока что он не используется
public class WindowManager {

    /**
     * Создает окно с параметрами, полезен в контроллерах
     *
     * @param fxmlPath
     * @param cssPath
     * @param title
     * @return
     * @throws IOException
     *
     * UPD: Удалил параметры для указания размера окна, т.к они не нужны и можно указать размер в FXML
     */
    public static void openWindow(String fxmlPath, String cssPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource(fxmlPath));

        Parent root = loader.load();
        Stage stage = new Stage();

        stage.setTitle(title);
        stage.setScene(new javafx.scene.Scene(root));

        if (cssPath != null) {
            stage.getScene().getStylesheets().add(Objects.requireNonNull(WindowManager.class.getResource(cssPath)).toExternalForm());
        }

        stage.setResizable(false);
        stage.show();
    }

    /**
     * Создает окно с контроллером, можно использовать для получения каких-либо данных из окна
     *
     * @param fxmlPath
     * @param cssPath
     * @param title
     * @return
     * @param <T>
     * @throws IOException
     *
     * UPD: Удалил параметры для указания размера окна, т.к они не нужны и можно указать размер в FXML
     */
    public static <T> T openWindowWithController(String fxmlPath, String cssPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource(fxmlPath));

        Parent root = loader.load();
        Stage stage = new Stage();

        stage.setTitle(title);
        stage.setScene(new Scene(root));

        if (cssPath != null) {
            stage.getScene().getStylesheets().add(Objects.requireNonNull(WindowManager.class.getResource(cssPath)).toExternalForm());
        }

        stage.setResizable(false);
        stage.show();

        return loader.getController();
    }

}
