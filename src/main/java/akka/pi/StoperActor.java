package akka.pi;

import akka.actor.UntypedAbstractActor;

public class StoperActor extends UntypedAbstractActor {
    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof PiApproximation) {
            PiApproximation approximation = (PiApproximation) message;
            System.out.printf("Pi: %s\nActor-based consuming milliseconds: %s\n",
                    approximation.getApproximation(), approximation.getConsumingTimeMs());
            getContext().system().terminate();
        } else {
            unhandled(message);
        }
    }
}
