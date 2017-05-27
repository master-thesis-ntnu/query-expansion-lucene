package org.query.expansion.util;

public class Statistics {
    private static final long NANO_TO_SECOND_RATIO = 1000000;

    public static double getAverageSearchTime(long totalSearchTimeInNanoSeconds, int numberOfTestRuns) {
        double sumOfDeltaTimesInMilliseconds = totalSearchTimeInNanoSeconds / NANO_TO_SECOND_RATIO;

        return sumOfDeltaTimesInMilliseconds / numberOfTestRuns;
    }
}
