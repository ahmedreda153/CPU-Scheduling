import java.util.ArrayList;
import java.util.List;

public class App {
  public static void main(String[] args) throws Exception {
    List<Process> processes = new ArrayList<>();
    processes.add(new Process(1, 0, 7, 4));
    processes.add(new Process(2, 2, 4, 9));
    processes.add(new Process(3, 4, 1, 3));
    processes.add(new Process(4, 4, 4, 8));
    SJF sjf = new SJF(processes);
    sjf.sjfScheduling();
    // SRTF srtf = new SRTF(processes);
    // srtf.SRTFScheduling();

    // AG ag = new AG(processes, 4);
    // ag.roundRobinUsingQueue();
  }
  
}
