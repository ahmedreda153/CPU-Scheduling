import java.util.ArrayList;
import java.util.List;

public class App {
      public static void main(String[] args) throws Exception {
        List<Process> processes = new ArrayList<>();
        processes.add(new Process(1, 6));
        processes.add(new Process(2, 8));
        processes.add(new Process(3, 7));
        processes.add(new Process(4, 3));
        SJF sjf = new SJF(processes);
        sjf.sjfScheduling(processes);
    }
}

