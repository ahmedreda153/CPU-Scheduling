import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;



public class SJF {
    List<Process> processes = new ArrayList<>();

    public SJF(List<Process> processes) {
        this.processes = processes;
    }

    public void sjfScheduling() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the context switching time: ");
        int contextSwitchingTime = scanner.nextInt();
        scanner.close();
        Collections.sort(processes, Comparator.comparingInt(Process::getArriveTime));
        List<Process> ProcessesTemp = new ArrayList<>();
        for (Process process : processes) {
            ProcessesTemp.add(new Process(process.processID, process.burstTime, process.arriveTime));
        }
        int time = ProcessesTemp.get(0).arriveTime + ProcessesTemp.get(0).burstTime;
        System.out.println("Process " + ProcessesTemp.get(0).processID + " starts at " + ProcessesTemp.get(0).arriveTime 
                 + " to " + time);
        processes.get(0).startingTime = ProcessesTemp.get(0).arriveTime;
        ProcessesTemp.remove(0);
        while (!ProcessesTemp.isEmpty()) {
            int i = -1;
            int minValue = Integer.MAX_VALUE;
            for (int j = 0; j < ProcessesTemp.size(); j++) {
                Process process = ProcessesTemp.get(j);
                if (process.arriveTime <= time) {
                    if (process.burstTime < minValue) {
                        minValue = process.burstTime;
                        i = j;
                    }
                }
            }
            ProcessesTemp.get(i).startingTime = time+contextSwitchingTime;
            processes.get(i).startingTime = time+contextSwitchingTime;
            System.out.println("Context Switching to next process at time " + ProcessesTemp.get(i).startingTime);
            time += minValue+contextSwitchingTime;
            System.out.println("Process " + ProcessesTemp.get(i).processID + " starts at " +
                    ProcessesTemp.get(i).startingTime + " to " +time);
            ProcessesTemp.remove(i);
        }
        averageWaitingTime(contextSwitchingTime);  
    }

    public void averageWaitingTime(int contextSwitchingTime) {
        int totalWaitingTime = 0;
        for (int i=1;i<processes.size();i++) {
           int endTime=processes.get(i).startingTime+processes.get(i).burstTime;
           totalWaitingTime+=endTime;
        }
        System.out.println("Average waiting time = " + (float) totalWaitingTime/ processes.size());
    }
}
