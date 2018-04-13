# java-future-demo

非阻塞IO/异步/并行
--------

非阻塞指线程处理异步任务时，当异步任务获取到数据时使用回调函数处理数据，而不是CPU空闲等待数据返回后再处理。

使用场景
-------

Restful服务往往部署在不同的物理机器上，通过Http协议进行调用，如果直接在主线程中进行IO操作，主线程将阻塞以等待请求完成。而Java原生多线程编程繁琐而且容易出错，线程池也无法完美解决主线程阻塞的问题，Future以同步代码风格编写异步调用，提供主线程无需阻塞的方法，对于微服务架构是一个很好的并发解决方案。

在实际的web项目中，传统的服务器后端，往往使用线程池做请求处理，每一个请求到来独占一个线程，当涉及io操作时，线程阻塞以等待IO完成，当IO耗时太长或者IO操作太频繁时，线程长时间无法释放，导致线程池可用线程不足，后续的请求被迫排队，服务器吞吐量受到严重影响。

另一个场景不太常见，一段代码中包含多个计算密集型任务（如超大矩阵计算）时，在同一个线程中线性执行使得整段代码运行时间过长，这时使用异步执行可以适当加速可并行执行的代码。

Java CompletableFuture Demo
------------------

使用前提：java8

Java8吸收了Scala的Future和其他基于事件驱动的异步调用框架的优点，在原先的Future接口之上实现了CompletableFuture。

CompletableFuture 通过回调函数，实现非阻塞的IO的异步调用。
 

使用场景：Restful服务往往部署在不同的物理机器上，通过Http协议进行调用，如果直接在主线程中进行IO操作，主线程将阻塞以等待请求完成。而Java原生多线程编程繁琐而且容易出错，线程池也无法完美解决主线程阻塞的问题，Future以同步代码风格编写异步调用，提供主线程无需阻塞的方法，对于微服务架构是一个很好的并发解决方案。

全部Demo代码

Main.java

```java
import java.io.IOException;

import java.util.concurrent.CompletableFuture;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpTask task = new HttpTask();

        System.out.println("main thread starts in thread id: "+ Thread.currentThread().getId());

        CompletableFuture futureNonBlocking = CompletableFuture.supplyAsync(() -> {
            try {
                return task.doHttp("https://guazi.com");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "error";
        });
        CompletableFuture futureBlocking = CompletableFuture.supplyAsync(() -> {
            try {
                return task.doHttp("https://guazi.com");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "error";
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
    }
}
```

HttpTask.java

```java
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpTask {
    public String doHttp(String url) throws IOException, InterruptedException {
        long threadId = Thread.currentThread().getId();
        OkHttpClient client = new OkHttpClient();
        System.out.println("http io starts in thread id: "+ threadId);

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String result = response.headers().toString();

        // mock delay
        for (int i=1; i<=3; ++i){
            // 阻塞本线程1秒
            Thread.sleep(1000);
            System.out.println("delayed "+ i + " seconds in thread id: "+ threadId);
        }

        return "******** headers in thread id "+threadId+" ***********\n"+result+"***************** headers ******************";
    }
}
```

输出结果
-------

![终端输出](https://upload-images.jianshu.io/upload_images/37272-d6be35b75851cf39.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

本Demo中有三个线程执行，已通过线程ID标识，非阻塞线程（这里线程id为10）异步执行，结果通过回调函数处理。线程id为11的同样异步执行，但是Future接口的get()方法使主线程（id为1）阻塞以等待结果到达。

总结

    线程10 → 异步 + 非阻塞IO
    线程11 → 异步 + 阻塞IO
    
推荐使用 异步 + 非阻塞IO 的方式，这也是 ```Java1.8``` 的 ```CompletableFuture``` 和 ```Java1.5``` 的 ```Future``` 的不同之处。

可以看到：

**阻塞位置1** 总是在 ```blocking future``` 的 ```get()```方法**之后**执行。

使用姿势
-----------

#### 不关心返回结果

这种情况毋庸置疑: 异步 + 非阻塞

#### 必须获取到返回结果程序才能继续执行

这种情况下有两种选择

1. 异步 + 阻塞 (前文已述) 说明一点,这种方式并不是完全失去了性能上的提升,因为在另一进程在未执行完成时,本进程不是必须在空等,而是可以做自己的数据处理,两个进程并发执行,虽然有时候快的那个要等待慢的那个,但是这比非异步也是有很大的性能提升的.如果实际场景中返回结果耗时太长,比如下载批量图片,请使用方法2

2. 使用回调函数. 这种方法可以实现 异步+非阻塞的效果,但是复杂逻辑场景中会出现回调函数嵌套层数增加的混乱. trade-offs :-) :-)

参考：

- [Scala Futures and Promise](http://docs.scala-lang.org/overviews/core/futures.html)
- [LinkedIn微服务架构并发场景分析](https://engineering.linkedin.com/play/play-framework-async-io-without-thread-pool-and-callback-hell)
- [Java CompletableFuture 详解](http://colobu.com/2016/02/29/Java-CompletableFuture/)
