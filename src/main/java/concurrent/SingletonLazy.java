package concurrent;

/*
@author mahongyue@guazi.com
@date 2018/4/19
*/
public class SingletonLazy {

    private static volatile SingletonLazy instance;

    public static SingletonLazy getInstance() {
        final SingletonLazy result = instance; // volatile read
        return instance == null ? maybeCompute() : result;
    }

    private synchronized static SingletonLazy maybeCompute(){
        if (instance == null){
            System.out.println("MUST invoked once!");
            instance = new SingletonLazy();
        }
        return instance;
    }

    public static void main(String... args){
        for (int i=0; i< 100; ++i){
            new Thread(() -> { // a Runnable lambda
                SingletonLazy singleton = null;
                try {
                    Thread.sleep(10);
                    singleton = SingletonLazy.getInstance();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(singleton);
            }).start();
        }

    }
}
