import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SJF {
    List<Process> processes = new ArrayList<>();
    public SJF(List<Process> processes) {
        this.processes = processes;
    }
    public void sjfScheduling(List<Process> processes){
        Collections.sort(processes, Comparator.comparingInt(p -> p.burstTime));
        int time = 0;
        for (Process process : processes) {
            process.setStartingTime(time);
            System.out.println("Process " + process.processID + " starts at " + time + " ends at " + (time + process.burstTime));
            if (!processes.isEmpty()) {
                System.out.println("Context Switching to next process at time " + time);
            }
            time += process.burstTime;
        }
        System.out.println(processes.get(0).startingTime);
        averageWaitingTime(processes);
    }
    public void averageWaitingTime(List<Process> processes){
        int totalWaitingTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.startingTime;
        }
        System.out.println("Average Waiting Time: " + totalWaitingTime / processes.size());
    }
}