package org.work_edition.Tests_Demo;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Улучшенная демонстрация WebView в JavaFX.
 * Полноценный мини-браузер с закладками, историей и скрываемой панелью.
 * FXML встроен прямо в код для портативности.
 */
public class WebViewDemo extends Application {

    // --- FXML В ОДНОМ ФАЙЛЕ (через Text Blocks) ---
    private static final String FXML_LAYOUT = """
            <?xml version="1.0" encoding="UTF-8"?>
            <?import javafx.scene.control.*?>
            <?import javafx.scene.layout.*?>
            <?import javafx.scene.web.*?>
            <BorderPane xmlns:fx="http://javafx.com/fxml" styleClass="root-pane">
                <top>
                    <VBox>
                        <ToolBar styleClass="main-toolbar">
                            <Button fx:id="btnBack" text="◀" styleClass="nav-button" disable="true" />
                            <Button fx:id="btnForward" text="▶" styleClass="nav-button" disable="true" />
                            <Button fx:id="btnRefresh" text="↻" styleClass="nav-button" />
                            <TextField fx:id="urlField" HBox.hgrow="ALWAYS" promptText="Введите URL или поисковый запрос..." />
                            <Button fx:id="btnGo" text="🌐 Перейти" />
                            <Button fx:id="btnHtml" text="📄 Тест HTML" />
                            <Separator orientation="VERTICAL" />
                            <Button fx:id="btnAddBookmark" text="⭐ В закладки" />
                            <Button fx:id="btnTogglePanel" text="👁 Панель" />
                        </ToolBar>
                        <ProgressBar fx:id="progressBar" progress="0.0" maxWidth="Infinity" prefHeight="4" styleClass="browser-progress"/>
                    </VBox>
                </top>
                <center>
                    <WebView fx:id="webView" />
                </center>
                <right>
                    <VBox fx:id="rightPanel" spacing="10" styleClass="right-panel">
                        <Label text="📌 Заметки и Закладки" styleClass="header-label"/>
                        <ListView fx:id="bookmarksList" VBox.vgrow="ALWAYS" />
                        <Button fx:id="btnOpenBookmark" text="🔗 Открыть выбранное" maxWidth="Infinity"/>
                    </VBox>
                </right>
            </BorderPane>
            """;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        // Создаем и привязываем контроллер вручную
        BrowserController controller = new BrowserController(primaryStage);
        loader.setController(controller);

        // Загружаем FXML из строки
        BorderPane root = loader.load(new ByteArrayInputStream(FXML_LAYOUT.getBytes(StandardCharsets.UTF_8)));

        Scene scene = new Scene(root, 1200, 750);
        // ПОДКЛЮЧАЕМ CSS ИЗ ОТДЕЛЬНОГО ФАЙЛА
        scene.getStylesheets().add(getClass().getResource("/styles/ForTestStyle.css").toExternalForm());

        primaryStage.setTitle("Браузер на JavaFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // --- ВНУТРЕННИЙ КОНТРОЛЛЕР С ЛОГИКОЙ ---
    public static class BrowserController {
        private final Stage stage;
        private WebEngine webEngine;

        @FXML private WebView webView;
        @FXML private TextField urlField;
        @FXML private Button btnBack, btnForward, btnRefresh, btnGo, btnHtml, btnAddBookmark, btnTogglePanel, btnOpenBookmark;
        @FXML private ProgressBar progressBar;
        @FXML private VBox rightPanel;
        @FXML private ListView<String> bookmarksList;

        public BrowserController(Stage stage) {
            this.stage = stage;
        }

        @FXML
        public void initialize() {
            webEngine = webView.getEngine();

            setupWebView();
            setupButtons();
            setupBookmarksContextMenu();

            // Загружаем стартовую страницу
            loadPage("https://duckduckgo.com");
        }

        private void setupWebView() {
            // Синхронизация URL в текстовом поле при переходах
            webEngine.locationProperty().addListener((obs, oldLoc, newLoc) -> {
                if (newLoc != null && !newLoc.isEmpty()) {
                    urlField.setText(newLoc);
                }
            });

            // Обновление заголовка окна
            webEngine.titleProperty().addListener((obs, oldTitle, newTitle) -> {
                stage.setTitle(newTitle != null ? newTitle + " - JavaFX Browser" : "JavaFX Browser");
            });

            // Прогресс-бар загрузки
//            progressBar.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED || newState == Worker.State.FAILED) {
                    progressBar.setVisible(false);
                } else {
                    progressBar.setVisible(true);
                }
            });

            // Управление историей (кнопки Назад/Вперед)
            WebHistory history = webEngine.getHistory();
            history.currentIndexProperty().addListener((obs, oldIdx, newIdx) -> {
                int currentIndex = newIdx.intValue();
                btnBack.setDisable(currentIndex == 0);
                btnForward.setDisable(currentIndex >= history.getEntries().size() - 1);
            });
        }

        private void setupButtons() {
            // Обработка ввода в адресную строку (Enter)
            urlField.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER) processInput(urlField.getText());
            });
            btnGo.setOnAction(e -> processInput(urlField.getText()));

            // Навигация
            btnBack.setOnAction(e -> {
                if (webEngine.getHistory().getCurrentIndex() > 0) {
                    webEngine.getHistory().go(-1);
                    processInput(urlField.getText());
                }
            });
            btnForward.setOnAction(e -> {
                WebHistory history = webEngine.getHistory();
                if (history.getCurrentIndex() < history.getEntries().size() - 1) {
                    history.go(1);
                    processInput(urlField.getText());
                }
            });
            btnRefresh.setOnAction(e -> webEngine.reload());

            // Демо HTML
            btnHtml.setOnAction(e -> {
                String html = "<html><body style='font-family:sans-serif; padding: 20px;'>" +
                        "<h2>Внутренний HTML</h2><p>Этот код отрендерен прямо из Java-строки!</p>" +
                        "</body></html>";
                webEngine.loadContent(html);
                urlField.setText("(Локальный HTML)");
            });

            // Работа с панелью (Скрыть/Показать)
            btnTogglePanel.setOnAction(e -> {
                boolean isVisible = rightPanel.isVisible();
                rightPanel.setVisible(!isVisible);
                rightPanel.setManaged(!isVisible); // Чтобы панель "схлопывалась" и освобождала место
            });

            // Добавление закладки
            btnAddBookmark.setOnAction(e -> {
                String currentUrl = webEngine.getLocation();
                String title = webEngine.getTitle();
                if (currentUrl != null && !currentUrl.isEmpty() && currentUrl.startsWith("http")) {
                    bookmarksList.getItems().add(title != null ? title + " | " + currentUrl : currentUrl);
                } else {
                    bookmarksList.getItems().add("[Заметка] " + urlField.getText());
                }
            });

            // Открытие закладки
            btnOpenBookmark.setOnAction(e -> openSelectedBookmark());
            bookmarksList.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2) openSelectedBookmark(); // Двойной клик
            });
        }

        private void setupBookmarksContextMenu() {
            ContextMenu menu = new ContextMenu();
            MenuItem delete = new MenuItem("🗑 Удалить");
            delete.setOnAction(e -> {
                String selected = bookmarksList.getSelectionModel().getSelectedItem();
                if (selected != null) bookmarksList.getItems().remove(selected);
            });
            menu.getItems().add(delete);
            bookmarksList.setContextMenu(menu);
        }

        private void processInput(String input) {
            String text = input.trim();
            if (text.isEmpty()) return;

            // Если похоже на URL
            if (text.contains(".") && !text.contains(" ")) {
                if (!text.startsWith("http://") && !text.startsWith("https://")) {
                    text = "https://" + text;
                }
                loadPage(text);
            } else {
                // Если просто текст - ищем в поисковике
                loadPage("https://duckduckgo.com/search?q=" + text.replace(" ", "+"));
            }
        }

        private void loadPage(String url) {
            webEngine.load(url);
        }

        private void openSelectedBookmark() {
            String selected = bookmarksList.getSelectionModel().getSelectedItem();
            if (selected != null && selected.contains("http")) {
                String url = selected.substring(selected.indexOf("http"));
                loadPage(url);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}