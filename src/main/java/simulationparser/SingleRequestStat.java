package simulationparser;

import java.time.Duration;
import java.util.List;

public class SingleRequestStat {

    private String name;
    private boolean result = false;
    private String error;
    private Long responseTime;
    private Long start;
    private Long end;

    public SingleRequestStat(List<String> line) {
        setName(line.get(2));
        setStart(Long.parseLong(line.get(3)));
        setEnd(Long.parseLong(line.get(4)));
        setResponseTime(Duration.ofMillis(end - start).toMillis());
        if (line.get(5).equals("OK")) {
            setResult(true);
        }
        if (!result) {
            setError(line.get(6));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }
}
