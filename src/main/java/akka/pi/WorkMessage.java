package akka.pi;

public class WorkMessage {
    private long round;
    private long numsElements;

    public WorkMessage(long round, long numsElements) {
        this.round = round;
        this.numsElements = numsElements;
    }

    public long getRound() {
        return round;
    }

    public void setRound(long round) {
        this.round = round;
    }

    public long getNumsElements() {
        return numsElements;
    }

    public void setNumsElements(long numsElements) {
        this.numsElements = numsElements;
    }
}
