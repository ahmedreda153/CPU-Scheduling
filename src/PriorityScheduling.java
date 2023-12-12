import java.util.ArrayList;
import java.util.HashMap;

public class PriorityScheduling {
    private ArrayList<Process> processes;
    private ArrayList<Process> ganttChart;
    private HashMap<Integer, Integer> waitingTime;
    private HashMap<Integer, Integer> turnAroundTime;
    private ArrayList<Process> arrivedProcesses;

    public PriorityScheduling(ArrayList<Process> processes) {
        this.processes = processes;
        ganttChart = new ArrayList<Process>();
        waitingTime = new HashMap<Integer, Integer>();
        turnAroundTime = new HashMap<Integer, Integer>();
        arrivedProcesses = new ArrayList<Process>();

        // Sort the processes by priority ascendingly
        processes.sort((p1, p2) -> p1.getPriority() - p2.getPriority());
    }

    public PriorityScheduling() {
        processes = new ArrayList<Process>();
        ganttChart = new ArrayList<Process>();
        waitingTime = new HashMap<Integer, Integer>();
        turnAroundTime = new HashMap<Integer, Integer>();
    }

    public int getLowestPriorityIndex(ArrayList<Process> processes) {
        int min = (int) 1e9;
        int index = -1;
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(i).getPriority() < min) {
                min = processes.get(i).getPriority();
                index = i;
            }
        }
        return index;
    }

    public int getHighestPriorityIndex(ArrayList<Process> processes) {
        int max = -1;
        int index = -1;
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(i).getPriority() > max) {
                max = processes.get(i).getPriority();
                index = i;
            }
        }
        return index;
    }

    public boolean searchProcess(ArrayList<Process> processes, int id) {
        for (Process p : processes) {
            if (p.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public int searchProcessIndex(ArrayList<Process> processes, int id) {
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public void buildGanttChart() {
        int time = 0;
        int starv = 0;
        int index = 0;
        int size = processes.size();
        int k = 0;
        while (size > 0) {
            if (starv == 3) {
                starv = 0;
                for (int i = 0; i < processes.size(); i++) {
                    processes.get(i).setPriority(processes.get(i).getPriority() + 1);
                }
            } else {
                starv++;
                time += processes.get(k).getBurstTime();
                processes.get(k).setCompletionTime(time);
                processes.get(k)
                        .setTurnAroundTime(processes.get(k).getCompletionTime() - processes.get(k).getArriveTime());
                processes.get(k).setWaitingTime(processes.get(k).getTurnAroundTime() - processes.get(k).getBurstTime());
                waitingTime.put(processes.get(k).getId(), processes.get(k).getWaitingTime());
                turnAroundTime.put(processes.get(k).getId(), processes.get(k).getTurnAroundTime());
                ganttChart.add(processes.get(k));
                processes.remove(k);
                size--;
                if (arrivedProcesses.size() < processes.size()) {
                    for (int i = 0; i < processes.size(); i++) {
                        if (processes.get(i).getArriveTime() <= time) {
                            arrivedProcesses.add(processes.get(i));
                        }
                    }
                }
                if (arrivedProcesses.size() > 0) {
                    index = getHighestPriorityIndex(arrivedProcesses);
                    k = searchProcessIndex(processes, arrivedProcesses.get(index).getId());
                    arrivedProcesses.remove(index);
                }
            }
        }
    }

    public void printGanttChart() {
        System.out.println("Gantt Chart");
        System.out.println("-----------");
        for (Process p : ganttChart) {
            System.out.println(p.getId() + " " + p.getArriveTime() + " " + p.getBurstTime() + " " + p.getPriority());
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
}
