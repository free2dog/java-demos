package zookeeper;

/*
@author mahongyue@guazi.com
@date 2018/5/2
*/
public interface Constants {
    /*

    sessionTimeoutMs: 6000
    connectionTimeoutMs: 6000
    maxRetries: 3
    baseSleepTimeMs: 1000
     */
    String zkServer = "127.0.0.1:2181";
    int sessionTimeoutMs = 6000;
    int connectionTimeoutMs = 6000;
    int maxRetries = 3;
    int baseSleepTimeMs = 1000;
}
