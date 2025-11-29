package logparser;

import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {

    private long totalTraffic;          // суммарный трафик теперь long
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
        totalTraffic = 0L;
        minTime = null;
        maxTime = null;
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getResponseSize();

        LocalDateTime time = entry.getDateTime();
        if (minTime == null || time.isBefore(minTime)) minTime = time;
        if (maxTime == null || time.isAfter(maxTime)) maxTime = time;
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null || minTime.equals(maxTime)) {
            return totalTraffic;
        }

        double hours = Duration.between(minTime, maxTime).toMinutes() / 60.0;
        if (hours == 0) hours = 1.0;

        return totalTraffic / hours;
    }

    public long getTotalTraffic() { return totalTraffic; }
    public LocalDateTime getMinTime() { return minTime; }
    public LocalDateTime getMaxTime() { return maxTime; }
}
