package com.selbekk.lars.timerapp;

import javafx.beans.NamedArg;
import javafx.scene.control.Button;

public class SnoozeButton extends Button {
    public int snoozeTime;

    public void setSnoozeTime(int snoozeTime) {
        this.snoozeTime = snoozeTime;
    }

    public int getSnooozeTime() {
        return snoozeTime;
    }

    public SnoozeButton(@NamedArg("snoozeTime") int snoozeTime, @NamedArg("text") String text) {
        super(text);
        this.snoozeTime = snoozeTime;
    }
}
