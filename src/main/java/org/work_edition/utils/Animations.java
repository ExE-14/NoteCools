package org.work_edition.utils;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class Animations {

    /**
     * Запускает анимацию плавного появления для двух переданных узлов.
     * Сосредоточено на заголовке, и, подзаголовке.
     *
     * @param mainTitleLabel  узел для главного заголовка
     * @param subtitleLabel   узел для подзаголовка
     *
     * Выше покащан лишь один сопсоб как я это использовал.
     */
    public static void playFadeInAnimation(Node mainTitleLabel, Node subtitleLabel) {
        FadeTransition fadeMain = new FadeTransition(Duration.seconds(1.5), mainTitleLabel);
        fadeMain.setFromValue(0);
        fadeMain.setToValue(1);

        FadeTransition fadeSub = new FadeTransition(Duration.seconds(1), subtitleLabel);
        fadeSub.setFromValue(0);
        fadeSub.setToValue(1);

        // Алгоритм: сначала появляется главный заголовок, затем подзаголовок
        SequentialTransition sequence = new SequentialTransition(fadeMain, fadeSub);
        sequence.play();
    }

    /**
     * Запускает анимацию плавного появления для одного переданного узла.
     *
     * @param node  узел для анимации
     */
    public static void playFadeInAnimation(Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    /**
     * Добавляет анимацию при наведении для переданной кнопки.
     *
     * @param button  кнопка, для которой будет добавлена анимация
     * @param Distance  расстояние в пикселях, на которое кнопка будет смещаться при наведении
     */
    public static void addHoverAnimetion(Button button, double Distance) {
        TranslateTransition hoverUp = new TranslateTransition(Duration.millis(200), button);
        hoverUp.setToY(Distance);

        TranslateTransition hoverDown = new TranslateTransition(Duration.millis(200), button);
        hoverDown.setToY(0);

        final boolean[] isAnimeting = {false};

        hoverUp.setOnFinished(e -> isAnimeting[0] = false);
        hoverDown.setOnFinished(e -> isAnimeting[0] = false);

        button.setOnMouseEntered(e -> {
            if (!isAnimeting[0] && button.getTranslateY() != Distance) {
                hoverDown.stop();
                hoverUp.playFromStart();
            }
        });

        button.setOnMouseExited(e -> {
            if (!isAnimeting[0] && button.getTranslateY() != 0) {
                isAnimeting[0] = true;
                hoverUp.stop();
                hoverDown.playFromStart();
            }
        });
    }

    public static void addHoverAnimetion(Button button) {
        addHoverAnimetion(button, -10);
    }

}
