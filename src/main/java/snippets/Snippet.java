package snippets;

import com.alibaba.fastjson.JSON;

/*
@author mahongyue@guazi.com
@date 2018/4/20
*/
public class Snippet{

    public static void main(String... args){
        int intValue = 4;
        Object o = intValue;
        if (o instanceof Integer) {
            System.out.println(o.toString() + " is instanceof Integer");
        }
        User u = new User("my name");
        try {
            String uString = JSON.toJSONString(u);
            System.out.println(uString);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
