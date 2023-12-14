import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;


public class AG {
    int quantum;
    private List<Process> processes;
    private List<Process> ganttChart;
    private List<Process> dieList;
    private Queue<Process> readyQueue;
    private List<Process> arrivedProcesses;
    private HashMap<Integer, Integer> waitingTime;
    private HashMap<Integer, Integer> turnAroundTime;

    public AG(List<Process> processes, int quantum) {
        this.quantum = quantum;
        this.processes = processes;
        this.readyQueue =  new LinkedList<>();;
        ganttChart = new ArrayList<Process>();
        dieList = new ArrayList<Process>();        
        arrivedProcesses = new ArrayList<Process>();
        waitingTime = new HashMap<Integer, Integer>();
        turnAroundTime = new HashMap<Integer, Integer>();
        processes.sort((p1, p2) -> p1.getArriveTime() - p2.getArriveTime());
        for (Process p : processes) {
            p.quantum = quantum;
            p.quantumHistory.add(p.quantum);
            p.AGFactor = getAGFactor(p);
        }
        // processes.get(0).AGFactor = 20;        
        // processes.get(1).AGFactor = 17;
        // processes.get(2).AGFactor = 16;
        // processes.get(3).AGFactor = 43;

    }

    public AG(){
        quantum = 0;
        processes = new ArrayList<Process>();
        ganttChart = new ArrayList<Process>();
        dieList = new ArrayList<Process>();
        arrivedProcesses = new ArrayList<Process>();
        this.readyQueue = new LinkedList<>();
        waitingTime = new HashMap<Integer, Integer>();
        turnAroundTime = new HashMap<Integer, Integer>();
    }

    public void roundRobinUsingQueue(){
        int totalProcessTime=0;
        for(Process p:processes){
            totalProcessTime += p.burstTime;
        }
        int time = 0;
        int processIndex = 0;
        Process runningProcess = new Process();
        for(time=0;time<totalProcessTime;){
            
            if (arrivedProcesses.isEmpty() && processIndex < processes.size()  && time >= processes.get(processIndex).arriveTime ) {
                runningProcess = processes.get(processIndex);
                arrivedProcesses.add(processes.get(processIndex));
                processIndex++;
            } 
            if (runningProcess.processID == -1 && !readyQueue.isEmpty()) {
                runningProcess = readyQueue.poll();
            } 
            System.out.println("P"+runningProcess.processID);
            time += Math.min(Math.ceil(runningProcess.quantum * 0.5), runningProcess.remainingTime);
            runningProcess.remainingTime -= Math.ceil(runningProcess.quantum * 0.5);
            int currSteps = 0;
            Process temp = runningProcess;
            boolean flag = false;
            for(Process p : arrivedProcesses){
                if (p.AGFactor < temp.AGFactor) {
                    temp = p;
                    flag = true;
                }
            }
            if (flag) {
                runningProcess.quantum += runningProcess.quantum - Math.ceil(runningProcess.quantum * 0.5);
                runningProcess.quantumHistory.add(runningProcess.quantum);
                readyQueue.add(runningProcess);
                runningProcess = temp;
                time += Math.min(Math.ceil(runningProcess.quantum * 0.5), runningProcess.remainingTime)  ;
                runningProcess.remainingTime -= Math.ceil(runningProcess.quantum * 0.5);
                System.out.println("P"+runningProcess.processID);
               
            }
            
            while (time<=totalProcessTime+1) {
                if (processIndex < processes.size()  && time >= processes.get(processIndex).arriveTime ) {
                    if(processes.get(processIndex).AGFactor < runningProcess.AGFactor){
                        runningProcess.quantum += runningProcess.quantum -(Math.ceil(runningProcess.quantum * 0.5) + currSteps); 
                        runningProcess.quantumHistory.add(runningProcess.quantum);
                        arrivedProcesses.add(processes.get(processIndex));
                        readyQueue.add(runningProcess);
                        runningProcess = processes.get(processIndex);
                        processIndex++;
                        
                        break;
                    } else {
                        arrivedProcesses.add(processes.get(processIndex));
                        readyQueue.add(processes.get(processIndex));
                        processIndex++;
                    }
                 
                } else if (runningProcess.remainingTime <= 0) {
                    runningProcess.completionTime = time;
                    runningProcess.turnarround = runningProcess.completionTime - runningProcess.arriveTime;
                    turnAroundTime.put(runningProcess.processID, runningProcess.turnarround);
                    runningProcess.waitingTime = runningProcess.turnarround - runningProcess.burstTime;
                    waitingTime.put(runningProcess.processID, runningProcess.waitingTime);
                    ganttChart.add(runningProcess);
                    runningProcess.quantum = 0;
                    runningProcess.quantumHistory.add(runningProcess.quantum);
                    int numOfOcc = 0;
                    arrivedProcesses.remove(runningProcess);
                    for (Process process : readyQueue) {
                        if (process.equals(runningProcess)) {
                            numOfOcc++;
                        }
                    }
                    for (int j = 0; j < numOfOcc; j++) {
                        for (Process process : readyQueue) {
                            if (process.equals(runningProcess)) {
                                readyQueue.remove(process);
                                break;
                            }
                        }
                    }
                    
                    dieList.add(runningProcess);
                    if (!readyQueue.isEmpty()) {
                        runningProcess = readyQueue.poll();
                    }
                    break;
                }
                // mean
                else if ((Math.ceil(runningProcess.quantum * 0.5) + currSteps) == runningProcess.quantum) {
                    int totalQuantum = 0;
                    for(Process p:arrivedProcesses){
                        totalQuantum += p.quantum;
                    }
                    runningProcess.quantum += Math.ceil((totalQuantum/arrivedProcesses.size())*0.1);
                    runningProcess.quantumHistory.add(runningProcess.quantum);
                    readyQueue.add(runningProcess);
                    runningProcess = readyQueue.poll();
                    break;
                }  else {
                    runningProcess.remainingTime--;
                    time++;
                    currSteps++;
                }
                
            }
            


        }
        for (Process process : processes) {
            System.out.println("P"+process.processID+" "+process.quantumHistory);
        }
    }

    public void roundRobin() {
        int size = processes.size();
        int time = 0;
        int k = 0;
        while (size  > 0) {
            if (processes.get(k).arriveTime > time) {
                k++;
                continue;
            }
            else if (processes.get(k).remainingTime <= quantum) {
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
