package concurrent;

/*
@author mahongyue@guazi.com
@date 2018/4/19

*/
public class SingletonLazy {

    private static volatile SingletonLazy instance;

    // 读
    public static SingletonLazy getInstance() {
        final SingletonLazy result = instance; // volatile read
        return instance == null ? maybeNewInstance() : result;
    }

    // 写:加锁
    private synchronized static SingletonLazy maybeNewInstance(){
        if (instance == null){   // volatile read
            System.out.println("MUST invoked once!");
            instance = new SingletonLazy();
        }
        return instance;
    }

    public static void main(String... args){
        for (int i=0; i< 1000; ++i){
            new Thread(() -> { // a Runnable lambda
                SingletonLazy singleton = null;
                try {
                    Thread.sleep(10000);
                    singleton = SingletonLazy.getInstance();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(singleton);
            }).start();
        }

    }
}
