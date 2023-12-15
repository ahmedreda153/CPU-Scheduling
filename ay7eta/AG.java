import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
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
            System.out.print(runningProcess.name + " from time "+ time);
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
                System.out.println(" "+runningProcess.name + " to time "+ time);
                runningProcess = temp;
                System.out.print(runningProcess.name + " from time "+ time);
                time += Math.min(Math.ceil(runningProcess.quantum * 0.5), runningProcess.remainingTime)  ;
                runningProcess.remainingTime -= Math.ceil(runningProcess.quantum * 0.5);
               
               
            }
            
            while (time<=totalProcessTime+1) {
                if (processIndex < processes.size()  && time >= processes.get(processIndex).arriveTime ) {
                    if(processes.get(processIndex).AGFactor < runningProcess.AGFactor){
                        runningProcess.quantum += runningProcess.quantum -(Math.ceil(runningProcess.quantum * 0.5) + currSteps); 
                        runningProcess.quantumHistory.add(runningProcess.quantum);
                        arrivedProcesses.add(processes.get(processIndex));
                        readyQueue.add(runningProcess);
                        System.out.println(" "+runningProcess.name + " to time "+ time);
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
                    System.out.println(" "+runningProcess.name + " to time "+ time);
                    runningProcess.completionTime = time;
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
                    System.out.println(" "+runningProcess.name + " to time "+ time);

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
            System.out.println(process.name+" History Of Quantum: "+process.quantumHistory);
        }
        calcAndPrintWaitingAndTurnarround();
    }
   

    public void printGanttChart() {
        System.out.println("Gantt Chart:");
        for (Process p : ganttChart) {
            System.out.print("P" + p.processID + " ");
        }
        System.out.println();
    }
    private void calcAndPrintWaitingAndTurnarround(){
        System.out.println("Turnaround and waiting time for each process");
        System.out.println("------------------------");
        int totalTurnarround = 0 ;
        int totalWaitingTime = 0;
        for(Process p : dieList){
                p.turnarround = p.completionTime-p.arriveTime;
                p.waitingTime = p.turnarround - p.burstTime;
                totalTurnarround+=p.turnarround;
                totalWaitingTime += p.waitingTime;
                System.out.println(p.name+ " turnaround: "+p.turnarround+", waiting time: "+p.waitingTime);
        }

        float avgTurnarround = totalTurnarround / (float)dieList.size() ;
        float avgWaitingTime = totalWaitingTime / (float)dieList.size() ;

        System.out.println("Average Turn Around Time");
        System.out.println("------------------------");
        System.out.println( avgTurnarround);
        System.out.println("Average Waiting Time");
        System.out.println("--------------------");
        System.out.println(avgWaitingTime);
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
