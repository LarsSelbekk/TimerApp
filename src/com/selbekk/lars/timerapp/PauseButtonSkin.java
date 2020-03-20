package com.selbekk.lars.timerapp;

import javafx.css.PseudoClass;
import javafx.scene.control.SkinBase;
import javafx.scene.input.MouseEvent;

public class PauseButtonSkin extends SkinBase<PauseButton> {
    public final PauseButton pauseButton;
    public final PseudoClass paused = new PseudoClass() {
        @Override
        public String getPseudoClassName() {
            return "paused";
        }
    };

    protected PauseButtonSkin(PauseButton model) {
        super(model);
        this.pauseButton = model;
        model.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> togglePaused());
        getSkinnable().getStylesheets().setAll(
                PauseButtonSkin.class.getResource("pauseButton.css").toExternalForm());
        System.out.println("Created pause button skin");
    }

    private void togglePaused() {
        System.out.println("togle");
        System.out.println(pauseButton.getParent());
        Timer parent = (Timer) pauseButton.getParent();
        parent.togglePaused();
        pauseButton.pseudoClassStateChanged(paused, parent.paused.get());
    }
}
