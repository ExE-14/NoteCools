package org.work_edition.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class WindowManager {

    /**
     * Создает окно с параметрами, полезен в контроллерах
     *
     * @param fxmlPath
     * @param cssPath
     * @param title
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    public static void openWindow(String fxmlPath, String cssPath, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource(fxmlPath));

        Parent root = loader.load();
        Stage stage = new Stage();

        stage.setTitle(title);
        stage.setScene(new javafx.scene.Scene(root, width, height));

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
     * @param width
     * @param height
     * @return
     * @param <T>
     * @throws IOException
     */
    public static <T> T openWindowWithController(String fxmlPath, String cssPath, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource(fxmlPath));

        Parent root = loader.load();
        Stage stage = new Stage();

        stage.setTitle(title);
        stage.setScene(new Scene(root, width, height));

        if (cssPath != null) {
            stage.getScene().getStylesheets().add(Objects.requireNonNull(WindowManager.class.getResource(cssPath)).toExternalForm());
        }

        stage.setResizable(false);
        stage.show();

        return loader.getController();
    }

    // ВСЕ ЧТО НИЖЕ - ПОЛНОСТЬЮ БЕСПОЛЕЗНО, МОЖНО УДАЛИТЬ, НО Я ПОКА ОСТАВЛЮ, МОЖЕТ ПРИГОДИТСЯ, МАКСИМАЛЬНО ТУПОЙ И БЕСПОЛЕЗНЫЙ ГАВНАКОД КОТОРЫЙ ТУПО СОСЗДАЕТ СЦЕННЫ А ОКНА ДЛЯ НИХ НЕ УКЕЗАЫВАЕЕТ
//    /**
//     * Создает сцену с параметрами, полезен для получения сцены в контроллере и использования ее в окне
//     *
//     * @param fxmlPath
//     * @param cssPath
//     * @param width
//     * @param height
//     * @return
//     * @throws IOException
//     */
//    public static Scene createScene(String fxmlPath, String cssPath, int width, int height) throws IOException {
//        FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource(fxmlPath));
//
//        Parent root = loader.load();
//        javafx.scene.Scene scene = new Scene(root, width, height);
//
//        if (cssPath != null) {
//            scene.getStylesheets().add(Objects.requireNonNull(WindowManager.class.getResource(cssPath)).toExternalForm());
//        }
//
//        return scene;
//    }
//
//    /**
//     * Создает сцену с контроллером, полезен для получения сцены и контроллера в контроллере и использования их в окне
//     *
//     * @param fxmlPath
//     * @param cssPath
//     * @param width
//     * @param height
//     * @return
//     * @param <T>
//     * @throws IOException
//     */
//    public static <T> Pair<Scene, T> createSceneWithController(String fxmlPath, String cssPath, int width, int height) throws IOException {
//        FXMLLoader loader = new FXMLLoader(WindowManager.class.getResource(fxmlPath));
//
//        Parent root = loader.load();
//        javafx.scene.Scene scene = new Scene(root, width, height);
//
//        if (cssPath != null) {
//            scene.getStylesheets().add(Objects.requireNonNull(WindowManager.class.getResource(cssPath)).toExternalForm());
//        }
//
//        return new Pair<>(scene, loader.getController());
//    }

}
