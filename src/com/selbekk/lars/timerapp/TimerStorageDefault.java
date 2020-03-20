package com.selbekk.lars.timerapp;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TimerStorageDefault implements TimerStorage {

    private static FileChooser fileChooser = new FileChooser();

    static {
        fileChooser.setTitle("Select storage file");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Serialization Files", "*.ser"),
                new ExtensionFilter("All Files", "*"));
    }

    @Override
    public void save(String filename, List<Timer> timers) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            for (Timer t : timers) {
                out.writeObject(t.serialize());
            }
        }
    }

    @Override
    public List<Timer> load(String filename) throws IOException {
        ArrayList<Timer> ret = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            try {
                while (true) {
                    Timer.SerializedTimer serializedTimer = (Timer.SerializedTimer) in.readObject();
                    if (serializedTimer != null) {
                        Timer deserialized = serializedTimer.deserialize();
                        ret.add(deserialized);

                    }
                }
            } catch (EOFException ignored) {
            }
        } catch (ClassCastException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public boolean saveWithDialog(Stage parentWindow, List<Timer> timers) {
        File selectedFile = fileChooser.showSaveDialog(parentWindow);
        if (selectedFile != null) {
            try {
                save(selectedFile.getPath(), timers);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public List<Timer> loadWithDialog(Stage parentWindow) {
        File selectedFile = fileChooser.showOpenDialog(parentWindow);
        if (selectedFile != null) {
            try {
                return load(selectedFile.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
