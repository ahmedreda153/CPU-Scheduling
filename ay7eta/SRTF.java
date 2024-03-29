import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;



public class SRTF {
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
