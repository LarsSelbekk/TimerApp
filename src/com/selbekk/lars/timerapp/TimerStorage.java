package com.selbekk.lars.timerapp;

import java.io.IOException;
import java.util.List;

public interface TimerStorage {
    void save(String filename, List<Timer> timers) throws IOException;
    List<Timer> load(String filename) throws IOException;
}
