

public class Process  {
    int processID;
    int burstTime;
    int startingTime;
    int remainingTime;
    int completionTime;
    int arriveTime;
    int turnarround;
    int waitingTime;

    public Process(){
        this.burstTime = 0;
        this.completionTime = 0;
        this.startingTime = 0 ;
        this.processID = -1;
        this.remainingTime =0;
        this.arriveTime = 0;
    }
    
    public Process(int processID, int burstTime) {
        this.processID = processID;
        this.burstTime = burstTime;
    }
     public Process(int processID, int burstTime,int arriveTime) {
        this.processID = processID;
        this.burstTime = burstTime;
        this.arriveTime=arriveTime;
        this.startingTime = -1;
        this.remainingTime = burstTime;
    }
    
    void setStartingTime(int startingTime){
        this.startingTime=startingTime;
    }

    void setRemainingTime(int reminingTime){
        this.remainingTime = reminingTime;
    }

    void setCompletionTime(int completionTime){
        this.completionTime = completionTime;
    }

    void setArriveTime(int arriveTime){
        this.arriveTime = arriveTime;
    }
   

    
}
