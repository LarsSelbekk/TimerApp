package com.selbekk.lars.timerapp;

import javafx.beans.NamedArg;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;

public class Timer extends Control {
    private static final int MAX_SECONDS = 999 * 3600 + 59 * 60 + 59;

    private static int numCreated = 0;
    static final ArrayList<Timer> runningTimers = new ArrayList<>();

    public StringProperty label=new SimpleStringProperty();
    public IntegerProperty totalTimeSeconds=new SimpleIntegerProperty();
    public IntegerProperty remainingTimeSeconds=new SimpleIntegerProperty();
    public BooleanProperty paused = new SimpleBooleanProperty(true);
    public BooleanProperty ringing = new SimpleBooleanProperty(false);

    static java.util.Timer scheduler = new java.util.Timer();

    static {
        scheduler.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tickAll();
            }
        }, 1000L, 1000L);

        // DEBUG
        scheduler.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                int b = 0;
                for (var a : runningTimers) {
                    System.out.println(a.toString()+b++);
                }
                System.out.println();
            }
        }, 10000L, 10000L);
    }

    public Timer(@NamedArg("label") String label, @NamedArg("totalTimeSeconds") int totalTimeSeconds) {
        this.label.set(label);
        this.totalTimeSeconds.set(totalTimeSeconds);
        this.remainingTimeSeconds.set(totalTimeSeconds);
        togglePaused();

        //DEBUG
        System.out.println("Created Timer model");
    }

    public Timer() {
        this("Timer " + ++numCreated, 60);
    }

    public Timer(SerializedTimer serializedTimer) {
        this();
        deserialize(serializedTimer);
    }

    public void togglePaused() {
        if (paused.get()) {
            if (remainingTimeSeconds.get() == 0) {
                ringing.set(true);
            } else runningTimers.add(this);
            paused.set(false);
        } else {
            ringing.set(false);
            paused.set(true);
            runningTimers.remove(this);
        }
    }

    public void setTotalTimeSeconds(int newTotal) {
        totalTimeSeconds.set(newTotal);
    }

    @Override
    protected Skin<Timer> createDefaultSkin() {
        return new TimerSkin(this);
    }

    private void tick() {
        remainingTimeSeconds.set(remainingTimeSeconds.get() - 1);
    }

    private static void tickAll() {
        Iterator<Timer> it = runningTimers.iterator();
        while (it.hasNext()) {
            Timer t = it.next();
            t.tick();
            if (t.remainingTimeSeconds.get() <= 0) {
                it.remove();
                t.ringing.set(true);
            }
        }
    }

    public void snooze(int inputSeconds) {
        int seconds = inputSeconds;
        if (inputSeconds + totalTimeSeconds.get() > MAX_SECONDS) {
            seconds = MAX_SECONDS - totalTimeSeconds.get();
        }
        if (seconds < 0) {
            throw new IllegalArgumentException("Snooze time must be positive! Got " + seconds);
        } else if (seconds == 0) {
            return;
        }
        if (ringing.get()) {
            runningTimers.add(this);
            ringing.set(false);
        }
        remainingTimeSeconds.set(remainingTimeSeconds.get() + seconds);
        totalTimeSeconds.set(totalTimeSeconds.get() + seconds);
    }

    public SerializedTimer serialize() {
        return new SerializedTimer(this);
    }

    public void deserialize(SerializedTimer st) {
        this.label.set(st.label_ser);
        this.remainingTimeSeconds.set(st.remainingTimeSeconds_ser);
        this.totalTimeSeconds.set(st.totalTimeSeconds_ser);
        this.paused.set(st.paused_ser);
        this.ringing.set(st.ringing_ser);
    }

    public static class SerializedTimer implements Serializable {
        private static final long serialVersionUID = 1L;

        String label_ser;
        int remainingTimeSeconds_ser;
        int totalTimeSeconds_ser;
        boolean paused_ser;
        boolean ringing_ser;

        public SerializedTimer(Timer t) {
            label_ser = t.label.get();
            remainingTimeSeconds_ser = t.remainingTimeSeconds.get();
            totalTimeSeconds_ser = t.totalTimeSeconds.get();
            paused_ser = t.paused.get();
            ringing_ser = t.ringing.get();
        }

        public SerializedTimer() {
            label_ser = "";
            remainingTimeSeconds_ser = 60;
            totalTimeSeconds_ser = 60;
            paused_ser = true;
            ringing_ser = false;
        }

        public Timer deserialize() {
            return new Timer(this);
        }
    }

    @Override
    public String toString() {
        return "Timer{" +
                "label=" + label.get() +
                ", totalTimeSeconds=" + totalTimeSeconds.get() +
                ", remainingTimeSeconds=" + remainingTimeSeconds.get() +
                ", paused=" + paused.get() +
                ", ringing=" + ringing.get() +
                '}';
    }
}
