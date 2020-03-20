package com.selbekk.lars.timerapp;

import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.shape.SVGPath;

import java.io.*;
import java.util.Scanner;

public class PauseButton extends Button {
//    public static final SVGPath pause = new SVGPath();
//    public static final SVGPath play = new SVGPath();

    static {
//        File f = new File("file:D:/OneDrive%20-%20NTNU/Proggeprosjekter/TimerApp/out/production/TimerApp/com/selbekk/lars/timerapp/assets/play.svg");
//        try (
//                FileReader r1 = new FileReader(f
//                        PauseButton.class.getResource("assets/play.svg").toExternalForm()
//                );
//                Scanner r2 = new Scanner(new File(
//                        PauseButton.class.getResource("assets/pause.svg").toExternalForm()))
//        ) {
//            r1.useDelimiter("\\Z");
//            r2.useDelimiter("\\Z");
//            r1.
//            pause.setContent(r1.next());
//            play.setContent(r2.next());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public PauseButton() {
        super();
        Image playImage = new Image(getClass().getResource("assets/play.png").toExternalForm());
        this.setGraphic(new ImageView(playImage));
    }

    @Override
    protected Skin<PauseButton> createDefaultSkin() {
        return new PauseButtonSkin(this);
    }
}
