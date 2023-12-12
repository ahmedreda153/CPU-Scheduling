import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        ArrayList<Process> processes = new ArrayList<Process>();
        processes.add(new Process(1, 0, 4, 2));
        processes.add(new Process(2, 1, 3, 3));
        processes.add(new Process(3, 2, 1, 4));
        processes.add(new Process(4, 3, 5, 5));
        processes.add(new Process(5, 4, 2, 5));
        PriorityScheduling priorityScheduling = new PriorityScheduling(processes);
        priorityScheduling.buildGanttChart();
        System.out.println();
        System.out.println();
        System.out.println();
        priorityScheduling.printGanttChart();
        System.out.println();
        priorityScheduling.printWaitingTime();
        System.out.println();
        priorityScheduling.printAverageWaitingTime();
        System.out.println();
        priorityScheduling.printTurnAroundTime();
        System.out.println();
        priorityScheduling.printAverageTurnAroundTime();
    }
}
