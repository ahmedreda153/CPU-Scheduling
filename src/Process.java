public class Process {
    int processID;
    int burstTime;
    int startingTime;
    int remainingTime;
    int completionTime;
    int arriveTime;
    int turnarround;
    int waitingTime;
    int priority;

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

    public Process(int id, int arrivalTime, int burstTime, int priority) {
        this.processID = id;
        this.arriveTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
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

}