package org.query.expansion.util;

import java.util.concurrent.TimeUnit;

public class ElapsedTime {
    private long startTime;
    private long endTime;
    private boolean running = false;

    public void start() {
        startTime = System.nanoTime();
        running = true;
    }

    public void stop() {
        endTime = System.nanoTime();
        running = false;
    }

    public long getElapsedTimeInMilliSeconds() {
        long deltaTime;
        if (running) {
            deltaTime =  System.nanoTime() - startTime;
        } else {
            deltaTime = endTime - startTime;
        }

        return TimeUnit.NANOSECONDS.toMillis(deltaTime);
    }

    public long getElapsedTimeInMicroSeconds() {
        long deltaTime;
        if (running) {
            deltaTime =  System.nanoTime() - startTime;
        } else {
            deltaTime = endTime - startTime;
        }

        return TimeUnit.NANOSECONDS.toMicros(deltaTime);
    }

    public long getElapsedTimeInNanoSeconds() {
        long deltaTime;
        if (running) {
            deltaTime = System.nanoTime() - startTime;
        } else {
            deltaTime = endTime - startTime;
        }

        return deltaTime;
    }
}
