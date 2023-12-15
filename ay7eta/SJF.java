import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;



public class SJF {
    List<Process> processes = new ArrayList<>();
    List<Integer> timeline = new ArrayList<>();
    List<Process> finishedProcess = new ArrayList<>();
    PriorityQueue<Process> arrivedProcess = new PriorityQueue<>(
        new Comparator<Process>(){
            @Override
            public int compare(Process p1, Process p2) {
                if(p1.burstTime < p2.burstTime){
                    return -1;
                }else if(p1.burstTime > p2.burstTime){
                    return 1;
                }else{
                    return 0;
                }
            }
        }
    );


    public SJF(List<Process> processes) {
        this.processes = processes;
    }

    public void sjfScheduling(int contextSwitchingTime) {
       
        Collections.sort(processes, Comparator.comparingInt(Process::getArriveTime));
        
       
        int time = processes.get(0).arriveTime + processes.get(0).burstTime;
        System.out.println("Process " + processes.get(0).name + " starts at " + processes.get(0).arriveTime 
                 + " to " + time);
        processes.get(0).startingTime = processes.get(0).arriveTime;
        processes.get(0).completionTime = time;
        finishedProcess.add(processes.get(0));

        int processIndex = 1;
        while (finishedProcess.size() != processes.size()) {
            for (int i = processIndex; i < processes.size(); i++) {
                if (processes.get(i).arriveTime <= time) {
                    arrivedProcess.add(processes.get(i));
                    processIndex++;
                }
            }
            if (arrivedProcess.isEmpty()) {
                time++;
                continue;
            }else{
                Process currProcess = arrivedProcess.poll();
                currProcess.startingTime = time+contextSwitchingTime;
                System.out.println("Context Switching to next process at time " + currProcess.startingTime);
                time += currProcess.burstTime+contextSwitchingTime;
                System.out.println("Process " + currProcess.name + " starts at " +
                currProcess.startingTime + " to " +time);
                currProcess.completionTime = time-contextSwitchingTime;
                finishedProcess.add(currProcess);
            }
            
            
        }
        calcAndPrintWaitingAndTurnarround();
    }

    private void calcAndPrintWaitingAndTurnarround(){
        System.out.println("Turnaround and waiting time for each process");
        System.out.println("------------------------");
        int totalTurnarround = 0 ;
        int totalWaitingTime = 0;
        for(Process p : finishedProcess){
                p.turnarround = p.completionTime-p.arriveTime;
                p.waitingTime = p.turnarround - p.burstTime;
                totalTurnarround+=p.turnarround;
                totalWaitingTime += p.waitingTime;
                System.out.println(p.name+ " turnaround: "+p.turnarround+", waiting time: "+p.waitingTime);
        }

        float avgTurnarround = totalTurnarround / (float)finishedProcess.size() ;
        float avgWaitingTime = totalWaitingTime / (float)finishedProcess.size() ;

        System.out.println("Average Turn Around Time");
        System.out.println("------------------------");
        System.out.println( avgTurnarround);
        System.out.println("Average Waiting Time");
        System.out.println("--------------------");
        System.out.println(avgWaitingTime);
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
