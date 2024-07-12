package simulationparser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static simulationparser.LineType.REQUEST;
import static simulationparser.LineType.USER;

public class SimulationParser {

    private final File file;
    private SimulationStat simulationStat;
    private final List<SingleRequestStat> singleRequestStats = new ArrayList<>();
    private final List<GeneralRequestStat> generalRequestStats = new ArrayList<>();
    private final TimeStat timeStat = new TimeStat();

    public SimulationParser(File file) {
        this.file = file;
    }

    public void parseData() {
        try (SimulationReader reader = new SimulationReader(file)) {
            List<String> header = reader.readNext();
            simulationStat = new SimulationStat(header);
            List<String> line;
            while ((line = reader.readNext()) != null) {
                switch (line.get(0)) {
                    case REQUEST:
                        singleRequestStats.add(new SingleRequestStat(line));
                        timeStat.updateRequest(line);
                        break;
                    case USER:
                        timeStat.updateUser(line);
                        break;
                }
                simulationStat.setEndTime(line);
            }
            timeStat.convert();
            timeStat.addResponseTimes(singleRequestStats);
            timeStat.calculatePercentiles();
            calculateRequestsStats();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void calculateRequestsStats() {
        generalRequestStats.add(new GeneralRequestStat("allRequests"));
        Set <String> requestNames = singleRequestStats.stream()
                .map(SingleRequestStat::getName)
                .collect(Collectors.toSet());
        requestNames.forEach( requestName ->
                generalRequestStats.add(new GeneralRequestStat(requestName)));
        for (GeneralRequestStat requestStat : generalRequestStats) {
            for (SingleRequestStat request : singleRequestStats) {
                if (request.getName().equals(requestStat.getName()) || requestStat.getName().equals("allRequests")) {
                    requestStat.countResult(request);
                }
            }
            requestStat.calculateMetrics();
        }
    }

    public void writeJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("target/metrics.json"), this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SimulationStat getSimulationStat() {
        return simulationStat;
    }

    public void setSimulationStat(SimulationStat simulationStat) {
        this.simulationStat = simulationStat;
    }

    public List<SingleRequestStat> getSingleRequestStats() {
        return singleRequestStats;
    }

    public List<GeneralRequestStat> getGeneralRequestStats() {
        return generalRequestStats;
    }

    public TimeStat getTimeStat() {
        return timeStat;
    }
}
