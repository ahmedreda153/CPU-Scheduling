public class Process {
    int processID;
    int burstTime;
    int startingTime;
    
    public Process(int processID, int burstTime) {
        this.processID = processID;
        this.burstTime = burstTime;
    }
    void setStartingTime(int startingTime){
        this.startingTime=startingTime;
    }

}
