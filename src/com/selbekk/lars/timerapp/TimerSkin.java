package com.selbekk.lars.timerapp;

import javafx.animation.StrokeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

import java.io.IOException;

public class TimerSkin extends SkinBase<Timer> {

    public static final PseudoClass RINGING = new PseudoClass() {
        @Override
        public String getPseudoClassName() {
            return "ringing";
        }
    };
    public static final PseudoClass PAUSED = new PseudoClass() {
        @Override
        public String getPseudoClassName() {
            return "paused";
        }
    };

    protected final Timer model;

    protected final DoubleProperty progressProperty = new SimpleDoubleProperty();

    protected RadialProgressIndicatorSkin progressIndicatorSkin;

    private static final String HOURS_FORMAT = "%d:%02d:%02d";
    private static final String MINUTES_FORMAT = "%d:%02d";

    private static final AudioClip ringTone;
    private final StrokeTransition ringingAnimation = new StrokeTransition();
    private Paint usualStroke = Paint.valueOf("000000");
    private static int ringingTimers = 0;

    static {
        ringTone = new AudioClip(TimerSkin.class.getResource("assets/ringTone.mp3").toExternalForm());
    }

    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    Text remainingTimeText;
    @FXML
    Text totalTimeText;
    @FXML
    SnoozeButton snoozeButton1m;
    @FXML
    SnoozeButton snoozeButton10m;
    @FXML
    SnoozeButton snoozeButton1h;
    @FXML
    PauseButton pauseButton;
    @FXML
    HBox snoozeRow;

    public TimerSkin(Timer model) {
        super(model);
        this.model = model;

        model.getStyleClass().setAll("timer");
        model.getStylesheets().add(getClass().getResource("timer.css").toExternalForm());

        ringingAnimation.setCycleCount(StrokeTransition.INDEFINITE);
        ringingAnimation.setDuration(Duration.millis(400));
        ringingAnimation.setFromValue(Color.CORNSILK);
        ringingAnimation.setToValue(Color.CRIMSON);
        ringingAnimation.setAutoReverse(true);

        ringTone.setCycleCount(AudioClip.INDEFINITE);

        progressProperty.bind(Bindings.createDoubleBinding(
                () -> model.remainingTimeSeconds.doubleValue() / model.totalTimeSeconds.get(),
                model.remainingTimeSeconds, model.totalTimeSeconds));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Timer.fxml"));
        loader.setController(this);
        try {
            Parent p = loader.load();
            this.getChildren().setAll(p.getChildrenUnmodifiable());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void initialize() {
        progressIndicatorSkin = new RadialProgressIndicatorSkin(progressIndicator);
        progressIndicator.setSkin(progressIndicatorSkin);

        ringingAnimation.setShape(progressIndicatorSkin.fullRing);

        progressIndicatorSkin.progressProperty.bind(progressProperty);

        model.paused.addListener((observable, oldValue, newValue) -> {
            model.pseudoClassStateChanged(PAUSED, newValue);
        });

        model.ringing.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                usualStroke = progressIndicatorSkin.fullRing.getStroke();
                model.pseudoClassStateChanged(RINGING, true);
                ringingAnimation.play();
                if (ringingTimers++ == 0) ringTone.play();
            } else {
                model.pseudoClassStateChanged(RINGING, false);
                ringingAnimation.stop();
                progressIndicatorSkin.fullRing.setStroke(usualStroke);
                if (--ringingTimers == 0) ringTone.stop();
            }
        });

        totalTimeText.textProperty().bind(Bindings.createStringBinding(() -> "/" + toTimerFormat(model.totalTimeSeconds.get()),
                model.totalTimeSeconds));
        remainingTimeText.textProperty().bind(Bindings.createStringBinding(() -> toTimerFormat(model.remainingTimeSeconds.get()),
                model.remainingTimeSeconds));

        for (Node node : snoozeRow.getChildren()) {
            if (!(node instanceof SnoozeButton)) continue;
            SnoozeButton button = (SnoozeButton) node;
            button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> model.snooze(button.getSnooozeTime()));
        }
    }

    private String toTimerFormat(int totalSeconds) {
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        if (hours != 0) {
            return String.format(HOURS_FORMAT, hours, minutes, seconds);
        } else {
            if (minutes != 0) {
                return String.format(MINUTES_FORMAT, minutes, seconds);
            } else {
                return String.valueOf(seconds);
            }
        }
    }

    @Override
    protected double computeMinWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 80;
    }

    @Override
    protected double computeMinHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected double computeMaxWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefWidth(height, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected double computeMaxHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return computePrefHeight(width, topInset, rightInset, bottomInset, leftInset);
    }

    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
//        return progressIndicator.getPrefWidth();
        return 200;
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
//        return progressIndicator.getPrefHeight() + snoozeButton1m.getPrefHeight();
        return 300;
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        progressIndicator.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
        pauseButton.resizeRelocate(contentX, contentY, contentWidth, contentHeight);

        remainingTimeText.relocate(progressIndicator.getLayoutX() + progressIndicator.getWidth() / 2 - remainingTimeText.prefWidth(contentHeight) / 2,
                progressIndicator.getLayoutY() + progressIndicator.getHeight() / 2 - remainingTimeText.prefHeight(contentWidth) / 2);

        totalTimeText.relocate(progressIndicator.getLayoutX() + progressIndicator.getWidth() / 2 - totalTimeText.prefWidth(contentHeight) / 2,
                remainingTimeText.getLayoutY() + remainingTimeText.getWrappingWidth());

        snoozeRow.resizeRelocate(contentX,
                progressIndicator.getLayoutY() + progressIndicator.getHeight(),
                contentWidth, 20);
    }
}
