import java.util.ArrayList;
import java.util.HashMap;

public class AG {
    int quantum;
    private ArrayList<Process> processes;
    private ArrayList<Process> ganttChart;
    private HashMap<Integer, Integer> waitingTime;
    private HashMap<Integer, Integer> turnAroundTime;

    public AG(ArrayList<Process> processes, int quantum) {
        this.quantum = quantum;
        this.processes = processes;
        ganttChart = new ArrayList<Process>();
        waitingTime = new HashMap<Integer, Integer>();
        turnAroundTime = new HashMap<Integer, Integer>();
        processes.sort((p1, p2) -> p1.getArriveTime() - p2.getArriveTime());
    }

    public AG(){
        quantum = 0;
        processes = new ArrayList<Process>();
        ganttChart = new ArrayList<Process>();
        waitingTime = new HashMap<Integer, Integer>();
        turnAroundTime = new HashMap<Integer, Integer>();
    }

    public void roundRobin() {
        int size = processes.size();
        int readyQueueSize = 0;
        int time = 0;
        int k = 0;

        while (size + readyQueueSize > 0) {
            if (processes.get(k).arriveTime > time) {
                k++;
                continue;
            }
            else if (processes.get(k).remainingTime <= quantum) {
                System.out.println("3");
                time += processes.get(0).remainingTime;
                processes.get(0).completionTime = time;
                processes.get(0).turnarround = processes.get(0).completionTime - processes.get(0).arriveTime;
                turnAroundTime.put(processes.get(0).processID, processes.get(0).turnarround);
                processes.get(0).waitingTime = processes.get(0).turnarround - processes.get(0).burstTime;
                waitingTime.put(processes.get(0).processID, processes.get(0).waitingTime);
                ganttChart.add(processes.get(0));
                processes.remove(0);
                size--;
            } else {
                System.out.println("4");
                time += quantum;
                processes.get(0).remainingTime -= quantum;
                ganttChart.add(processes.get(0));
                processes.add(processes.get(0));
                processes.remove(0);
            }
            k = 0;
        }
    }

    public void printGanttChart() {
        System.out.println("Gantt Chart:");
        for (Process p : ganttChart) {
            System.out.print("P" + p.processID + " ");
        }
        System.out.println();
    }

    public void printWaitingTime() {
        System.out.println("Waiting Time");
        System.out.println("------------");
        for (HashMap.Entry<Integer, Integer> entry : waitingTime.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println();
    }

    public void printAverageWaitingTime() {
        System.out.println("Average Waiting Time");
        System.out.println("--------------------");
        double averageWaitingTime = 0;
        for (HashMap.Entry<Integer, Integer> entry : waitingTime.entrySet()) {
            averageWaitingTime += entry.getValue();
        }
        averageWaitingTime /= waitingTime.size();
        System.out.println(averageWaitingTime);
        System.out.println();
    }

    public void printTurnAroundTime() {
        System.out.println("Turn Around Time");
        System.out.println("----------------");
        for (HashMap.Entry<Integer, Integer> entry : turnAroundTime.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println();
    }

    public void printAverageTurnAroundTime() {
        System.out.println("Average Turn Around Time");
        System.out.println("------------------------");
        double averageTurnAroundTime = 0;
        for (HashMap.Entry<Integer, Integer> entry : turnAroundTime.entrySet()) {
            averageTurnAroundTime += entry.getValue();
        }
        averageTurnAroundTime /= turnAroundTime.size();
        System.out.println(averageTurnAroundTime);
        System.out.println();
    }

    private Integer random_function(Integer start,Integer end){
        return (int)(Math.random()*(end-start+1)+start);
    }

    public Integer getAGFactor(Process process){
        int rf= random_function(0, 20);
        if (rf<10) {
            return rf + process.arriveTime + process.burstTime;
        }else if (rf>10) {
            return 10  + process.arriveTime + process.burstTime;
        }else{
            return process.priority + process.arriveTime + process.burstTime;
        }
    }

}
