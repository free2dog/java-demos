package akka.pi;

public class WorkMessage {
    private int round;
    private int numsElements;

    public WorkMessage(int round, int numsElements) {
        this.round = round;
        this.numsElements = numsElements;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getNumsElements() {
        return numsElements;
    }

    public void setNumsElements(int numsElements) {
        this.numsElements = numsElements;
    }
}
