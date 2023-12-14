import java.util.ArrayList;
import java.util.List;

public class App {
      public static void main(String[] args) throws Exception {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 8,0));
        processes.add(new Process(2, 4,2));
        processes.add(new Process(3, 1,4));
        processes.add(new Process(4, 4,5));
        // SJF sjf = new SJF(processes);
        // sjf.sjfScheduling(processes);
        // SRTF srtf = new SRTF(processes);
        // srtf.SRTFScheduling();

        AG ag = new AG();
        System.out.println(ag.random_function(0, 20));
    }
}

