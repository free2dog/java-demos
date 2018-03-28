import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/*
@author mahongyue@guazi.com
@date 2018/3/28
*/
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
