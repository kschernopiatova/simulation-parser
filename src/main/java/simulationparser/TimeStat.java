package simulationparser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TimeStat {

    private List<UserStat> stats;
    private Map<Long, UserStat> timestampStats = new LinkedHashMap<>();
    private Integer activeUsers = 0;
    private Integer maxUsers = 0;

    private class UserStat {
        private Integer numberOfUsers;
        private Integer requestsPerSec;
        private Integer responsesPerSec;
        private Integer successResponse;
        private Integer failedResponse;
        private List<Long> responseTimes = new ArrayList<>();
        private Percentiles percentiles;
        private Long timestamp;

        public UserStat(Long timestamp) {
            numberOfUsers = activeUsers;
            requestsPerSec = 0;
            responsesPerSec = 0;
            successResponse = 0;
            failedResponse = 0;
            this.timestamp = timestamp;
            percentiles = new Percentiles();
        }

        private void addUser() {
            numberOfUsers++;
        }

        private void addRequest() {
            requestsPerSec++;
        }

        private void addSuccessResponse() {
            successResponse++;
            responsesPerSec++;
        }

        private void addFailedResponse() {
            failedResponse++;
            responsesPerSec++;
        }

        private void calculatePercentiles() {
            percentiles.calculate(responseTimes);
        }

        public Integer getNumberOfUsers() {
            return numberOfUsers;
        }

        public void setNumberOfUsers(Integer numberOfUsers) {
            this.numberOfUsers = numberOfUsers;
        }

        public Integer getRps() {
            return requestsPerSec;
        }

        public void setRps(Integer rps) {
            this.requestsPerSec = rps;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public void setResponseTimes(List<SingleRequestStat> allRequests) {
            allRequests.stream()
                    .filter(request -> (request.getStart() / 1000) == (timestamp))
                    .forEach(request -> responseTimes.add(request.getResponseTime()));
        }

        public List<Long> getResponseTimes() {
            return responseTimes;
        }

        public Integer getResponsesPerSec() {
            return responsesPerSec;
        }

        public Integer getSuccessResponse() {
            return successResponse;
        }

        public Integer getFailedResponse() {
            return failedResponse;
        }

        public Percentiles getPercentiles() {
            return percentiles;
        }
    }

    public void updateRequest(List<String> line) {
        Long timestamp = Long.parseLong(line.get(3)) / 1000;
        Long response = Long.parseLong(line.get(3)) / 1000;
        if (!timestampStats.containsKey(timestamp)) {
            timestampStats.put(timestamp, new UserStat(timestamp));
        }
        if (!timestampStats.containsKey(response)) {
            timestampStats.put(response, new UserStat(timestamp));
        }
        timestampStats.get(timestamp).addRequest();
        if (line.get(5).equals("OK")) {
            timestampStats.get(response).addSuccessResponse();
        } else {
            timestampStats.get(response).addFailedResponse();
        }
    }

    public void updateUser(List<String> line) {
        Long timestamp = Long.parseLong(line.get(3)) / 1000;
        if (!timestampStats.containsKey(timestamp)) {
            timestampStats.put(timestamp, new UserStat(timestamp));
        }
        if (line.get(2).equals("START")) {
            timestampStats.get(timestamp).addUser();
            activeUsers++;
            if (maxUsers < activeUsers) {
                maxUsers = activeUsers;
            }
        } else {
            activeUsers--;
        }
    }

    public void convert() {
        stats = new ArrayList<>(timestampStats.values());
    }

    public Integer getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(Integer activeUsers) {
        this.activeUsers = activeUsers;
    }

    public Integer getMaxUsers() {
        return maxUsers;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public List<UserStat> getStats() {
        return stats;
    }

    public void setStats(List<UserStat> stats) {
        this.stats = stats;
    }

    public void addResponseTimes(List<SingleRequestStat> allRequests) {
        stats.forEach(stat -> stat.setResponseTimes(allRequests));
    }

    public void calculatePercentiles() {
        stats.forEach(UserStat::calculatePercentiles);
    }
}
