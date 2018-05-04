package snippets;

import jdk.nashorn.internal.objects.annotations.Getter;

import java.io.Serializable;

/*
@author mahongyue@guazi.com
@date 2018/4/20
*/
public class User{

    private String name;

    public void setName(String name){this.name = name;}

    public String getName() {return name;}

    User(String name) {this.name = name;}

    @Override
    public String toString(){
        return "name: " + this.name;
    }
}
