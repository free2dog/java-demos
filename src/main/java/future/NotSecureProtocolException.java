package future;

/*
@author mahongyue@guazi.com
@date 2018/4/18
*/
public class NotSecureProtocolException extends Exception{
    @Override
    public String toString(){
        return "should use TLS/LLS";
    }
}
