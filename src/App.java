import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class App {
      public static void main(String[] args) throws Exception {
        List<Process> processes = new ArrayList<>();
        // processes.add(new Process(1, 0,8,4));
        // processes.add(new Process(2, 1,4, 9));
        // processes.add(new Process(3, 2,9, 3));
        // processes.add(new Process(4, 3,5,8));
        // // SJF sjf = new SJF(processes);
        // // sjf.sjfScheduling(processes);
        // SRTF srtf = new SRTF(processes);
        // List<Integer> test = srtf.SRTFScheduling();

        // PlotWindow window =  new PlotWindow("Gantt Chart", test);
        // window.setVisible(true);
        // window.pack();
        // window.setLocationRelativeTo(null);

        // AG ag = new AG(processes, 4);
        // ag.roundRobinUsingQueue();
        System.out.println("Welcome to Operating System Scheduler^_^");
        System.out.println("Please enter number of processes:");
        Scanner input = new Scanner(System.in);
        int numOfProcesses = input.nextInt();
        System.out.println("Please enter Round Robin Time Quantum:");
        int quantumTime = input.nextInt();
        System.out.println("Please enter Context switching Time:");
        int contextSTime = input.nextInt();
        for (int index = 0; index < numOfProcesses; index++) {
          System.out.println("Please enter Process"+ index+1 +" name:");
          String processName = input.next();
          System.out.println("Please enter Process"+ index+1 +" color:");
          String color = input.next();
          System.out.println("Please enter Process"+ index+1 +" Arrival Time:");
          int arrivalTime = input.nextInt();
          System.out.println("Please enter Process"+ index+1 +" Burst Time:");
          int burstTime = input.nextInt();
          System.out.println("Please enter Process"+ index+1 +" Priority:");
          int priority = input.nextInt();
          processes.add(new Process(index+1 ,processName,color,arrivalTime,burstTime,priority));
        }
        System.out.println("Please choose one of the following Schedulers:\n1) Shortest Job First\n2) Shortest Remaining Time First\n3) Priority\n4) AG");
        
        
        int choice = input.nextInt();
        switch (choice){
          case 1 :
            SJF sjf = new SJF(processes);
            sjf.sjfScheduling(processes);
            break;
          case 2 :
            SRTF srtf = new SRTF(processes);
            srtf.SRTFScheduling();
          case 3 :
            PriorityScheduling P = new PriorityScheduling();
            P.buildGanttChart(processes);
            break;
          case 4 :
             AG ag = new AG(processes, quantumTime);
             ag.roundRobinUsingQueue();
             break;
          default : System.out.println("Invalid Choice!");
        }
      
    }
}
