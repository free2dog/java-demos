package akka.pi;

import akka.actor.UntypedAbstractActor;

public class WorkerActor extends UntypedAbstractActor{

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof WorkMessage){
            WorkMessage wmsg = (WorkMessage) message;
            long start = wmsg.getRound() * wmsg.getNumsElements();
            long end = (wmsg.getRound()+1) * wmsg.getNumsElements() -1;
            double result = 0;
            for (long i = start; i <= end; ++ i){
                double flag = 1 - i%2*2;
                result += flag / (2 * i +1);
            }
            getSender().tell(new ResultMessage(result), getSelf());
        }else{
            unhandled(message);
        }
    }
}
