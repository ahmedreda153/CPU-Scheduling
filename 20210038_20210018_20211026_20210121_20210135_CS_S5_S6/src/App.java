import java.util.ArrayList;
import java.util.List;
import java.util.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.IntervalCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.gantt.GanttCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.io.Serial;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;


class ProcessD{
    int processId;
    int start;
    int end;

    ProcessD(int start,int end,int processId){
        this.processId = processId;
        this.start = start;
        this.end = end;
    }

    ProcessD(){
        this.start = 0;
        this.end = 0;
        processId = 0;
    }
}

class Process {
    String name;
    String color;
    int processID;
    int burstTime;
    int startingTime;
    int remainingTime;
    int completionTime;
    int arriveTime;
    int turnarround;
    int waitingTime;
    int priority;
    int waitingTimeCounter;
    int quantum;
    int AGFactor;
    List<Integer> quantumHistory;
    boolean isStarted=false;


    public Process(){
        this.name=""   ;
        this.burstTime = 0;
        this.completionTime = 0;
        this.startingTime = 0 ;
        this.processID = -1;
        this.remainingTime =0;
        this.arriveTime = 0;
        this.quantumHistory = new ArrayList<>();
    }
    public Process(int processID,String name, String color, int arrivalTime, int burstTime, int priority)
    {
        this.processID = processID;
        this.name=name;
        this.color=color;
        this.arriveTime=arrivalTime;
        this.burstTime=burstTime;
        this.priority=priority;
        this.remainingTime = burstTime;
         this.quantumHistory = new ArrayList<>();
    }
    public Process(int processID, int burstTime) {
         this.name=""   ;
        this.processID = processID;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.quantumHistory = new ArrayList<>();
    }
     public Process(int processID, int burstTime,int arriveTime) {
         this.name=""   ;
        this.processID = processID;
        this.burstTime = burstTime;
        this.arriveTime=arriveTime;
        this.remainingTime = burstTime;
        this.quantumHistory = new ArrayList<>();
    }

    public Process(int id, int arrivalTime, int burstTime, int priority) {
         this.name=""   ;
        this.processID = id;
        this.arriveTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.quantumHistory = new ArrayList<>();
    }

    public int getId() {
        return processID;
    }

    public void setID(int id) {
        this.processID = id;
    }

    public int getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(int arrivalTime) {
        this.arriveTime = arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnAroundTime() {
        return turnarround;
    }

    public void setTurnAroundTime(int turnAroundTime) {
        this.turnarround = turnAroundTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public void setStartingTime(int startingTime){
        this.startingTime=startingTime;
    }

    public void setRemainingTime(int reminingTime){
        this.remainingTime = reminingTime;
    }

    public void incrementWaitingTimeCounter() {
        waitingTimeCounter++;
    }

    public int getWaitingTimeCounter() {
        return waitingTimeCounter;
    }

    public int getStartingTime(){
        return startingTime;
    }



}




class PlotWindow extends JFrame {

    @Serial
    private static final long serialVersionUID = 1L;

    static class MyToolTipGenerator extends IntervalCategoryToolTipGenerator {


        @Override
        public String generateToolTip(CategoryDataset cds, int row, int col) {
            final String s = super.generateToolTip(cds, row, col);
            StringBuilder sb = new StringBuilder(s);
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }

    StringBuilder message = new StringBuilder();
    String type = "ERROR";

    public PlotWindow(final String title, List<Integer> processes) {
        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       

        GanttCategoryDataset dataset = createDataset(processes);
        JFreeChart chart = createChart(dataset);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setItemMargin(-2);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(500, 300));
        setContentPane(chartPanel);


      
    }

     public PlotWindow(final String title, List<ProcessD> processes, String type ) {
        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       

        GanttCategoryDataset dataset = createDataset(processes,type);
        JFreeChart chart = createChart(dataset);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setItemMargin(-2);

        ChartPanel chartPanel = new ChartPanel(chart);

        chartPanel.setPreferredSize(new Dimension(500, 300));
        setContentPane(chartPanel);


      
    }

    public GanttCategoryDataset createDataset(List<ProcessD> processes, String type) {
        HashMap<Integer, Task> tasks = new HashMap<>();

    

        List<ProcessD> processTime = processes;
       


        for (ProcessD process : processTime) {
            if (tasks.get(process.processId) == null) {
                tasks.put(process.processId,  new Task("P"+process.processId, new SimpleTimePeriod(0, processTime.get(processTime.size() - 1).end)));
            }
           
            Task t = new Task("P"+process.processId, new SimpleTimePeriod(process.start, process.end));
            t.setDescription("P"+process.processId);
            if (tasks.get(process.processId) != null) {
                tasks.get(process.processId).addSubtask(t);
                
            }

        }

        TaskSeriesCollection collection = new TaskSeriesCollection();
        tasks.forEach((K, V) -> {
            TaskSeries t = new TaskSeries(V.getDescription());
            t.add(V);
            collection.add(t);
        });
        return collection;
    }

    public GanttCategoryDataset createDataset(List<Integer> processes) {
        HashMap<Integer, Task> tasks = new HashMap<>();

        List<ProcessD> processTime = new ArrayList<>();
       
        int current = processes.get(0);
        int start = 0;

        for (int i = 1; i < processes.size(); i++) {
            if ( processes.get(i) != current) {
                 ProcessD d = new ProcessD(start, i,processes.get(i-1).intValue());
                processTime.add(d);
               
                
                current =  processes.get(i);
                start = i;
            }
        }

       
          ProcessD d = new ProcessD(start, processes.size()-1,processes.get(processes.size()-1));
            processTime.add(d);

        


        for (ProcessD process : processTime) {
            if (tasks.get(process.processId) == null) {
                tasks.put(process.processId,  new Task("P"+process.processId, new SimpleTimePeriod(0, processTime.get(processTime.size() - 1).end)));
            }
           
            Task t = new Task("P"+process.processId, new SimpleTimePeriod(process.start, process.end));
            t.setDescription("P"+process.processId);
            if (tasks.get(process.processId) != null) {
                tasks.get(process.processId).addSubtask(t);
                
            }
        }

        TaskSeriesCollection collection = new TaskSeriesCollection();
        tasks.forEach((K, V) -> {
            TaskSeries t = new TaskSeries(V.getDescription());
            t.add(V);
            collection.add(t);
        });
        return collection;
    }

    private JFreeChart createChart(GanttCategoryDataset dataset) {
        final JFreeChart chart = ChartFactory.createGanttChart(
                "CPU Usage",
                "processes",
                "Time",
                dataset,
                true,
                true,
                false
        );

        CategoryPlot plot = chart.getCategoryPlot();

        plot.getRenderer().setDefaultToolTipGenerator(new MyToolTipGenerator());

        DateAxis axis = (DateAxis) plot.getRangeAxis();

        axis.setDateFormatOverride(new SimpleDateFormat("S"));
        return chart;
    }

}

class SJF {
    List<ProcessD> timeline = new ArrayList<>();
    List<Process> processes = new ArrayList<>();
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

    public List<ProcessD> sjfScheduling(int contextSwitchingTime) {
       
        Collections.sort(processes, Comparator.comparingInt(Process::getArriveTime));
        
       
        int time = processes.get(0).arriveTime + processes.get(0).burstTime;
        System.out.println("Process " + processes.get(0).name + " starts at " + processes.get(0).arriveTime 
                 + " to " + time);
        ProcessD p = new ProcessD(processes.get(0).arriveTime,time,processes.get(0).processID);
        timeline.add(p);
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
                ProcessD p1 = new ProcessD(currProcess.startingTime,time,currProcess.processID);
                timeline.add(p1);
                currProcess.completionTime = time-contextSwitchingTime;
                finishedProcess.add(currProcess);
            }
            
            
        }
        calcAndPrintWaitingAndTurnarround();
        return timeline;
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




class SRTF {
    List<Process> process;
    PriorityQueue<Process> queueProcess;
    List<Process> finishedProcess;
    List<Integer> timeline = new ArrayList<>();

    SRTF(List<Process> process){
        this.process = process;
        queueProcess = new PriorityQueue<Process>(new Comparator<Process>(){
            @Override
            public int compare(Process p1, Process p2) {
                if(p1.remainingTime < p2.remainingTime){
                    return -1;
                }else if(p1.remainingTime > p2.remainingTime){
                    return 1;
                }else{
                    return 0;
                }
            }
        });
        this.finishedProcess = new ArrayList<>();
        this.process.sort((p1, p2) -> p1.getArriveTime() - p2.getArriveTime());
    }
    public List<Integer> SRTFScheduling()
    {
        int totalProcessTime=0;
        for(Process p:process){
            totalProcessTime += p.burstTime;
        }
        int starvationTime = 5;
            int time = 0;
            int processIndex = 0;

            Process shortestJopProcess = new Process();
            Process runiningProcess= new Process();
            System.out.println(totalProcessTime);
            for(time=0;time<totalProcessTime;time++){
            boolean hasStarvation = false;

            if (processIndex < process.size()  && time == process.get(processIndex).arriveTime ) {
                queueProcess.add(process.get(processIndex));
                processIndex++;
            }

            for (Process p : queueProcess) {
                p.incrementWaitingTimeCounter();
            }
            for (Process process : queueProcess) {
                if (process.waitingTimeCounter == starvationTime) {
                    if (runiningProcess.processID != -1) {
                        queueProcess.add(runiningProcess);
                    }
                    process.waitingTimeCounter = 0;
                    runiningProcess = process;
                    queueProcess.remove(process);
                    hasStarvation = true;
                    break;
                }
            }

            if(!hasStarvation){
                if (queueProcess.size()!=0) {
                    shortestJopProcess = queueProcess.poll();
                }
                if (runiningProcess.processID != -1) {
                    if (runiningProcess.remainingTime > shortestJopProcess.remainingTime) {
                        queueProcess.add(runiningProcess);
                        runiningProcess = shortestJopProcess;
                    }else{
                        if (!runiningProcess.equals(shortestJopProcess))
                            queueProcess.add(shortestJopProcess); 
                    }
                }else{
                    runiningProcess = shortestJopProcess;
                }
            }

            if (runiningProcess.processID != -1) {
                if (runiningProcess.startingTime == -1) {
                    runiningProcess.startingTime = time;
                }
                timeline.add(runiningProcess.processID);
                // System.out.println("Time: "+time+" Process: "+runiningProcess.processID);
                runiningProcess.remainingTime--;     
                if (runiningProcess.remainingTime == 0) {
                    runiningProcess.completionTime = time+1;
                    finishedProcess.add(runiningProcess);
                    runiningProcess = new Process();
                }
            }  
            
       }

       
        printTimeline();
        calcAndPrintWaitingAndTurnarround();

        return timeline;
    
    }

    private void printTimeline(){
        int i =0;
        int j = 0;
        while (true) {
                System.out.print("Start time from "+i);
                for(;i<timeline.size();i++){
                    if(timeline.get(i) != timeline.get(j)){
                        break;
                    }
                }
                j = i;
                String processName = "";
                for (Process process : finishedProcess) {
                    if (process.processID == timeline.get(i-1)) {
                        processName = process.name;
                        break;
                    }
                }
                System.out.println(" to "+i+" for process "+ processName);
                if (j>=timeline.size()) {
                    break;
                }
        }
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
    
}



 class PriorityScheduling {
    private List<ProcessD> timeline = new ArrayList<>();
    private List<Process> processes;
    private ArrayList<Process> ganttChart;
    private HashMap<Integer, Integer> waitingTime;
    private HashMap<Integer, Integer> turnAroundTime;
    private ArrayList<Process> arrivedProcesses;

    public PriorityScheduling(List<Process> processes) {
        this.processes = processes;
        ganttChart = new ArrayList<Process>();
        waitingTime = new HashMap<Integer, Integer>();
        turnAroundTime = new HashMap<Integer, Integer>();
        arrivedProcesses = new ArrayList<Process>();

        // Sort the processes by priority ascendingly
        processes.sort((p1, p2) -> p1.getArriveTime() - p2.getArriveTime());
    }

    public PriorityScheduling() {
        processes = new ArrayList<Process>();
        ganttChart = new ArrayList<Process>();
        waitingTime = new HashMap<Integer, Integer>();
        turnAroundTime = new HashMap<Integer, Integer>();
        arrivedProcesses = new ArrayList<Process>();

    }

    public int getHighestPriorityIndex(ArrayList<Process> processes) {
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

    public int getLowestPriorityIndex(ArrayList<Process> processes) {
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

    public int searchProcessIndex(List<Process> processes, int id) {
        for (int i = 0; i < processes.size(); i++) {
            if (processes.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public List<ProcessD>  buildGanttChart() {
        int time = processes.get(0).getArriveTime();
        int starv = 0;
        int index = 0;
        int size = processes.size();
        int k = 0;
       
        while (size > 0) {
            arrivedProcesses.clear();
            if (starv == 3) {
                starv = 0;
                for (int i = 0; i < processes.size(); i++) {
                    processes.get(i).setPriority(processes.get(i).getPriority() - 1);
                }
            } else {
                starv++;
                 processes.get(k).startingTime = time;
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
        printGanttChart();
        printWaitingTime();
        printTurnAroundTime();
        printAverageWaitingTime();  
        printAverageTurnAroundTime();
        return timeline;
    }

    public void   printGanttChart() {
        System.out.println("Gantt Chart");
        System.out.println("-----------");
        for (Process p : ganttChart) {
            System.out.println(p.getId() + " name: " + p.name + " Start time: " + p.getStartingTime() + " Finish time: " + p.getCompletionTime());
            ProcessD pd = new ProcessD(p.getStartingTime(),p.getCompletionTime(),p.getId());
            timeline.add(pd);
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
        averageWaitingTime /= (double)waitingTime.size();
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
        averageTurnAroundTime /= (double)turnAroundTime.size();
        System.out.println(averageTurnAroundTime);
        System.out.println();
    }
}



class AG {
    int quantum;
    private List<Integer> timeline = new ArrayList<>();
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

    public List<Integer> roundRobinUsingQueue(){
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
            timeline.add(runningProcess.processID);
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
                timeline.add(runningProcess.processID);
                timeline.add(runningProcess.processID);

                runningProcess = temp;
                System.out.print(runningProcess.name + " from time "+ time);
                timeline.add(runningProcess.processID);
                timeline.add(runningProcess.processID);

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
                        timeline.add(runningProcess.processID);
                        runningProcess = processes.get(processIndex);
                        processIndex++;
                        
                        break;
                    } else {
                        timeline.add(runningProcess.processID);
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
                    timeline.add(runningProcess.processID);
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
                    timeline.add(runningProcess.processID);

                    runningProcess = readyQueue.poll();
                    break;
                }  else {
                    timeline.add(runningProcess.processID);
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
        return timeline;
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


public class App {
      public static void main(String[] args) throws Exception {
        List<Process> processes = new ArrayList<>();
        // processes.add(new Process(1, 0,8,4));
        // processes.add(new Process(2, 1,4, 9));
        // processes.add(new Process(3, 2,9, 3));
        // processes.add(new Process(4, 3,5,8));
        
        // processes.add(new Process(1,"pp1",  "red", 0,6,4));
        // processes.add(new Process(2,"pp2",  "red", 1,2, 9));
        // processes.add(new Process(5,"pp3",  "red",7,1, 3));
        // processes.add(new Process(3,"pp4",  "red", 2,5,8));
        // processes.add(new Process(4,"pp5",  "red",3,6,8));

        // processes.add(new Process(1,"pp1",  "red", 0,17,4));
        // processes.add(new Process(2,"pp2",  "red", 3,6, 9));
        // processes.add(new Process(3,"pp3",  "red", 4,10,2));
        // processes.add(new Process(4,"pp4",  "red",29,4,8));

        // PriorityScheduling P = new PriorityScheduling(processes);
        // P.buildGanttChart();
        // P.printGanttChart();
        // P.printAverageWaitingTime();
        // P.printAverageTurnAroundTime();

        // SJF sjf = new SJF(processes);
        // sjf.sjfScheduling(0);
       

        // AG ag = new AG(processes, 4);
        // ag.roundRobinUsingQueue();

        System.out.println("Welcome to Operating System Scheduler^_^");        
        System.out.println("Note: All output required you can find in terminal\n but we also provide you with Gantt Chart for each algorithm in a seperate window");
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
            List<ProcessD> sjftimeline = sjf.sjfScheduling(contextSTime);
            PlotWindow sjfwindow =  new PlotWindow("Gantt Chart", sjftimeline,"sjf");
            sjfwindow.setVisible(true);
            sjfwindow.pack();
            sjfwindow.setLocationRelativeTo(null);
            break;
          case 2 :
            SRTF srtf = new SRTF(processes);
            List<Integer> srtftimeline = srtf.SRTFScheduling();

            PlotWindow srtfwindow =  new PlotWindow("Gantt Chart", srtftimeline);
            srtfwindow.setVisible(true);
            srtfwindow.pack();
            srtfwindow.setLocationRelativeTo(null);
            break;
          case 3 :
            PriorityScheduling P = new PriorityScheduling(processes);
            List<ProcessD> ptimeline =   P.buildGanttChart();

            PlotWindow pfwindow =  new PlotWindow("Gantt Chart", ptimeline,"p");
            pfwindow.setVisible(true);
            pfwindow.pack();
            pfwindow.setLocationRelativeTo(null);
            break;
          case 4 :
             AG ag = new AG(processes, quantumTime);
             List<Integer> agtimeline = ag.roundRobinUsingQueue();

            PlotWindow agwindow =  new PlotWindow("Gantt Chart", agtimeline);
            agwindow.setVisible(true);
            agwindow.pack();
            agwindow.setLocationRelativeTo(null);
             break;
          default : System.out.println("Invalid Choice!");
        }
      
    }
}
