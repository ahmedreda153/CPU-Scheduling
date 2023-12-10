import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;



public class SRTF {
    List<Process> process;
    PriorityQueue<Process> queueProcess;
    List<Process> finishedProcess;

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
    }
    public void SRTFScheduling()
    {
        int totalProcessTime=0;
        for(Process p:process){
            totalProcessTime += p.burstTime;
        }
       int time = 0;
       int processIndex = 0;
       Process shortestJopProcess = new Process();
       Process runiningProcess= new Process();
       System.out.println(totalProcessTime);
       for(time=0;time<=totalProcessTime;time++){


           if (processIndex < process.size()  && time == process.get(processIndex).arriveTime ) {
               queueProcess.add(process.get(processIndex));
               processIndex++;
            }
           
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
            
            if (runiningProcess.processID != -1) {
                if (runiningProcess.startingTime == -1) {
                    runiningProcess.startingTime = time;
                }
                // System.out.println("Time "+time+" " + runiningProcess.processID + " "+runiningProcess.burstTime+" "+runiningProcess.remainingTime );
                System.out.println("Time "+time+" P" + runiningProcess.processID );
                runiningProcess.remainingTime--;     
                if (runiningProcess.remainingTime == 0) {
                    runiningProcess.completionTime = time+1;
                    finishedProcess.add(runiningProcess);
                    runiningProcess = new Process();
                }
            }

            // System.out.println("Processes at Wating Queue:");
            // for(Process p :queueProcess){
            //      System.out.println("Time "+time+" " + p.processID + " "+p.burstTime+" "+p.remainingTime );
            // }
            // System.out.println();
             
            
       }
       int totalTurnarround = 0 ;
       int totalWaitingTime = 0;
       for(Process p : finishedProcess){
            p.turnarround = p.completionTime-p.arriveTime;
            p.waitingTime = p.turnarround - p.burstTime;
            totalTurnarround+=p.turnarround;
            totalWaitingTime += p.waitingTime;
            System.out.println("P"+p.processID+ " turnaround: "+p.turnarround+", waiting time: "+p.waitingTime);
       }

       int avgTurnarround = totalTurnarround / finishedProcess.size() ;
       int avgWaitingTime = totalWaitingTime / finishedProcess.size() ;

       
        System.out.println("Avg Turnarround: "+ avgTurnarround);
        System.out.println("Avg Waiting Time: "+avgWaitingTime);

    
    }
    
}
