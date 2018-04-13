import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;

public class TimeMain {

    public static <U> Object blockingGet(Supplier<U> supplier){
        Future future = CompletableFuture.supplyAsync(supplier);
        Object o = null;
        try{
            o = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return o;
    }

    public static <U> Object blockingGetWithTimeConsuming(TimeConsumingUtil consuming, Supplier<U> supplier){
        Long begin = System.nanoTime();
        Object result = blockingGet(supplier);
        Long end = System.nanoTime();
        consuming.setConsumingNanoTime(end - begin);
        return result;
    }

    public static <U> CompletableFuture<U> nonBlockingGet(Supplier<U> supplier){
        return CompletableFuture.supplyAsync(supplier);
    }

    public static void main(String... args) throws IOException {
        HttpTask task = new HttpTask();
        TimeConsumingUtil uu1 = new TimeConsumingUtil();
        CompletableFuture future = nonBlockingGet(() -> {
            try {

                return task.doHttp("http://baidu.com");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "error";
        });
        future.thenAcceptAsync(result ->
            System.out.println(result)
        );
        System.out.println("main thread ends");
        System.in.read();
    }
}
