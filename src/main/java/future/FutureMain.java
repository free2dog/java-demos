package future;

import java.io.IOException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class FutureMain {
    public static void main(String[] args) throws Exception {
        HttpTask task = new HttpTask();

        System.out.println("main thread starts in thread id: "+ Thread.currentThread().getId());

        // 新建一个Future
        CompletableFuture futureNonBlocking = CompletableFuture.supplyAsync(() -> {
            try {
                return task.doHttp("http://guazi.com");
            } catch (Exception e) {
                e.printStackTrace();
                throw new CompletionException(e);
            }
        });
        // 新建一个Future
        CompletableFuture futureBlocking = CompletableFuture.supplyAsync(() -> {
            try {
                return task.doHttp("https://guazi.com");
            } catch (Exception e) {
                e.printStackTrace();
                throw new CompletionException(e);
            }
        });

        // 非阻塞
        System.out.println("-----------------非阻塞位置1----------------------\n");
        // future.thenAcceptAsync() 方法不阻塞本线程，http请求成功后执行回调函数

            futureNonBlocking.thenAcceptAsync(result -> System.out.println("\nfrom non blocking future:\n"+result+"\n"));


        System.out.println("-----------------非阻塞位置2----------------------\n");

        // 阻塞
        System.out.println("\nfrom blocking future:\n"+futureBlocking.get()+"\n");
        // future.get() 方法阻塞本线程，直到http请求成功
        System.out.println("-----------------阻塞位置1----------------------\n");
        System.out.println("main thread ends");
        //防止主线程在其他线程运行完成之前退出,导致打印信息不全
        System.in.read();
    }
}
