package akka.pi;

public class PiApproximation {
    private double approximation;
    private long consumingTimeMs;

    public PiApproximation(double approximation, long consumingTimeMs) {
        this.approximation = approximation;
        this.consumingTimeMs = consumingTimeMs;
    }

    public double getApproximation() {
        return approximation;
    }

    public void setApproximation(double approximation) {
        this.approximation = approximation;
    }

    public long getConsumingTimeMs() {
        return consumingTimeMs;
    }

    public void setConsumingTimeMs(long consumingTimeMs) {
        this.consumingTimeMs = consumingTimeMs;
    }



}
