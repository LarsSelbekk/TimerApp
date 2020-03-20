package com.selbekk.lars.timerapp;

import javafx.beans.NamedArg;
import javafx.scene.control.Button;
import javafx.scene.control.Skin;

public class PauseButton extends Button {
//    public final BooleanProperty paused = new SimpleBooleanProperty();

    public PauseButton(@NamedArg("label") String label) {
        super(label);
    }

    @Override
    protected Skin<PauseButton> createDefaultSkin() {
        return new PauseButtonSkin(this);
    }
}
