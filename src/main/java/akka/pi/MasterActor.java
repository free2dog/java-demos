package akka.pi;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.routing.RoundRobinPool;

public class MasterActor extends UntypedAbstractActor{
    int numsWorkers;
    long numsRounds;
    long numsElements;
    ActorRef workerRouter;
    ActorRef stoper;
    int currentRound = 0;
    double piApproxi = 0.0;
    private final long beginTimeMs = System.currentTimeMillis();

    public MasterActor(int nWorkers, long nRounds, long nElements, ActorRef stoper) {
        this.numsWorkers = nWorkers;
        this.numsRounds = nRounds;
        this.numsElements = nElements;
        this.stoper = stoper;

        workerRouter = this.getContext().actorOf(Props.create(WorkerActor.class).withRouter(new RoundRobinPool(numsWorkers)),
                "workerRouter");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof CalculateMessage){
            for (int roundCount = 0; roundCount < numsRounds; roundCount++) {
                workerRouter.tell(new WorkMessage(roundCount, numsElements), getSelf());
            }
        }else if (message instanceof ResultMessage){
            ResultMessage msg = (ResultMessage) message;
            double result = msg.getValue();
            piApproxi += result * 4;
            ++ currentRound;
            if (currentRound >= numsRounds){
                long now = System.currentTimeMillis();
                this.stoper.tell(new PiApproximation(this.piApproxi, now - beginTimeMs), getSelf());
                getContext().stop(getSelf());
            }

        }else{
            unhandled(message);
        }
    }
}
