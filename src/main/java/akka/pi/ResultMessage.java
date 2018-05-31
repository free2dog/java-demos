package akka.pi;

public class ResultMessage {
    private double value;

    public ResultMessage(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
