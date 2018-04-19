package future;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class TimeMain {

    public static <U> Object blockingGet(Supplier<U> supplier) throws InterruptedException, ExecutionException{

        // create a future
        CompletableFuture future = CompletableFuture.supplyAsync(supplier);
        return future.get();

    }

    public static <U> Object blockingGetWithTimeConsuming(TimeConsumingUtil consuming, Supplier<U> supplier) throws InterruptedException, ExecutionException{
        Long begin = System.nanoTime();
        Object result = blockingGet(supplier);
        Long end = System.nanoTime();
        consuming.setConsumingNanoTime(end - begin);
        return result;
    }

    public static <U> CompletableFuture<U> nonBlockingGet(Supplier<U> supplier){
        return CompletableFuture.supplyAsync(supplier);
    }

    public static void main(String... args) throws InterruptedException, ExecutionException, IOException{
        HttpTask task = new HttpTask();
        TimeConsumingUtil uu1 = new TimeConsumingUtil();
        String result = (String)blockingGet(() -> {
            try {
                return task.doHttp("http://baidu.com");
            }catch (Throwable ta){
                System.out.println(ta);
                throw new RuntimeException(ta);
            }
        });
        System.out.println("result in main: " + result);
        System.out.println("main thread ends");
        System.in.read();
    }
}
