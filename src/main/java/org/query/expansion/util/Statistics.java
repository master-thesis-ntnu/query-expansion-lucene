package org.query.expansion.util;

import java.util.concurrent.TimeUnit;

public class Statistics {
    public static double getAverageSearchTime(long totalSearchTimeInNanoSeconds, int numberOfTestRuns) {
        long sumOfDeltaTimesInMilliseconds = TimeUnit.NANOSECONDS.toMillis(totalSearchTimeInNanoSeconds);

        return sumOfDeltaTimesInMilliseconds / numberOfTestRuns;
    }
}
