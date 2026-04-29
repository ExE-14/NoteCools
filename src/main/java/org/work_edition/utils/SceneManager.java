package org.work_edition.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.work_edition.controllers.SettingsWindowController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SceneManager {
    private Stage primaryStage;
    private Scene currentScene;
    private String currentFxmlPath;
    private String previousFxmlPath;
    private String currentCssPath;
    private Map<String, CachedItem> cache = new HashMap<>(); // содержит Parent и Controller для каждого FXML пути

    private static class CachedItem {
        private final Parent root;
        private final Object controller;

        public CachedItem(Parent root, Object controller) {
            this.root = root;
            this.controller = controller;
        } public Parent getRoot() {
            return root;
        } public Object getController() {
            return controller;
        }
    }

    public SceneManager(Stage stage) {
        this.primaryStage = stage;
        this.currentScene = stage.getScene();
    }

    /**
     * Switch current scene to new scene, and update scc whet this needed.
     *
     * @param fxmlPath путь к FXML файлу
     * @param cssPath  путь к CSS файлу (может быть null)
     * @param title    заголовок окна
     */
    public void switchTo(String fxmlPath, String cssPath, String title) throws IOException {
        CachedItem item = cache.computeIfAbsent(fxmlPath, path -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();
                Object controller = loader.getController();
                // init SceneManager in controller if it has method setSceneManager
                if (controller instanceof SceneManagerAware) {
                    ((SceneManagerAware) controller).setSceneManager(this);
                }

                return new CachedItem(root, controller);
            } catch (Exception e) {
                System.out.println("FXML ERROR: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        });

        if (item == null || item.getRoot() == null) {
            throw new IOException("FXML FAILED | " + fxmlPath);
        }

        if (currentFxmlPath != null) {
            previousFxmlPath = currentFxmlPath;
        }
        currentFxmlPath = fxmlPath;
        currentCssPath = cssPath;

        Parent root = item.getRoot();

        if (currentScene == null) {
            Scene nScene = new Scene(root);
            if (cssPath != null) nScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(cssPath)).toExternalForm());

            primaryStage.setScene(nScene);
            currentScene = nScene;
        } else {
            currentScene.setRoot(root);
            currentScene.getStylesheets().clear();
            if (cssPath != null) {
                currentScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(cssPath)).toExternalForm());
            }
        }
        primaryStage.setTitle(title);
    }

    /**
     * Returns controller of FXML file, can be used for getting some data from window
     */
    public <T> T getController(String fxmlPath) throws IOException {
        CachedItem item = cache.get(fxmlPath);
        if (item != null) {
            return (T) item.getController();
        }
        // if cache don't loaded - load it! (although switchTo should already be loaded)
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        loader.load();
        Object controller = loader.getController();
        if (controller instanceof SceneManagerAware) {
            ((SceneManagerAware) controller).setSceneManager(this);
        }
        cache.put(fxmlPath, new CachedItem(loader.getRoot(), controller));
        return (T) controller;
    }

    /**
     * Back to main window (MainWindow.fxml)
     */
    public void switchToMainWindow() throws IOException {
        SettingsWindowController controller = getController("/fxmls/SettingsWindow.fxml");
        controller.prepareForShow();

        switchTo("/fxmls/MainWindow.fxml", null, "Заметки с приоритетом");
    }

    /**
     * Back to previous window
     */
    public void switchToPrevWindow() throws IOException {
        if (previousFxmlPath != null) {
            switchTo(previousFxmlPath, currentCssPath, "Previous Window");
        }
    }
}
