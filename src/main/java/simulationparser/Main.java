package simulationparser;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        SimulationParser sim = new SimulationParser(new File("simulation.log"));
        sim.parseData();
        sim.writeJson();
    }
}
