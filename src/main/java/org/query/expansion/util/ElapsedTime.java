package org.query.expansion.util;

public class ElapsedTime {
    private long startTime;
    private long endTime;
    private boolean running = false;

    private static final long NANOSECONDS_TO_MILLIISECONDS = 1000000;

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

        return deltaTime / NANOSECONDS_TO_MILLIISECONDS;
    }
}
