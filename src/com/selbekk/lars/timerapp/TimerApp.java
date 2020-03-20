package com.selbekk.lars.timerapp;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class TimerApp extends Application {
    ScrollPane root;
    TimerStorageDefault storage;
    Stage window;

    @FXML
    Button addNewTimerButton;
    @FXML
    Button saveButton;
    @FXML
    Button loadButton;
    @FXML
    VBox contentRoot;

    @Override
    public void start(Stage stage) throws IOException {
        window = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Root.fxml"));
        loader.setController(this);
        root = loader.load();
        root.setFitToWidth(true);
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.setTitle("Timer");
        window.show();

        storage = new TimerStorageDefault();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @FXML
    public void initialize() {

        addNewTimerButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> contentRoot.getChildren().add(new Timer()));
        saveButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> storage.saveWithDialog(window, Timer.runningTimers));
        loadButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                event -> contentRoot.getChildren().addAll(storage.loadWithDialog(window)));
    }
}

// TODO: Knapper kun på hover.
// TODO: Refine look.
// TODO: Fluent scaling.
// TODO: App icon.
// TODO: Pause.
// TODO: Delete-knapp.
// TODO: Meny når man lager ny for å velge duration, navn og farge.
// TODO: Vise navn