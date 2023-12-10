import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class SJF {
    List<Process> processes = new ArrayList<>();
    public SJF(List<Process> processes) {
        this.processes = processes;
    }
    public void sjfScheduling(List<Process> processes){
        Collections.sort(processes, Comparator.comparingInt(p -> p.burstTime));
        int time = 0;
        for (Process process : processes) {
            System.out.println("Process " + process.processID + " starts at " + time + " ends at " + (time + process.burstTime));
            process.setStartingTime(time + process.burstTime);
            if (!processes.isEmpty()) {
                System.out.println("Context Switching to next process at time " + time);
                
            }
            time += process.burstTime;
        }
        averageWaitingTime(processes);
    }
    public void averageWaitingTime(List<Process> processes){
        int totalWaitingTime = 0;
        for (Process process : processes) {
            totalWaitingTime += process.startingTime - process.burstTime;
        }
        System.out.println("Average Waiting Time: " + totalWaitingTime / processes.size());
    }
}


