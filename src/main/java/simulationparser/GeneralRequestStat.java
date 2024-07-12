package simulationparser;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralRequestStat {

    private String name;
    private Integer totalCount = 0;
    private Integer successCount = 0;
    private Integer failedCount = 0;
    private Double failedPercent;
    private Double minResponseTime;
    private Double maxResponseTime;
    private Double meanResponseTime;
    private Double standardDeviation;
    private Double percentile50;
    private Double percentile75;
    private Double percentile95;
    private Double percentile99;
    private List<Long> responseTimes = new ArrayList<>();
    private List<ErrorStat> errors = new ArrayList<>();

    public GeneralRequestStat(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void addSuccess() {
        totalCount++;
        successCount++;
    }

    private void addFailed() {
        totalCount++;
        failedCount++;
    }

    private void addResponseTime(Long responseTime) {
        responseTimes.add(responseTime);
    }

    private void addError(String error) {
        if (errors.contains(new ErrorStat(error, 1))) {
            Integer oldValue = errors.stream()
                    .filter(errorStat -> errorStat.getErrorName().equals(error))
                    .map(ErrorStat::getCount)
                    .findFirst()
                    .get();
            errors = errors.stream()
                    .peek(errorStat -> {
                        if (errorStat.getErrorName().equals(error))
                            errorStat.setCount(oldValue + 1);
                    })
                    .collect(Collectors.toList());
        } else {
            errors.add(new ErrorStat(error, 1));
        }
    }

    public void countResult(SingleRequestStat request) {
        if (request.getResult()) {
            addSuccess();
        } else {
            addFailed();
            addError(request.getError());
        }
        addResponseTime(request.getResponseTime());
    }

    public void calculateMetrics() {
        failedPercent = (double) failedCount / totalCount * 100;
        double[] responses = responseTimes.stream().mapToDouble(Long::doubleValue).toArray();
        minResponseTime = StatUtils.min(responses);
        maxResponseTime = StatUtils.max(responses);
        meanResponseTime = StatUtils.mean(responses);
        StandardDeviation stdDev = new StandardDeviation();
        standardDeviation = stdDev.evaluate(responses, meanResponseTime);
        percentile50 = StatUtils.percentile(responses, 50);
        percentile75 = StatUtils.percentile(responses, 75);
        percentile95 = StatUtils.percentile(responses, 95);
        percentile99 = StatUtils.percentile(responses, 99);
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public Double getFailedPercent() {
        return failedPercent;
    }

    public void setFailedPercent(Double failedPercent) {
        this.failedPercent = failedPercent;
    }

    public Double getMinResponseTime() {
        return minResponseTime;
    }

    public void setMinResponseTime(Double minResponseTime) {
        this.minResponseTime = minResponseTime;
    }

    public Double getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(Double maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public Double getMeanResponseTime() {
        return meanResponseTime;
    }

    public void setMeanResponseTime(Double meanResponseTime) {
        this.meanResponseTime = meanResponseTime;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public Double getPercentile50() {
        return percentile50;
    }

    public void setPercentile50(Double percentile50) {
        this.percentile50 = percentile50;
    }

    public Double getPercentile75() {
        return percentile75;
    }

    public void setPercentile75(Double percentile75) {
        this.percentile75 = percentile75;
    }

    public Double getPercentile95() {
        return percentile95;
    }

    public void setPercentile95(Double percentile95) {
        this.percentile95 = percentile95;
    }

    public Double getPercentile99() {
        return percentile99;
    }

    public void setPercentile99(Double percentile99) {
        this.percentile99 = percentile99;
    }

    public List<Long> getResponseTimes() {
        return responseTimes;
    }

    public void setResponseTimes(List<Long> responseTimes) {
        this.responseTimes = responseTimes;
    }

    public List<ErrorStat> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorStat> errors) {
        this.errors = errors;
    }
}
