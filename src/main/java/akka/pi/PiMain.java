package akka.pi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PiMain {
    static final int numsWorkers = 15;
    static final long numsRounds = 100000;
    static final long numsElements = numsRounds;

    // must volatile: multi thread read and write may
    static volatile double piAppro = 0.0;
    public static void main(String... args){

//        System.out.printf("number of rounds: %s, range per round: %s\n", numsRounds, numsElements);
//
//        // Create an Akka system
//        ActorSystem system = ActorSystem.create("PiSystem");
//
//        // create the result listener, which will print the result and shutdown the system
//        final ActorRef stoper = system.actorOf(Props.create(StoperActor.class), "stoper");
//
//        // create the master
//        ActorRef master = system.actorOf(Props.create(MasterActor.class, numsWorkers, numsRounds, numsElements, stoper),"master");
//
//        // start the calculation
//        master.tell(new CalculateMessage(), system.lookupRoot());

        // thread based calculate
        calculateUsingThread();
    }

    public static void calculateUsingThread(){
        long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        for (long count=0; count<numsRounds; ++ count){
            final long cf = count;
            executor.submit(() -> {
                double result = oneRoundCalculate(cf, numsElements);
                reduceResult(result);
            });
        }
        try {
            executor.shutdown();
            //主线程轮询，判断任务结束
            while(!executor.isTerminated()){
                Thread.sleep(500);
                long endTime = System.currentTimeMillis();
                System.out.printf("Pi: %s\nthread-based consuming milliseconds: %s\n", piAppro,(endTime - startTime));
            }

            long endTime = System.currentTimeMillis();
            System.out.printf("Pi: %s\nthread-based consuming milliseconds: %s\n", piAppro,(endTime - startTime));
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private static double oneRoundCalculate(long round, long n){
        long start = round * n;
        long end = (round+1) * n -1;
        double result = 0;
        for (long i = start; i <= end; ++ i){
            double flag = 1 - i%2*2;
            result += flag / (2 * i +1);
        }
        return result;
    }

    private static synchronized void reduceResult(double result){
        piAppro += result *4;
    }
}
