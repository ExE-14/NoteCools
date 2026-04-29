package org.work_edition.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.work_edition.utils.Animations;
import org.work_edition.utils.SceneManager;
import org.work_edition.utils.SceneManagerAware;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Base64;

public class ContactsWindowController implements SceneManagerAware {

    @FXML private WebView imageCarouselWeb;
    @FXML private Button btnBack;

    @SuppressWarnings("unused")
    @FXML private VBox mainContainer; // Корневой узел

    private SceneManager sceneManager;
    public void setSceneManager(SceneManager sceneManager) {
        this.sceneManager = sceneManager;
    }

    @FXML
    public void initialize() {
        Animations.addHoverAnimetion(btnBack, -5);

        btnBack.setOnAction(e -> {
            if (sceneManager != null) {
                try {
                    sceneManager.switchToMainWindow();
                } catch (IOException ex) {
                    System.err.println("CONTACTS CONTROLLER: ERROR SWITCHING TO PREV WINDOW");
                }
            } else {
                System.err.println("CONTACTS CONTROLLER: SCENE MANAGER IS STILL NULL!");
            }
        });

        try {
            // Я ЗАЕБАЛСЯ ЭТО ФИКСИТЬ, ПУСТЬ БУДЕТ ТАКАЯ ТУПАЯ ЗАТЫЧКА НАХУЙ, КАК В АНУСЕ ОБЕЗЬЯНЫ БЛОЯТЬЬЬЬ
            throw new Exception();
        } catch (Exception e) {
            System.err.println("CONTACTS CONTROLLER: ERROR INITIALIZING CAROUSEL");
            e.printStackTrace();

            initializeWithOutCarousel();
        }
    }

    @FXML
    private void initializeWithOutCarousel() {
        Animations.addHoverAnimetion(btnBack, -5);
        btnBack.setOnAction(e -> {
            if (sceneManager != null) {
                try {
                    sceneManager.switchToMainWindow();
                } catch (IOException ex) {
                    System.err.println("CONTACTS CONTROLLER: ERROR SWITCHING TO PREV WINDOW");
                }
            } else {
                System.err.println("CONTACT CONTROLLER: SCENE MANAGER IS STILL NULL!");
            }
        });

        WebEngine engine = imageCarouselWeb.getEngine();
        String errorHtml = """
            <html>
            <head>
                <style>
                    body { display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #2c3e50; color: #ecf0f1; font-family: 'Segoe UI', sans-serif; margin: 0; }
                    .error-message { text-align: center; }
                    .error-message h2 { font-size: 24px; margin-bottom: 10px; }
                    .error-message p { font-size: 16px; opacity: 0.8; margin: 5px 0; }
                    .ErrBtn { margin-top: 15px; padding: 10px 20px; font-size: 14px; background-color: #3490db; color: #ecf0f1; border: none; border-radius: 6px; cursor: pointer; transition: background-color 0.3s; }
                    .ErrBtn:hover { background-color: #2980b9; }
                </style>
            </head>
            <body>
                <div class="error-message">
                    <h2>Ошибка загрузки карусели</h2>
                    <p>К сожалению, изображения не были найдены...</p>
                    <p>Возможно, это исправится позже, но не факт.</p>
                    <button class="ErrBtn" onclick="window.open('https://github.com/ExE-14/NoteCools/issues')">GitHub Помощь</button>
                </div>
            </body>
            </html>
        """;

        engine.loadContent(errorHtml);
    }

    private void initCarousel() {
        WebEngine engine = imageCarouselWeb.getEngine();

        URL firstUrl = getClass().getResource("/images/carousel_Images/code-programming-todo-project.png");
        URL secondUrl = getClass().getResource("/images/carousel_Images/programming-todo-project.png");
        URL thirdUrl = getClass().getResource("/images/carousel_Images/scheme-todo-project.png");

        if (firstUrl == null || secondUrl == null || thirdUrl == null) {
            System.err.println("No images! NOT FOUND: INITCAROUSEL-CONTACT_WINDOW");
            if (firstUrl == null) System.err.println("NOT FOUND: /images/carousel_Images/code-programming-todo-project.png");
            if (secondUrl == null) System.err.println("NOT FOUND: /images/carousel_Images/programming-todo-project.png");
            if (thirdUrl == null) System.err.println("NOT FOUND: /images/carousel_Images/scheme-todo-project.png");

            initializeWithOutCarousel();
            return;
        }

        String firstPhotoUrl = toBase64DataUri(firstUrl, "image/png");
        String secondPhotoUrl = toBase64DataUri(secondUrl, "image/png");
        String thirdPhotoUrl = toBase64DataUri(thirdUrl, "image/png");

        String htmlTemplate = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { margin: 0; padding: 0; background-color: #2c3e50; overflow: hidden; border-radius: 12px; font-family: 'Segoe UI', sans-serif; }
                    .carousel { position: relative; width: 100vw; height: 100vh; border-radius: 12px; overflow: hidden;}
                    .slide { position: absolute; width: 100vw; height: 100vh; opacity: 0; transition: opacity 1s ease-in-out; background-size: cover; background-position: center; }
                    .slide.active { opacity: 1; z-index: 1; }

                    .info-panel { position: absolute; bottom: -80px; left: 0; width: 100vw; background: linear-gradient(to top, rgba(0,0,0,0.9), transparent); color: #ecf0f1; padding: 30px 20px 20px 20px; box-sizing: border-box; transition: bottom 0.4s ease; z-index: 10; }
                    .carousel:hover .info-panel { bottom: 0; }
                    .info-panel h2 { margin: 0 0 5px 0; font-size: 20px; }
                    .info-panel p { margin: 0; font-size: 14px; opacity: 0.8;}

                    .dots-container { position: absolute; top: 15px; width: 100vw; text-align: center; z-index: 10; }
                    .dot { display: inline-block; width: 12px; height: 12px; cursor: pointer; background: rgba(255,255,255,0.5); border-radius: 36px; margin: 0 6px; transition: background 0.3s; box-shadow: 0 2px 4px rgba(0,0,0,0.3); }
                    .dot.active { background: #3498db; }
                </style>
            </head>
            <body>
                <div class="carousel" id="carousel">
                    <div class="dots-container" id="dots"></div>
                    <div class="info-panel">
                        <h2 id="slide-title">Title</h2>
                        <p id="slide-desc">About</p>
                    </div>
                </div>

                <script>
                    %s
                </script>
            </body>
            </html>
        """;

        String scriptTemplate = """
            const slidesData = [
                {url:'%s5', title:'Программирование', desc:'Написание кода и архитектуры'},
                {url:'%s10', title:'Железо и Схемотехника', desc:'Работа с микроконтроллерами'},
                {url:'%s15', title:'Кибербезопасность', desc:'Изучение сетей и протоколов'}
            ];
            const carousel = document.getElementById('carousel');
            const dotsContainer = document.getElementById('dots');
            const titleEl = document.getElementById('slide-title');
            const descEl = document.getElementById('slide-desc');
            let currentIndex = 0;
            slidesData.forEach((data, index) => {
                let slide = document.createElement('div');
                slide.className = 'slide' + (index === 0 ? ' active' : '');
                slide.style.backgroundImage = `url('${data.url}')`;
                slide.id = 'slide-' + index;
                carousel.appendChild(slide);
                let dot = document.createElement('div');
                dot.className = 'dot' + (index === 0 ? ' active' : '');
                dot.id = 'dot-' + index;
                dot.addEventListener('click', () => { goToSlide(index); });
                dotsContainer.appendChild(dot);
            });
            function updateInfo(index) {
                titleEl.innerText = slidesData[index].title;
                descEl.innerText = slidesData[index].desc;
            }
            function goToSlide(index) {
                document.getElementById('slide-' + currentIndex).classList.remove('active');
                document.getElementById('dot-' + currentIndex).classList.remove('active');
                currentIndex = index;
                document.getElementById('slide-' + currentIndex).classList.add('active');
                document.getElementById('dot-' + currentIndex).classList.add('active');
                updateInfo(currentIndex);
            }
            function nextSlide() {
                goToSlide((currentIndex + 1) % slidesData.length);
            }
            updateInfo(0);
            setInterval(nextSlide, 5400);
        """;

        String fullScript = scriptTemplate
                .replace("%s5", firstPhotoUrl)
                .replace("%s10", secondPhotoUrl)
                .replace("%s15", thirdPhotoUrl);

        String finalHtml = htmlTemplate.replace("%s", fullScript);

        // For tests, now 0.0 - this is very bad
//        System.out.println("WebView width: " + imageCarouselWeb.getWidth());
//        System.out.println("WebView height: " + imageCarouselWeb.getHeight());

        engine.loadContent(finalHtml);

        // блять меня уже так заебал этот веб вайв сука, я рот его нахуй ебал блять
//        String testHtml = "<html><body><img src='" + firstPhotoUrl + "' width='100%'></body></html>";
//        engine.loadContent(testHtml);
    }

    // Вспомогательный метод: читает ресурс по URL и возвращает data:image/png;base64,...
    private String toBase64DataUri(java.net.URL url, String mimeType) {
        try (InputStream in = url.openStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = baos.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(imageBytes);
            return "data:" + mimeType + ";base64," + base64;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load image from " + url, e);
        }
    }

}