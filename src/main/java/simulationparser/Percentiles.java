package simulationparser;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Percentiles {

    private Double percentile25;
    private Double percentile50;
    private Double percentile75;
    private Double percentile80;
    private Double percentile85;
    private Double percentile90;
    private Double percentile95;
    private Double percentile99;
    private Integer min;
    private Integer max;

    public void calculate(List<Long> responseTimes) {
        if (!responseTimes.isEmpty()) {
            percentile25 = getPercentile(responseTimes, 25);
            percentile50 = getPercentile(responseTimes, 50);
            percentile75 = getPercentile(responseTimes, 75);
            percentile80 = getPercentile(responseTimes, 80);
            percentile85 = getPercentile(responseTimes, 85);
            percentile90 = getPercentile(responseTimes, 90);
            percentile95 = getPercentile(responseTimes, 95);
            percentile99 = getPercentile(responseTimes, 99);
            min = Collections.min(responseTimes).intValue();
            max = Collections.max(responseTimes).intValue();
        } else {
            percentile25 = 0d;
            percentile50 = 0d;
            percentile75 = 0d;
            percentile80 = 0d;
            percentile85 = 0d;
            percentile90 = 0d;
            percentile95 = 0d;
            percentile99 = 0d;
            min = 0;
            max = 0;
        }
    }

    public Double getPercentile(List<Long> responses, double percentile) {
        List<Long> sortedList = responses.stream()
                .sorted()
                .collect(Collectors.toList());
        double rank = (percentile / 100) * (sortedList.size() - 1);
        int intRank = (int) rank;
        if (rank == intRank) {
            return (double) sortedList.get(intRank);
        } else {
            return sortedList.get(intRank) + (rank - intRank) * (sortedList.get(intRank + 1) - sortedList.get(intRank));
        }
    }

    public Double getPercentile25() {
        return percentile25;
    }

    public Double getPercentile50() {
        return percentile50;
    }

    public Double getPercentile75() {
        return percentile75;
    }

    public Double getPercentile80() {
        return percentile80;
    }

    public Double getPercentile85() {
        return percentile85;
    }

    public Double getPercentile90() {
        return percentile90;
    }

    public Double getPercentile95() {
        return percentile95;
    }

    public Double getPercentile99() {
        return percentile99;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }
}
