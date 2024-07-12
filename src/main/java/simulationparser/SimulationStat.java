package simulationparser;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static simulationparser.LineType.REQUEST;
import static simulationparser.LineType.USER;

public class SimulationStat {

    private String name;
    private String gatlingVersion;
    private Date date;
    private Long duration;
    private Long startTime;
    private Long endTime;

    public SimulationStat(List<String> header) {
        setName(header);
        setDate(header);
        setStartTime(header);
        setGatlingVersion(header);
    }

    public String getName() {
        return name;
    }

    public void setName(List<String> header) {
        this.name = header.get(2);
    }

    public String getGatlingVersion() {
        return gatlingVersion;
    }

    public void setGatlingVersion(List<String> header) {
        this.gatlingVersion = header.get(5);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(List<String> header) {
        this.date = new Date(Long.parseLong(header.get(3)));
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(List<String> header) {
        this.startTime = Long.parseLong(header.get(3));
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(List<String> header) {
        switch (header.get(0)) {
            case REQUEST:
                this.endTime = Long.parseLong(header.get(4));
                break;
            case USER:
                this.endTime = Long.parseLong(header.get(3));
                break;
        }
        this.duration = Duration.of(endTime-startTime, ChronoUnit.MILLIS).toMillis();
    }
}
